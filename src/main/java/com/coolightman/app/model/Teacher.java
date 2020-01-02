package com.coolightman.app.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "teachers")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
public class Teacher extends User {

    @Column(name = "first_name")
    @NotNull(message = "{teacher.firstName.notNull}")
    @NotEmpty(message = "{teacher.firstName.notEmpty}")
    @Size(min = 2, max = 80, message = "{teacher.firstName.size}")
    private String firstName;

    @NotNull(message = "{teacher.surname.notNull}")
    @NotEmpty(message = "{teacher.surname.notEmpty}")
    @Size(min = 2, max = 80, message = "{teacher.surname.size}")
    private String surname;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "teachers_disciplines",
            joinColumns = {@JoinColumn(name = "teacher_id")},
            inverseJoinColumns = {@JoinColumn(name = "discipline_id")})
    private List<Discipline> disciplines;
}
