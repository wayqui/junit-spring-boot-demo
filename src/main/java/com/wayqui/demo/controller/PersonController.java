package com.wayqui.demo.controller;

import com.wayqui.demo.controller.response.PersonResponse;
import com.wayqui.demo.dto.PersonDto;
import com.wayqui.demo.mapper.PersonMapper;
import com.wayqui.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/persons")
    public List<PersonResponse> getAllPersons() {
        List<PersonDto> persons = personService.getAllPersons();
        return PersonMapper.INSTANCE.dtosToResponses(persons);
    }

    @GetMapping("/persons/{id}")
    public PersonResponse getPerson(@PathVariable String id) {
        return PersonMapper.INSTANCE.dtoToResponse(
                personService.getAllPersons().stream().findFirst().get());
    }

}

