package com.wayqui.demo.controller;

import com.wayqui.demo.controller.response.PersonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;

@RestController
public class PersonController {

    private PersonResponse person = PersonResponse.builder()
            .age(20)
            .birthDate(LocalDate.now())
            .firstName("Jose")
            .lastName("Bustamante")
            .email("joshelito@gmail.com")
            .id(UUID.randomUUID().toString())
            .build();
    private List<PersonResponse> persons = Collections.singletonList(person);

    @GetMapping("/persons")
    public List<PersonResponse> getPersons() {
        return persons;
    }

}

