package com.somecode.domain;


import javax.persistence.*;

import static com.somecode.utils.Utils.getMessage;

/**
 * Created by achulkin on 03.06.14.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"LABEL"}))
public class State implements EntityType {
    private static final String ID_COLUMN = "STATE_ID";
    private static final String STRING_VERSION = "state.toString";
    private static final int ID_COLUMN_LENTGTH = 2;
    private static final int LABEL_COLUMN_LENTGTH = 20;

    private Long id;
    private String label;
    private Integer version;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = ID_COLUMN, length = ID_COLUMN_LENTGTH, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Version
    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Column(nullable = false, length = LABEL_COLUMN_LENTGTH)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return getMessage(STRING_VERSION, new Object[]{id, label});
    }
}
