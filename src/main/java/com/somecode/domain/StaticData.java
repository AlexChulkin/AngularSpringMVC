package com.somecode.domain;


import javax.persistence.*;


@Entity
@Table(name="STATIC_DATA")
@NamedQuery(name="StaticData.getComptsData",
        query="SELECT DISTINCT dc.id, c.id, dc.state.id, sd.label, dc.checked " +
                "FROM Packet p JOIN p.compts c JOIN c.dataCompts dc JOIN dc.staticData sd " +
                "WHERE p.id = :packetId ORDER BY dc.id")

public class StaticData {

    private long id;

    private String label;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="STATIC_DATA_ID", length = 11, unique = true, nullable=false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name="LABEL", nullable=false, length = 20)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Static data with id: "+id+" and label: "+label;
    }
}