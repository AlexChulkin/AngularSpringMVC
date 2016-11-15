/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.somecode.utils.Utils.getMessage;

/**
 * The self-explanatory {@link Compt} entity POJO class(for the {@literal COMPT} table).
 * <p>
 * Contains the {@link ComptSupplInfo} named load queries.
 * @version 1.0
 */

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"PACKET_ID_FK", "LABEL"}))
@NamedQueries( {
        @NamedQuery(name = "Compt.loadComptsSupplInfoByPacketId",
                query = "SELECT NEW com.somecode.domain.ComptSupplInfo(c.id, dc.state.id, cd.label, dc.checked) " +
                        "FROM Packet p JOIN p.compts c JOIN c.dataCompts dc JOIN dc.comboData cd " +
                        "WHERE p.id = :packetId ORDER BY dc.id ASC"),
        @NamedQuery(name = "Compt.loadAllComptsSupplInfo",
                query = "SELECT NEW com.somecode.domain.ComptSupplInfo(c.id, dc.state.id, cd.label, dc.checked) " +
                        "FROM Compt c JOIN c.dataCompts dc JOIN dc.comboData cd ORDER BY dc.id ASC")
})
public class Compt implements EntityType, Serializable {
    private static final long serialVersionUID = 1L;

    private final static String COMPT_ID = "COMPT_ID";
    private final static String PACKET_ID_FK = "PACKET_ID_FK";
    private final static String COMPT = "compt";
    private final static String NOT_EMPTY_LABEL_MESSAGE = "Compt label can't be empty";
    private final static String LABEL_SIZE_MESSAGE = "Compt label length can't exceed 70 symbols";
    private final static String STRING_VERSION = "compt.toString";
    private final static int COMPT_LENGTH = 11;
    private final static int MAX_LABEL_SIZE = 70;

    private Long id;
    private String label;
	private Packet packet;
    private List<DataCompt> dataCompts = new ArrayList<>();
    private Integer version;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = COMPT_ID, length = COMPT_LENGTH)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Version
    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @OneToMany(mappedBy = COMPT, cascade = CascadeType.ALL, orphanRemoval = true)
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

    @NotEmpty(message = NOT_EMPTY_LABEL_MESSAGE)
    @Size(max = MAX_LABEL_SIZE, message = LABEL_SIZE_MESSAGE)
    @Column
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = PACKET_ID_FK)
    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    /**
     * Returns a brief description of this entity.
     * The pattern is the following:
     * Component with id: {link #id}, label: {@link #label} and packet#{@link #packet#id}
     */
    @Override
    public String toString() {
        return getMessage(STRING_VERSION, new Object[]{id, label, packet.getId()});
    }
}