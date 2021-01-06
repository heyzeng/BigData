package com.wordcount;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Author:ZJF
 * Date:2021-01-06 上午9:32
 */
public class StreamWordCount {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<String> dataStream = env.readTextFile("/Users/judezeng/Desktop/GoodGoodStudy/BigData/flink/src/main/resources/a.txt");

        SingleOutputStreamOperator<Tuple2<String, Integer>> dataResult = dataStream.flatMap(new BatchWordCount.MyFlatMapFunction())
                .keyBy(0)
                .sum(1);

        dataResult.print();

        env.execute();

    }
}
