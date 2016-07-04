package com.somecode.domain;


import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name="DATA_COMPT")
public class DataCompt {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="DATA_COMPT_ID", length = 21, unique = true, nullable=false)
    private long id;

    @ManyToOne
    @JoinColumn(name="COMPT_ID_FK", nullable=false)
    private Compt compt;


    @ManyToOne
    @JoinColumn(name="STATIC_DATA_ID_FK", nullable=false)
    private StaticData staticData;

    @ManyToOne
    @JoinColumn(name="STATE_ID_FK", nullable=false)
    private State state;

    @Column(name="CHECKED", nullable=false, length = 1)
    @Type(type="yes_no")
    private boolean checked;



    public DataCompt() {

    }

    public DataCompt(Compt compt, StaticData staticData, State state, long selected) {
        this.compt = compt;
        this.staticData = staticData;

        this.state = state;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public StaticData getStaticData() {
        return staticData;
    }

    public void setStaticData(StaticData staticData) {
        this.staticData = staticData;
    }

    public Compt getCompt() {
        return compt;
    }

    public void setCompt(Compt compt) {
        this.compt = compt;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "Data Component with id: " + id + " and checked flag: " + checked + "\nand component: " + compt +
                "\nand state: " + state + "\nand static data: " + staticData;
    }

}
