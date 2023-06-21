package com.armdoctor.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@Embeddable
@Data
public class DBjoin implements Serializable {
    @Column(name = "user_id")
    private Integer doctorId;
    @Column(name = "hospital_id")
    private String hospitalId;
}
