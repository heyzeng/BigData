package com.work.operate;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;
/**
 * Author:ZJF
 * Date:2020-12-10 上午10:00
 */

public class HdfsMultipleTextOutputFormat<K,V> extends MultipleTextOutputFormat<K,V> {
    @Override
    protected String generateFileNameForKeyValue(K key, V value, String name) {
        return String.valueOf(key);
    }
    @Override
    protected K generateActualKey(K key, V value) {
        return (K) NullWritable.get();
    }
}

