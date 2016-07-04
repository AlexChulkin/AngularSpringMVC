package com.somecode.domain;


import javax.persistence.*;

@Entity
public class Packet {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="PACKET_ID", length = 11, unique = true, nullable=false)
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

    @Override
    public String toString() {
        return "Packet with id: " + id + "\nand state: " + state ;
    }
}
