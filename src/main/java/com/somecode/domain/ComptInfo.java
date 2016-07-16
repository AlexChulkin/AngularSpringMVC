package com.somecode.domain;

import javax.persistence.NamedQuery;

/**
 * Created by alexc_000 on 2016-07-16.
 */
@NamedQuery(name = "ComptInfo.getCompts", query = "SELECT DISTINCT NEW com.somecode.domain.ComptInfo(c.id, c.label) " +
        "FROM Packet p JOIN p.compts c WHERE p.id = :packetId ORDER BY c.id ASC  ")
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
