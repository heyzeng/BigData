package com.wordcount;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class Flink01_WC_Batch {

    public static void main(String[] args) throws Exception {

        // 拿到执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // 获取数据源
        DataSource<String> dataLine = env.readTextFile("/Users/judezeng/Desktop/GoodGoodStudy/BigData/hadoop/src/main/resources/word");

        // 按照word分组
        FlatMapOperator<String, Tuple2<String, Integer>> wordsAndOne = dataLine.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] words = value.split(" ");
                for (String word : words) {
                    out.collect(new Tuple2<String, Integer>(word, 1));
                }
            }
        });

        // word 分组
        UnsortedGrouping<Tuple2<String, Integer>> wordGroup = wordsAndOne.groupBy(0);

        //分组内聚合
        AggregateOperator<Tuple2<String, Integer>> wordSum = wordGroup.sum(1);

        //打印
        wordSum.print();

        env.execute("Flink01_WC_Batch");

    }
}
