package com.coolightman.app.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "teachers")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
public class Teacher extends User {

    @Column(name = "first_name")
    private String firstName;

    private String surname;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "teachers_disciplines",
            joinColumns = {@JoinColumn(name = "teacher_id")},
            inverseJoinColumns = {@JoinColumn(name = "discipline_id")})
    private List<Discipline> disciplines;
}
