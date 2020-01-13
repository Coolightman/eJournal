package com.coolightman.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * The type A class.
 */
@Entity
@Table(name = "aclasses")
@Data
@EqualsAndHashCode(callSuper = true)
public class AClass extends BaseClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
}
