package com.somecode.domain;


import javax.persistence.*;

@Entity
public class Packet {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="PACKET_ID", unique = true, nullable=false)
    private long id;

    @ManyToOne
    @JoinColumn(name="STATE_ID_FK", nullable=false)
    private State state;

    public Packet() {

    }

    public Packet(long id, State state) {
        this.id = id;
        this.state = state;

    }
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String toString() {
        return "packageId " + id + " state " + state ;
    }
}
