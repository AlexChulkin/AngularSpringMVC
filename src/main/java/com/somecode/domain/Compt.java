package com.somecode.domain;


import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Compt  {


    private long id;
    private String label;
	private Packet packet;
    private Set<DataCompt> dataCompts = new HashSet<>();

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="COMPT_ID", length = 11, unique = true, nullable=false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "compt", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    public Set<DataCompt> getDataCompts() {
        return Collections.unmodifiableSet(dataCompts);
    }

    public void setDataCompts(Set<DataCompt> dataCompts) {  }

    @Column(name="LABEL", length = 75)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @ManyToOne
    @JoinColumn(name="packet_id_fk", nullable=false)
    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    @Override
    public String toString() {
        return "Component with id: " + id + " and label: "+label+"\nand packet: " + packet ;
    }
}