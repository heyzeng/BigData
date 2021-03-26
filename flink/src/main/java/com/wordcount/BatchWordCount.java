package com.wordcount;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;


public class BatchWordCount {

    public static void main(String[] args) throws Exception {

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<String> data = env.readTextFile("/Users/judezeng/Desktop/GoodGoodStudy/BigData/flink/src/main/resources/a.txt");

        AggregateOperator<Tuple2<String, Integer>> resultData = data.flatMap(new MyFlatMapFunction())
                .groupBy(0)// 按照第一个位置的word分组
                .sum(1);//按照第二个位置数据求和

        resultData.print();

    }

    //自定义类 ,做一个二元组Tuple(word,1)
    public static class MyFlatMapFunction implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
            String[] words = value.split(" ");
            for (String word : words) {
                out.collect(new Tuple2<>(word, 1));
            }
        }
    }
}


