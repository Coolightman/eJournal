package com.coolightman.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

/**
 * The type Teacher.
 */
@Entity
@Table(name = "teachers")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@EqualsAndHashCode(callSuper = true)
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
