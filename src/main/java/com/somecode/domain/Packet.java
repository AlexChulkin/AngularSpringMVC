package com.somecode.domain;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Packet {

    private long id;

    private int version;

    private State state;

    private Set<Compt> compts = new HashSet<>();

    public Packet() {  }

    public Packet(long id, State state) {
        this.id = id;
        this.state = state;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "PACKET_ID", length = 11, nullable = false)
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
    @JoinColumn(name="STATE_ID_FK", nullable=false)
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @OneToMany(mappedBy = "packet", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    public Set<Compt> getCompts() {
        return compts;
    }

    public void setCompts(Set<Compt> compts){
        this.compts = compts;
    }

    public void addCompt(Compt compt) {
        compt.setPacket(this);
        getCompts().add(compt);
    }

    public void removeCompt(Compt compt){
        compt.setPacket(null);
        getCompts().remove(compt);
    }

    @Override
    public String toString() {
        return "\nPacket with id: " + id + "\nand state: " + state;
    }
}
