package com.armdoctor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.print.Doc;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "hospital")
@NoArgsConstructor
@AllArgsConstructor
public class HospitalEntity {
    @Id
    @Column(name = "hospital_id")
    private Integer hospitalId;
    private String name;
    private String address;
    @ManyToMany(mappedBy = "hospitalEntities")
    Set<DoctorEntity> entiteaaaa;

}
