package com.rey.material.demo;

/**
 * Created by zhaoheri on 3/5/15.
 */
public class Rule {
    private long id;
    private String title;
    private String date;
    private String start_time;
    private String end_time;
    private String volume;

    public Rule(String t, String d, String s, String e, String v) {
        title = t;
        date = d;
        start_time = s;
        end_time = e;
        volume = v;
    }

    public Rule() { }

    public String getDate() {
        return this.date;
    }

    public String getTitle() {
        return this.title;
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

    public void setTitle(String title) {this.title = title;}

    public void setDate(String date) {this.date = date;}

    public void setStart_time(String startTime) {this.start_time = startTime;}

    public void setEnd_time(String endTime) {this.end_time = endTime;}

    public void setVolume(String volume) {this.volume = volume;}

}
