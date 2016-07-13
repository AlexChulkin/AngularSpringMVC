package com.somecode.domain;


import javax.persistence.*;

@Entity
public class Compt  {


    private long id;
    private String label;
	private Packet packet;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="COMPT_ID", length = 11, unique = true, nullable=false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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