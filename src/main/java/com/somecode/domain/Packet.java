package com.somecode.domain;


import javax.persistence.*;

@Entity
public class Packet {

    private long id;

    private State state;

    public Packet() {  }

    public Packet(long id, State state) {
        this.id = id;
        this.state = state;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="PACKET_ID", length = 11, unique = true, nullable=false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="STATE_ID_FK", nullable=false)
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Packet with id: " + id + "\nand state: " + state ;
    }
}
