package com.SparkCore

import org.apache.spark.{SparkConf, SparkContext}

/**
 * Author:Jude 
 * Date:2021-03-26 上午9:17 
 */
object RDDTest {
  def main(args: Array[String]): Unit = {

    val markRDD = new SparkConf().setMaster("local").setAppName("RDDTest")
    val sc = new SparkContext(markRDD)
    val value = sc.parallelize(Array(1, 2, 3, 4), 1)
    val result = value.flatMap(x => Array(x, x * x, x * x * x))

    result.collect().foreach(println)

    sc.stop()
  }
}
