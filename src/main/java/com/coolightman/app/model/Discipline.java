package com.coolightman.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * The type Discipline.
 */
@Entity
@Table(name = "disciplines")
@Data
@EqualsAndHashCode(callSuper = true)
public class Discipline extends BaseClass {

    @Column(unique = true, nullable = false)
    private String name;
}
