package com.armdoctor.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "related")
public class Related implements Serializable {
    @Id
    private Integer user_id;
    @Id
    private Integer hospital_id;
}
