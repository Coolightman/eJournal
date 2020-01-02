package com.coolightman.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "grades")
@Data
@EqualsAndHashCode(callSuper = true)
public class Grade extends BaseClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{grade.value.notNull}")
    @NotEmpty(message = "{grade.value.notEmpty}")
    @Range(min = 1, max = 10, message = "{grade.value.range}")
    private Short value;

    @NotNull(message = "{grade.date.notNull}")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "pupil_id")
    private Pupil pupil;
}
