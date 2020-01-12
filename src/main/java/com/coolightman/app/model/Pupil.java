package com.coolightman.app.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pupils")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
public class Pupil extends User {

    @Column(name = "first_name")
    private String firstName;

    private String surname;

    private LocalDate dob;

    @ManyToOne
    @JoinColumn(name = "aclass_id")
    private AClass aClass;
}
