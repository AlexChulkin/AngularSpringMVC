package com.luxoft.snp.domain;


import javax.persistence.*;

@Entity
public class Compt  {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String label;





//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name="packet_id_fk", nullable=false)
//	  private Packet packet;


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
}