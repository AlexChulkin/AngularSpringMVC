package com.somecode.domain;


import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;

import static com.somecode.helper.Helper.getMessage;


@Entity
@Table(name = "COMBO_DATA", uniqueConstraints = @UniqueConstraint(columnNames = {"LABEL"}))
public class ComboData implements EntityType {
    private final static String ID_COLUMN = "COMBO_DATA_ID";
    private final static String NOT_EMPTY_LABEL_MESSAGE = "ComboData label can't be empty";
    private final static String LABEL_SIZE_MESSAGE = "ComboData label length can't exceed 20 symbols";
    private final static String STRING_VERSION = "comboData.toString";

    private final static int COMBO_DATA_LENGTH = 11;
    private final static int MAX_LABEL_SIZE = 20;

    private Long id;

    private String label;

    private Integer version;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = ID_COLUMN, length = COMBO_DATA_LENGTH)
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

    @NotEmpty(message = NOT_EMPTY_LABEL_MESSAGE)
    @Size(max = MAX_LABEL_SIZE, message = LABEL_SIZE_MESSAGE)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return getMessage(STRING_VERSION, new Object[]{id, label});
    }
}