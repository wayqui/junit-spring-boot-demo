package com.wayqui.demo.controller.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PersonResponse {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Integer age;
    private String email;
}
