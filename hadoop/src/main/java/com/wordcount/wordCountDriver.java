package com.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Author:Jude
 * Date:2020-09-29 9:38 上午
 */
public class wordCountDriver {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        // 1 获取配置信息以及封装任务
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        // 2 设置jar加载路径
        job.setJarByClass(wordCountDriver.class);

        // 3 设置map和reduce类
        job.setMapperClass(wordCountMapper.class);
        job.setReducerClass(wordCountReducer.class);

        // 4 设置map输出
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 5 设置最终输出kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 6 设置输入和输出路径
        //FileInputFormat.setInputPaths(job, new Path(args[0]));
        //FileOutputFormat.setOutputPath(job, new Path(args[1]));

        //如果文件在hdfs上地址需要如下这样设置
        //FileInputFormat.setInputPaths(job, "hdfs://127.0.0.1:8088/source/word/");
        //FileOutputFormat.setOutputPath(job, new Path("hdfs://127.0.0.1:8088/output/word"));


        //test
        FileInputFormat.setInputPaths(job, "/Users/judezeng/Desktop/GoodGoodStudy/BigData/hadoop/src/main/resources/word");
        FileOutputFormat.setOutputPath(job, new Path("/Users/judezeng/Desktop/GoodGoodStudy/BigData/hadoop/src/main/resources/output"));

        job.waitForCompletion(true);


        // 7 提交
        //boolean result = job.waitForCompletion(true);
        //System.exit(result ? 0 : 1);
    }
}
