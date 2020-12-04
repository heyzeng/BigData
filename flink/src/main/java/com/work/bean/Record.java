package com.work.bean;

/**
 * Author:Jude
 * Date:2020-12-03 上午10:15
 */
public class Record {

    private String device_id;
    private String resource_id;
    private String value;
    private String source;
    private String s1;
    private String s2;
    private String s3;
    private String s4;
    private String s5;
    private String s6;
    private String s7;
    private String heard_beat;
    private String user_id;
    private String data_time;

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public String getS3() {
        return s3;
    }

    public void setS3(String s3) {
        this.s3 = s3;
    }

    public String getS4() {
        return s4;
    }

    public void setS4(String s4) {
        this.s4 = s4;
    }

    public String getS5() {
        return s5;
    }

    public void setS5(String s5) {
        this.s5 = s5;
    }

    public String getS6() {
        return s6;
    }

    public void setS6(String s6) {
        this.s6 = s6;
    }

    public String getS7() {
        return s7;
    }

    public void setS7(String s7) {
        this.s7 = s7;
    }

    public String getHeard_beat() {
        return heard_beat;
    }

    public void setHeard_beat(String heard_beat) {
        this.heard_beat = heard_beat;
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

    @Override
    public String toString() {
        return "Record{" +
                "device_id='" + device_id + '\'' +
                ", resource_id='" + resource_id + '\'' +
                ", value='" + value + '\'' +
                ", source='" + source + '\'' +
                ", s1='" + s1 + '\'' +
                ", s2='" + s2 + '\'' +
                ", s3='" + s3 + '\'' +
                ", s4='" + s4 + '\'' +
                ", s5='" + s5 + '\'' +
                ", s6='" + s6 + '\'' +
                ", s7='" + s7 + '\'' +
                ", heard_beat='" + heard_beat + '\'' +
                ", user_id='" + user_id + '\'' +
                ", data_time='" + data_time + '\'' +
                '}';
    }
}
