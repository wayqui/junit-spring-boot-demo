package com.wayqui.demo.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Data
@Builder
public class Person {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
}
