package com.work.bean;

/**
 * Author:Jude
 * Date:2020-12-03 上午10:15
 */
public class Record {

    private String device_id;
    private String resource_id;
    private String value;
    private String user_id;
    private String data_time;

    @Override
    public String toString() {
        return "Record{" +
                "device_id='" + device_id + '\'' +
                ", resource_id='" + resource_id + '\'' +
                ", value='" + value + '\'' +
                ", user_id='" + user_id + '\'' +
                ", data_time='" + data_time + '\'' +
                '}';
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getResource_id() {
        return resource_id;
    }

    public void setResource_id(String resource_id) {
        this.resource_id = resource_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getData_time() {
        return data_time;
    }

    public void setData_time(String data_time) {
        this.data_time = data_time;
    }
}

