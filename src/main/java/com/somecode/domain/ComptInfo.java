package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-07-16.
 */


public class ComptInfo {
    private long id;
    private String label;

    public ComptInfo(long id, String label) {
        this.id = id;
        this.label = label;
    }

    public long getId() {
        return id;
    }

//    public void setId(long id) {
//        this.id = id;
//    }

    public String getLabel() {
        return label;
    }

//    public void setLabel(String label) {
//        this.label = label;
////    }
}