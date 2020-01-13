package com.coolightman.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * The type Grade.
 */
@Entity
@Table(name = "grades")
@Data
@EqualsAndHashCode(callSuper = true)
public class Grade extends BaseClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Short value;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "pupil_id")
    private Pupil pupil;
}
