package com.somecode.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NamedQuery(name = "Packet.getAllPackets",
        query = "SELECT NEW com.somecode.domain.PacketInfo(p.id, p.state.id) FROM Packet p ORDER BY p.id ASC")
public class Packet implements EntityType {

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
    @Column(name = "PACKET_ID", length = 11)
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
    @JoinColumn(name = "STATE_ID_FK")
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @OneToMany(mappedBy = "packet", cascade = CascadeType.ALL, orphanRemoval = true)
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

    @Transient
    public List<Long> getComptIds() {
        return getCompts().stream().map(Compt::getId).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "\nPacket with id: " + id + "\nand state: " + state;
    }
}
