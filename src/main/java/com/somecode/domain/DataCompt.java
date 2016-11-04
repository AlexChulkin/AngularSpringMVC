/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;


import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.somecode.utils.Utils.getMessage;

/**
 * The self-explanatory {@link DataCompt} entity POJO class(for the {@literal DATA_COMPT} table).
 */

@Entity
@Table(name = "DATA_COMPT",
        uniqueConstraints = @UniqueConstraint(columnNames = {"COMPT_ID_FK", "STATE_ID_FK", "COMBO_DATA_ID_FK"}))
public class DataCompt implements EntityType {

    private static final String ID_COLUMN = "DATA_COMPT_ID";
    private static final String COMBO_DATA_ID_FK = "COMBO_DATA_ID_FK";
    private static final String COMPT_ID_FK = "COMPT_ID_FK";
    private static final String STATE_ID_FK = "STATE_ID_FK";
    private static final String CHECKED_COLUMN_NAME = "CHECKED";
    private static final String CHECKED_COLUMN_TYPE = "yes_no";
    private static final String STRING_VERSION = "dataCompt.toString";
    private static final int ID_COLUMN_LENGTH = 21;
    private static final int CHECKED_COLUMN_LENGTH = 1;

    private Long id;
    private Compt compt;
    private ComboData comboData;
    private State state;
    private boolean checked;
    private Integer version;

    public DataCompt() {  }

    public DataCompt(Compt compt, ComboData comboData, State state, long selected) {
        this.compt = compt;
        this.comboData = comboData;
        this.state = state;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = ID_COLUMN, length = ID_COLUMN_LENGTH)
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

    @NotNull
    @ManyToOne
    @JoinColumn(name = COMBO_DATA_ID_FK)
    public ComboData getComboData() {
        return comboData;
    }

    public void setComboData(ComboData comboData) {
        this.comboData = comboData;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = COMPT_ID_FK)
    public Compt getCompt() {
        return compt;
    }

    public void setCompt(Compt compt) {
        this.compt = compt;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = STATE_ID_FK)
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Column(name = CHECKED_COLUMN_NAME, length = CHECKED_COLUMN_LENGTH)
    @Type(type = CHECKED_COLUMN_TYPE)
    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return getMessage(STRING_VERSION, new Object[]{id, checked, compt.getId(), state.getId(), comboData.getId()});
    }

}
