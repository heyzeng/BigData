package com.learning.chapter02;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * Author:Jude
 * Date:2023-04-09 下午9:11
 */
public class BatchWordCount {
    public static void main(String[] args) throws Exception {
        // 1 创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        // 2 从文件中读取数据
        DataSource<String> readFile = env.readTextFile("/Users/judezeng/IDEAProject/BigData/flink/FlinkTutorial/input/words.txt");
        // 3 转换数据格式、用二元组去包装，
        FlatMapOperator<String, Tuple2<String, Long>> wordAndOne = readFile.flatMap((String line, Collector<Tuple2<String, Long>> out) -> {
            String[] words = line.split(" "); // 对每行数据按照空格切分
            for (String word : words) {//遍历每个单词
                out.collect(Tuple2.of(word, 1L));//每个单词后面加1
            }
            //当Lambda表达式使用Java泛型的时候，由于泛型擦除的存在，需要显示的申明类型信息
        }).returns(Types.TUPLE(Types.STRING, Types.LONG));

        // 4 按照word进行分组
        UnsortedGrouping<Tuple2<String, Long>> tuple2UnsortedGrouping = wordAndOne.groupBy(0);

        // 5 分组聚合
        AggregateOperator<Tuple2<String, Long>> sum = tuple2UnsortedGrouping.sum(1);

        sum.print();
    }
}
