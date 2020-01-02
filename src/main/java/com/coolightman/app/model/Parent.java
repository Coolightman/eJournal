package com.coolightman.app.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "parents")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
public class Parent extends User {

    @OneToOne(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            })
    @JoinColumn(name = "pupil_id")
    private Pupil pupil;
}
