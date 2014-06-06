package com.luxoft.snp.domain;


import javax.persistence.*;

/**
 * Created by achulkin on 03.06.14.
 */
@Entity
public class State {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="STATE_ID", unique = true, nullable=false)
    private int id;

    @Column(name="LABEL", nullable=false, length = 20)
    private String label;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
