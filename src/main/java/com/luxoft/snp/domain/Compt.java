package com.luxoft.snp.domain;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import javax.persistence.*;

@JsonAutoDetect
@Entity(name="Compt")
@Table(name="Compt")
public class Compt  {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="compt_id", unique = true, nullable=false)
	private int id;


    @Column(name="label", nullable=false)
	private String label;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="packet_id_fk", nullable=false)
	private Packet packet;

	public Compt(String label, Packet packet) {
		this.label = label;
		this.packet = packet;

	}

	private Compt() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }


	@Override
	public boolean equals(Object otherObject) {
        Compt otherCompt = (Compt)otherObject;
		if(otherCompt.getId() == this.id) {
			return true;
		} else {
			return false;
		}
	}
}