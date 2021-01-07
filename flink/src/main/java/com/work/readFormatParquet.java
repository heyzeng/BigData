package com.work;

import com.work.bean.Record;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.GroupReduceOperator;
import org.apache.flink.api.java.typeutils.PojoTypeInfo;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;
import org.apache.flink.formats.parquet.ParquetPojoInputFormat;
import org.apache.flink.util.Collector;
import org.apache.parquet.avro.AvroSchemaConverter;
import org.apache.parquet.schema.MessageType;

import java.util.HashMap;

/**
 * Author:Jude
 * Date:2020-12-02 下午8:41
 */
public class readFormatParquet {

    public static final String ONLINE = "1";
    public static final String OFFLINE = "0";

    public static void main(String[] args) throws Exception {

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();


        //file path
        String topicResourceStreamFile = "hdfs://10.10.20.12:8020/test/device/device";
        String iotDeviceFile = "hdfs://10.10.20.12:8020/test/part-m-00000";
        String outPut = "hdfs://10.10.20.12:8020/test/iot_device";


        //read iotDevice
        DataSource<String> iotDevice = env.readTextFile(iotDeviceFile);
        DataSet<Record> iotDeviceTo = iotDevice.map(s -> {
            String[] strings = s.split("\t");
            Record record = new Record();
            record.setDevice_id(strings[0]);
            record.setValue(strings[1]);
            record.setData_time("1606838400000");
            return record;
        }).returns(Record.class);
//        iotDeviceTo.print();

        //read parquet
        PojoTypeInfo<Record> typeInfo = (PojoTypeInfo<Record>) PojoTypeInfo.of(Record.class);
        Schema schema = ReflectData.get().getSchema(Record.class);
        MessageType messageType = new AvroSchemaConverter().convert(schema);
        Path path = new Path(topicResourceStreamFile);
        ParquetPojoInputFormat<Record> recordParquetPojoInputFormat = new ParquetPojoInputFormat<>(path, messageType, typeInfo);
        DataSource<Record> topicResourceStream = env.createInput(recordParquetPojoInputFormat, typeInfo);

        //to dataset
        DataSet<Record> topicResourceStreamTo = topicResourceStream;

        // two stream union
        GroupReduceOperator<Record, Object> operator = topicResourceStream.filter(new FilterFunction<Record>() {
            @Override
            public boolean filter(Record value) throws Exception {
                return value.getResource_id().equals("8.0.2045");
            }
        }).union(iotDeviceTo)
//                .map(s -> {
//                    if (s.getData_time().equals("1606752000")) {
//                        System.out.println(s);
//                    }
//                    return s;
//                })
                .groupBy(s -> s.getDevice_id())
                .sortGroup(s -> s.getData_time(), Order.ASCENDING)
                .reduceGroup(new GroupReduceFunction<Record, Object>() {
                    @Override
                    public void reduce(Iterable<Record> values, Collector<Object> out) throws Exception {

                        int index = 0;
                        long offlineTime = 0;
                        Record last = null;
                        HashMap<String, Integer> deviceIDAndOffTime = new HashMap<>();

                        for (Record record : values) {
                            index++;
                            if (index == 1) {
                                last = record;
                            } else {
                                Record current = record;
                                // remove repetition
                                if (last.getValue().equals(current.getValue())) {
                                    continue;
                                }
                                if (last.getValue().equals(OFFLINE) && current.getValue().equals(ONLINE)) {
                                    offlineTime += Long.parseLong(current.getData_time()) - Long.parseLong(last.getData_time());
                                }
                                last = current;
                            }
                        }

                        if (index == 1) {
                            if (last.getValue().equals(OFFLINE)) {
                                offlineTime = 24 * 60 * 60 * 1000;
                            }
                        }
                        out.collect(deviceIDAndOffTime);
                    }
                });
        operator.print();


//        operator.writeAsText("hdfs://10.10.20.12:8020/test/aa", FileSystem.WriteMode.OVERWRITE);

    }
}



