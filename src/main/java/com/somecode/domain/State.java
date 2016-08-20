package com.somecode.domain;


import javax.persistence.*;

/**
 * Created by achulkin on 03.06.14.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"LABEL"}))
public class State implements EntityType {

    private Long id;

    private String label;

    private Integer version;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "STATE_ID", length = 2, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Version
    @Column(name = "VERSION", nullable = false)
    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Column(name = "LABEL", nullable = false, length = 20)
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
