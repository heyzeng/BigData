package com.FlinkAPI.source;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Author:Jude
 * Date:2021-07-27 下午11:04
 */
public class SourceFromFile {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<String> readTextFile = env.readTextFile("/Users/judezeng/Desktop/GoodGoodStudy/BigData/flink/src/main/resources/a.txt");

        readTextFile.print("data");

        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
