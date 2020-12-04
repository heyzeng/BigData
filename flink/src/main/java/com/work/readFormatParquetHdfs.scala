package com.work

import org.apache.flink.api.java.ExecutionEnvironment
import org.apache.flink.core.fs.Path
import org.apache.flink.formats.parquet.ParquetRowInputFormat
import org.apache.log4j.{Level, Logger}
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName
import org.apache.parquet.schema.Type.Repetition
import org.apache.parquet.schema.{MessageType, PrimitiveType}

/**
 * Author:Jude
 * Date:2020-12-02 上午10:20
 */
object readFormatParquetHdfs {
  def main(args: Array[String]): Unit = {

    Logger.getLogger("org.apache.flink").setLevel(Level.ERROR)
    val env = ExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val topicResourceStreamFile = "/Users/judezeng/Downloads/000000_9"
    val iotDeviceFile = "/Users/judezeng/Downloads/part-m-00000"

    /**
     * 指定schema信息
     */
    val device_id = new PrimitiveType(Repetition.OPTIONAL, PrimitiveTypeName.BINARY, "device_id")
    val resource_id = new PrimitiveType(Repetition.OPTIONAL, PrimitiveTypeName.BINARY, "resource_id")
    val value = new PrimitiveType(Repetition.OPTIONAL, PrimitiveTypeName.INT32, "value")
    val source = new PrimitiveType(Repetition.OPTIONAL, PrimitiveTypeName.BINARY, "source")
    val s1 = new PrimitiveType(Repetition.OPTIONAL, PrimitiveTypeName.BINARY, "s1")
    val s2 = new PrimitiveType(Repetition.OPTIONAL, PrimitiveTypeName.BINARY, "s2")
    val s3 = new PrimitiveType(Repetition.OPTIONAL, PrimitiveTypeName.BINARY, "s3")
    val s4 = new PrimitiveType(Repetition.OPTIONAL, PrimitiveTypeName.BINARY, "s4")
    val s5 = new PrimitiveType(Repetition.OPTIONAL, PrimitiveTypeName.BINARY, "s5")
    val s6 = new PrimitiveType(Repetition.OPTIONAL, PrimitiveTypeName.BINARY, "s6")
    val s7 = new PrimitiveType(Repetition.OPTIONAL, PrimitiveTypeName.BINARY, "s7")
    val heard_beat = new PrimitiveType(Repetition.OPTIONAL, PrimitiveTypeName.BINARY, "heard_beat")
    val user_id = new PrimitiveType(Repetition.OPTIONAL, PrimitiveTypeName.BINARY, "user_id")
    val data_time = new PrimitiveType(Repetition.OPTIONAL, PrimitiveTypeName.BINARY, "data_time")

    val topResourceStreamSchema = new MessageType("t1", device_id, resource_id, value,source,s1,s2,s3,s4,s5,s6,s7,heard_beat,user_id,data_time)
    print(s"topResourceStreamSchema : ${topResourceStreamSchema}")

    val t1 = env
      .readFile(new ParquetRowInputFormat(new Path(topicResourceStreamFile), topResourceStreamSchema), topicResourceStreamFile)
//      .flatMap(new FlatMapFunction[String, BaseBean] {
//
//      })
//      .map(s => f2(s))

    t1.print()





    /**
     * get file schema
     */
    val t2 = env.readTextFile(iotDeviceFile)
//    t2.print()

//   t1.union(t2)

//    val conf = new Configuration(true)

    env.execute("readFormatParquet")
  }

//  def f2(x:Row):BaseBean ={
//    val baseBean: ResourceBean = new ResourceBean();
//    baseBean.value = x.getField(2).toString.toInt
//    baseBean.timestamp = x.getField(13).toString.toLong
//    baseBean
//  }
}
