package com.somecode.domain;


import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "DATA_COMPT", uniqueConstraints = @UniqueConstraint(columnNames = {"COMPT_ID_FK", "STATE_ID_FK", "COMBO_DATA_ID_FK"}))
public class DataCompt {

    private long id;
    private Compt compt;
    private ComboData comboData;
    private State state;
    private boolean checked;
    private int version;

    public DataCompt() {  }

    public DataCompt(Compt compt, ComboData comboData, State state, long selected) {
        this.compt = compt;
        this.comboData = comboData;
        this.state = state;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "DATA_COMPT_ID", length = 21)
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

    @NotNull
    @ManyToOne
    @JoinColumn(name = "COMBO_DATA_ID_FK")
    public ComboData getComboData() {
        return comboData;
    }

    public void setComboData(ComboData comboData) {
        this.comboData = comboData;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "COMPT_ID_FK")
    public Compt getCompt() {
        return compt;
    }

    public void setCompt(Compt compt) {
        this.compt = compt;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "STATE_ID_FK")
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Column(name = "CHECKED", length = 1)
    @Type(type="yes_no")
    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "\nData Component with id: " + id + " and checked flag: " + checked + "\nand component: " + compt +
                "\nand state: " + state + "\nand combo data: " + comboData;
    }

}
