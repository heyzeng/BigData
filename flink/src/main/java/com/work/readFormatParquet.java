package com.work;

import com.work.bean.Record;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.typeutils.PojoTypeInfo;
import org.apache.flink.core.fs.Path;
import org.apache.flink.formats.parquet.ParquetPojoInputFormat;
import org.apache.flink.util.Collector;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.parquet.avro.AvroSchemaConverter;
import org.apache.parquet.schema.MessageType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author:Jude
 * Date:2020-12-02 下午8:41
 */
public class readFormatParquet {

    public static void main(String[] args) throws Exception{


        Logger.getLogger("org.apache.flink").setLevel(Level.ERROR);
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();


        //file path
        String topicResourceStreamFile = "/Users/judezeng/Downloads/000023_0";
        String iotDeviceFile = "/Users/judezeng/Downloads/part-m-00000";


        //read iotDevice
        DataSource<String> iotDevice = env.readTextFile(iotDeviceFile);
        DataSet<Record> iotDeviceTo = iotDevice.map(s -> {
            String[] strings = s.split("\t");
            Record record = new Record();
            record.setDevice_id(strings[0]);
            record.setValue(strings[1]);
            record.setData_time("1606752000");
            return record;
        }).returns(Record.class);
//        iotDeviceTo.print();


        //read parquet
        PojoTypeInfo<Record> typeInfo = (PojoTypeInfo<Record>)PojoTypeInfo.of(Record.class);
        Schema schema = ReflectData.get().getSchema(Record.class);
        MessageType messageType = new AvroSchemaConverter().convert(schema);
        Path path = new Path(topicResourceStreamFile);
        ParquetPojoInputFormat<Record> recordParquetPojoInputFormat = new ParquetPojoInputFormat<>(path, messageType, typeInfo);
        DataSource<Record> topicResourceStream = env.createInput(recordParquetPojoInputFormat, typeInfo);

        //to dataset
        DataSet<Record> topicResourceStreamTo = topicResourceStream;


        // two stream union
        iotDeviceTo.union(topicResourceStreamTo)
//                .map(s -> {
//                    if (s.getData_time().equals("1606752000")) {
//                        System.out.println(s);
//                    }
//                    return s;
//                })
                .groupBy(s->s.getDevice_id())
                .sortGroup(s -> s.getData_time(), Order.ASCENDING)
                .reduceGroup(new GroupReduceFunction<Record, Object>() {
                    @Override
                    public void reduce(Iterable<Record> values, Collector<Object> out) throws Exception {

//                        ArrayList<Integer> resultList = new ArrayList<>();

                        int index = 0;
                        int offlineTime = 0;
                        HashMap<String, Integer> deviceIDAndOffTime = new HashMap<>();
                        for (Record record : values){

                            index++;
                            Record last = record;


                            if (index == 1){
                                last = record;
                               if (last.getValue().equals("0")){
                                   offlineTime = 7777;
                                   deviceIDAndOffTime.put(record.getDevice_id(),offlineTime);
                               }else {
                                   break;
                               }
                            } else {
                                Record current = record;
                                if (current.getValue().equals("1") && last.getValue().equals(" 0")){
                                    offlineTime = Integer.parseInt(current.getData_time()) - Integer.parseInt(last.getData_time());
                                    deviceIDAndOffTime.put(record.getDevice_id(),offlineTime);
                                    current = last;
                                } else if((current.getValue().equals("0") && last.getValue().equals("1"))
                                        || (current.getValue().equals("0") && last.getValue().equals("0"))
                                        ||(current.getValue().equals("1") && last.getValue().equals( "1"))){
                                    continue;
                                }
                            }
                        }


//                        while (values.iterator().hasNext()){
//
//                            Record record = values.iterator().next();
//                            Record record_next = values.iterator().next();
//
//                            offlineTime = Integer.parseInt(record_next.getData_time()) - Integer.parseInt(record.getData_time());
//                            HashMap<String, Integer> deviceIDAndOffTime = new HashMap<>();
//                            if (record.getValue().equals("0") && record_next.getValue().equals("1")){
//                                if (deviceIDAndOffTime.containsKey(record.getDevice_id())){
//                                    int Time = deviceIDAndOffTime.get(record.getDevice_id()) + offlineTime;
//                                    deviceIDAndOffTime.put(record.getDevice_id(),Time);
//                                }else {
//                                    deviceIDAndOffTime.put(record.getDevice_id(),offlineTime);
//                                }
//                            }
//                        }
//                        resultList.add(offlineTime);
                        out.collect(deviceIDAndOffTime);
                    }
                }).print();

        //执行
//        env.execute("readFormatParquet");
    }
}



