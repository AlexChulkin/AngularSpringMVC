package com.somecode.domain;


import javax.persistence.*;

@Entity
@Table(name="STATIC_DATA")
public class StaticData {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="STATIC_DATA_ID", unique = true, nullable=false)
    private long id;




    @Column(name="LABEL", nullable=false, length = 20)
    private String label;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}