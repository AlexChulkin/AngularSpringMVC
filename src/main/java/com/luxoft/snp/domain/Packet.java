package com.luxoft.snp.domain;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import javax.persistence.*;

@JsonAutoDetect
@Entity(name="Packet")
@Table(name="PACKET")
public class Packet {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="PACKET_ID", unique = true, nullable=false)
	private int id;


    @Column(name="STATE_ID_FK", nullable=false, length=1)
	private int state;

	public Packet() {
		
	}
	
	public Packet(int id, int state) {
		this.id = id;
		this.state = state;

	}
    public int getState() {
        return state;
    }

    public void setState(int state) {
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
