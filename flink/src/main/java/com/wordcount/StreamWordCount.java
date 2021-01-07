package com.wordcount;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Author:ZJF
 * Date:2021-01-06 上午9:32
 */
public class StreamWordCount {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment()
                .setParallelism(4);

        // 1 文件路径不能代表实时流
        //DataStream<String> dataStream = env.readTextFile("/Users/judezeng/Desktop/GoodGoodStudy/BigData/flink/src/main/resources/a.txt");

        //2 nc 测试流
        //DataStream<String> dataStream = env.socketTextStream("", 9999);

        //3 用parameter tool工具从程序启动参数中提取配置项
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String host = parameterTool.get("host");
        int port = parameterTool.getInt("port");

        DataStreamSource<String> dataStream = env.socketTextStream(host, port);

        SingleOutputStreamOperator<Tuple2<String, Integer>> dataResult = dataStream.flatMap(new BatchWordCount.MyFlatMapFunction())
                .keyBy(0)
                .sum(1);

        dataResult.print();

        env.execute();

    }
}
