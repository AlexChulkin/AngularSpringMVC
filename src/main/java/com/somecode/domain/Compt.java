package com.somecode.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"PACKET_ID_FK", "LABEL"}))
@NamedQueries( {
        @NamedQuery(name = "Compt.getInfo", query = "SELECT NEW com.somecode.domain.ComptInfo(c.id, c.label) " +
                "FROM Packet p JOIN p.compts c WHERE p.id = :packetId ORDER BY c.id ASC"),
        @NamedQuery(name = "Compt.getSupplInfo",
                query = "SELECT NEW com.somecode.domain.ComptSupplInfo(c.id, dc.state.id, cd.label, dc.checked) " +
                        "FROM Packet p JOIN p.compts c JOIN c.dataCompts dc JOIN dc.comboData cd " +
                        "WHERE p.id = :packetId ORDER BY dc.id ASC"),
        @NamedQuery(name = "Compt.findAllWithDataCompts",
                query = "SELECT DISTINCT c FROM Compt c ORDER BY c.id ASC")
})
@NamedEntityGraph(name = "Compt.getGraphWithDataCompts",
        attributeNodes = {
                @NamedAttributeNode("id"),
                @NamedAttributeNode(value = "packet", subgraph = "packetGraph"),
                @NamedAttributeNode(value = "dataCompts", subgraph = "dataComptGraph")
        },
        subgraphs = {
                @NamedSubgraph(name = "packetGraph",
                        attributeNodes = {
                                @NamedAttributeNode("id")
                        }
                ),
                @NamedSubgraph(name = "dataComptGraph",
                        attributeNodes = {
                                @NamedAttributeNode("checked"),
                                @NamedAttributeNode(value = "state", subgraph = "stateGraph"),
                                @NamedAttributeNode(value = "comboData", subgraph = "comboDataGraph")
                        }
                ),
                @NamedSubgraph(name = "comboDataGraph",
                        attributeNodes = {
                                @NamedAttributeNode("label")
                        }
                ),
                @NamedSubgraph(name = "stateGraph",
                        attributeNodes = {
                                @NamedAttributeNode("id")
                        }
                )
        }
)
public class Compt  {

    private long id;
    private String label;
	private Packet packet;
    private List<DataCompt> dataCompts = new ArrayList<>();
    private int version;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "COMPT_ID", length = 11)
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

    @NotEmpty(message = "Compt label can't be empty")
    @Size(max = 75, message = "Compt label length can't exceed 75 symbols")
    @Column
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "packet_id_fk")
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