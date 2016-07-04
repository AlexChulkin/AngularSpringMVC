package com.somecode.domain;


import javax.persistence.*;

/**
 * Created by achulkin on 03.06.14.
 */
@Entity
public class State {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="STATE_ID", length = 2, unique = true, nullable=false)
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

    @Override
    public String toString() {
        return "State with id: "+id+" and label: "+label;
    }
}
