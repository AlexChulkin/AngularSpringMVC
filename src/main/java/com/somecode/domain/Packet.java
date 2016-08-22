package com.somecode.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Packet implements EntityType {

    private Long id;

    private Integer version;

    private State state;

    private Set<Compt> compts = new HashSet<>();

    public Packet() {  }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "PACKET_ID", length = 11)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Version
    @Column(name = "VERSION")
    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
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
