package com.coolightman.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * The type Parent.
 */
@Entity
@Table(name = "parents")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@EqualsAndHashCode(callSuper = true)
public class Parent extends User {

    @OneToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "pupil_id")
    private Pupil pupil;
}
