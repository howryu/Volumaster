package com.rey.material.demo;

/**
 * Created by zhaoheri on 3/5/15.
 */
public class Rule {
    private long id;
    private String start_time;
    private String end_time;
    private String volume;

    public Rule(String s, String e, String t) {
        start_time = s;
        end_time = e;
        volume = t;
    }

    public String getStart_time () {
        return this.start_time;
    }

    public String getEnd_time () {
        return this.end_time;
    }

    public String getVolume() {
        return this.volume;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

}
