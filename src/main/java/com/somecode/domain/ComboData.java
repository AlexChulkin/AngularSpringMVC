package com.somecode.domain;


import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Entity
@Table(name = "COMBO_DATA", uniqueConstraints = @UniqueConstraint(columnNames = {"LABEL"}))
public class ComboData implements HasLabel, EntityType {

    private long id;

    private String label;

    private int version;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "COMBO_DATA_ID", length = 11)
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

    @NotEmpty(message = "ComboData label can't be empty")
    @Size(max = 20, message = "ComboData label length can't exceed 20 symbols")
    @Column(name = "LABEL")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "\nCombo data with id: " + id + " and label: " + label;
    }
}