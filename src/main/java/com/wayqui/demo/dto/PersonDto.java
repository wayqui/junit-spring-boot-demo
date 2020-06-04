package com.wayqui.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PersonDto {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Integer age;
    private String email;
}
