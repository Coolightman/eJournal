package com.coolightman.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "disciplines")
@Data
@EqualsAndHashCode(callSuper = true)
public class Discipline extends BaseClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotNull(message = "{discipline.name.notNull}")
    @NotEmpty(message = "{discipline.name.notEmpty}")
    @Size(min = 5, max = 80, message = "{discipline.name.size}")
    private String name;
}
