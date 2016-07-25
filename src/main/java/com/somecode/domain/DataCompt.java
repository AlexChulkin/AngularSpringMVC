package com.somecode.domain;


import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name="DATA_COMPT")
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
    @Column(name = "DATA_COMPT_ID", length = 21, nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Version
    @Column(name = "VERSION", nullable = false)
    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @ManyToOne
    @JoinColumn(name = "COMBO_DATA_ID_FK", nullable = false)
    public ComboData getComboData() {
        return comboData;
    }

    public void setComboData(ComboData comboData) {
        this.comboData = comboData;
    }

    @ManyToOne
    @JoinColumn(name="COMPT_ID_FK", nullable=false)
    public Compt getCompt() {
        return compt;
    }

    public void setCompt(Compt compt) {
        this.compt = compt;
    }

    @ManyToOne
    @JoinColumn(name="STATE_ID_FK", nullable=false)
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Column(name="CHECKED", nullable=false, length = 1)
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
