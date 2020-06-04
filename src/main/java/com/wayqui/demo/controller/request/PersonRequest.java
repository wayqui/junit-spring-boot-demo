package com.wayqui.demo.controller.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PersonRequest {

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Integer age;
    private String email;
}
