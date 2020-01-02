package com.coolightman.app.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "pupils")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
public class Pupil extends User {

    @Column(name = "first_name")
    @NotNull(message = "{pupil.firstName.notNull}")
    @NotEmpty(message = "{pupil.firstName.notEmpty}")
    @Size(min = 2, max = 80, message = "{pupil.firstName.size}")
    private String firstName;

    @NotNull(message = "{pupil.surname.notNull}")
    @NotEmpty(message = "{pupil.surname.notEmpty}")
    @Size(min = 2, max = 80, message = "{pupil.surname.size}")
    private String surname;

    @NotNull(message = "{pupil.dob.notNull}")
    private LocalDate dob;

    @ManyToOne
    @JoinColumn(name = "aclass_id")
    private AClass aClass;
}
