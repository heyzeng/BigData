package com.learning.chapter02;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * Author:Jude
 * Date:2023-04-10 上午9:10
 */
public class BoundedStreamWordCount {
    public static void main(String[] args) throws Exception {

        // 1 执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 2 读取数据
        DataStreamSource<String> readTextData = env.readTextFile("/Users/judezeng/IDEAProject/BigData/flink/FlinkTutorial/input/words.txt");
        // 3 数据进行二元组包装转换
        SingleOutputStreamOperator<Tuple2<String, Long>> wordOutOne = readTextData.flatMap((String line, Collector<Tuple2<String, Long>> out) -> {
            String[] words = line.split(" "); // 每行数据按照空格切分
            for (String word : words) {
                out.collect(Tuple2.of(word, 1L)); // 每个单词后追加1
            }
        }).returns(Types.TUPLE(Types.STRING,Types.LONG));//当使用Lambda表达式时候，会对泛型的类型进行擦除，需要指定数据类型

        //按照key值进行分类
        KeyedStream<Tuple2<String, Long>, Tuple> tuple2TupleKeyedStream = wordOutOne.keyBy(0);
        SingleOutputStreamOperator<Tuple2<String, Long>> sum = tuple2TupleKeyedStream.sum(1); // 求和

        sum.print();
        // 流式执行环境
        env.execute();
    }
}
