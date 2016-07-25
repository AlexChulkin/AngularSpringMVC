package com.somecode.domain;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries( {
        @NamedQuery(name = "Compt.getInfo", query = "SELECT NEW com.somecode.domain.ComptInfo(c.id, c.label) " +
                "FROM Packet p JOIN p.compts c WHERE p.id = :packetId ORDER BY c.id ASC"),
        @NamedQuery(name="Compt.getSupplInfo",
                query="SELECT NEW com.somecode.domain.ComptSupplInfo(c.id, dc.state.id, sd.label, dc.checked) " +
                        "FROM Packet p JOIN p.compts c JOIN c.dataCompts dc JOIN dc.comboData sd " +
                        "WHERE p.id = :packetId ORDER BY dc.id ASC")
})
public class Compt  {


    private long id;
    private String label;
	private Packet packet;
    private List<DataCompt> dataCompts = new ArrayList<>();
    private int version;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "COMPT_ID", length = 11, nullable = false)
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

    @OneToMany(mappedBy = "compt", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @OrderBy
    public List<DataCompt> getDataCompts() {
        return dataCompts;
    }

    public void setDataCompts(List<DataCompt> dataCompts) {
        this.dataCompts = dataCompts;
    }

    public void addDataCompt(DataCompt dataCompt){
        dataCompt.setCompt(this);
        getDataCompts().add(dataCompt);
    }

    public void removeDataCompt(DataCompt dataCompt) {
        dataCompt.setCompt(null);
        getDataCompts().remove(dataCompt);
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