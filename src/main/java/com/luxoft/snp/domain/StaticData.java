package com.luxoft.snp.domain;


import javax.persistence.*;

@Entity
@Table(name="STATIC_DATA")
public class StaticData {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="STATIC_DATA_ID", unique = true, nullable=false)
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