package com.armdoctor.model;

import com.armdoctor.enums.Role;
import com.armdoctor.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorEntity {

    @Id
    @Column(name = "user_id")
    private Integer id;
    @Column(name = "first_name")
    private String name;
    @Column(name = "last_name")
    private String surname;
    private Integer year;

    private String email;
    private String password;
    @Column(name = "verification_code")
    private String verifyCode;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "reset_token")
    private String resetToken;
    private String profession;
    @Column(name = "work_time")
    private String workTime;
    @Column(name = "book_time")
    private String bookTime;
   @ManyToMany
   @JoinTable(
           name = "related",
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "hospital_id"))
    Set<HospitalEntity> hospitalEntities;

    public DoctorEntity(Integer id, String name, String surname, String email, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }
}
