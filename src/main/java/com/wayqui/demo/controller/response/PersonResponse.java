package com.wayqui.demo.controller.response;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PersonResponse {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Integer age;
    private String email;
}
