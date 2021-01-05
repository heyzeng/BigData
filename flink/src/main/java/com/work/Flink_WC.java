package com.work;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * Author:ZJF
 * Date:2021-01-05 下午12:07
 */
public class Flink_WC {

    public static void main(String[] args) throws Exception {

        ExecutionEnvironment executionEnvironment = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> textFile = executionEnvironment.readTextFile("/Users/judezeng/Desktop/GoodGoodStudy/BigData/flink/src/main/resources/a.txt");

        FlatMapOperator<String, Tuple2<String, Integer>> textFlatMap = textFile.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] s = value.split(" ");
                for (String word :
                        s) {
                    out.collect(new Tuple2<String, Integer>(word,1));
                }
            }
        });

        UnsortedGrouping<Tuple2<String, Integer>> wordGroupBy = textFlatMap.groupBy(0);
        AggregateOperator<Tuple2<String, Integer>> sum = wordGroupBy.sum(1);
        sum.print();
    }
}
