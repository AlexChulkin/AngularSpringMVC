package com.somecode.domain;


import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Created by achulkin on 03.06.14.
 */
@Entity
public class State {

    private long id;

    private String label;

    private int version;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "STATE_ID", length = 2)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Version
    @Column(name = "VERSION")
    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @NotEmpty(message = "State label can't be empty")
    @Size(max = 20, message = "State label length can't exceed 20 symbols")
    @Column(name = "LABEL")
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
