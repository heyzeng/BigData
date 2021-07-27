package com.FlinkAPI.source;

/**
 * Author:Jude
 * Date:2021-07-27 下午10:50
 */

import com.FlinkAPI.bean.Sensor;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

/**
 * 从集合中读取数据
 */
public class SourceFromCollection {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Sensor> sensorDataStreamSource = env.fromCollection(Arrays.asList(
                new Sensor("one", 2000000L, 10.0),
                new Sensor("two", 2100000L, 11.0),
                new Sensor("three", 2200000L, 12.0)
        ));

        DataStreamSource<Integer> integerDataStreamSource = env.fromElements(1, 2, 10, 100);

        integerDataStreamSource.print("int");
        sensorDataStreamSource.print("data");

        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
