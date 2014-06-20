package com.luxoft.snp.domain;


import javax.persistence.*;

@Entity
public class Packet {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="PACKET_ID", unique = true, nullable=false)
    private int id;

    @ManyToOne
    @JoinColumn(name="STATE_ID_FK", nullable=false)
    private State state;

    public Packet() {

    }

    public Packet(int id, State state) {
        this.id = id;
        this.state = state;

    }
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String toString() {
        return "packageId " + id + " state " + state ;
    }
}
