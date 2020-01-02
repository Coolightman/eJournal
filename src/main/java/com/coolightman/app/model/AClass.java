package com.coolightman.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "aclasses")
@Data
@EqualsAndHashCode(callSuper = true)
public class AClass extends BaseClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotNull(message = "{aclass.name.notNull}")
    @NotEmpty(message = "{aclass.name.notEmpty}")
    @Size(min = 2, max = 6, message = "{class.name.size}")
    private String name;
}
