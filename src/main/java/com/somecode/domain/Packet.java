/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

import static com.somecode.utils.Utils.getMessage;

/**
 * The self-explanatory {@link Packet} entity POJO class(for the {@literal PACKET} table).
 * @version 1.0
 */

@Entity
public class Packet implements EntityType {
    private static final String ID_COLUMN = "PACKET_ID";
    private static final String STATE_ID_FK = "STATE_ID_FK";
    private static final String PACKET_FOREIGN_COLUMN_MAPPING = "packet";
    private static final String STRING_VERSION = "packet.toString";
    private static final int ID_COLUMN_LENGTH = 11;

    private Long id;
    private Integer version;
    private State state;
    private List<Compt> compts = new LinkedList<>();

    public Packet() {
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = ID_COLUMN, length = ID_COLUMN_LENGTH)
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

    @NotNull
    @ManyToOne
    @JoinColumn(name = STATE_ID_FK)
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @OneToMany(mappedBy = PACKET_FOREIGN_COLUMN_MAPPING, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Compt> getCompts() {
        return compts;
    }

    public void setCompts(List<Compt> compts) {
        this.compts = compts;
    }

    public void addCompt(Compt compt) {
        compt.setPacket(this);
        getCompts().add(compt);
    }

    /**
     * Returns a brief description of this entity.
     * The pattern is the following:
     * Packet with id: {@link #id} and state: {@link #state}
     */
    @Override
    public String toString() {
        return getMessage(STRING_VERSION, new Object[]{id, state});
    }
}
