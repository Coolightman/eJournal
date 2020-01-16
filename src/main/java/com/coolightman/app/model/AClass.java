package com.coolightman.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The type A class.
 */
@Entity
@Table(name = "aclasses")
@Data
@EqualsAndHashCode(callSuper = true)
public class AClass extends BaseClass {

    @Column(unique = true, nullable = false)
    private String name;
}
