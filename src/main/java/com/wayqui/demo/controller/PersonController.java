package com.wayqui.demo.controller;

import com.wayqui.demo.controller.response.PersonResponse;
import com.wayqui.demo.dto.PersonDto;
import com.wayqui.demo.mapper.PersonMapper;
import com.wayqui.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/persons")
    public ResponseEntity<List<PersonResponse>> getAllPersons() {
        List<PersonDto> persons = personService.getAllPersons();

        final List<PersonResponse> personResponses =
                PersonMapper.INSTANCE.dtosToResponses(persons);

        return new ResponseEntity<>(personResponses, HttpStatus.OK);
    }

    @GetMapping("/persons/{id}")
    public ResponseEntity<PersonResponse> getPerson(@PathVariable String id) {
        PersonDto personDto = personService.getPerson(id);

        final PersonResponse personResponse =
                PersonMapper.INSTANCE.dtoToResponse(personDto);

        if (personResponse.getId() == null) {
            return new ResponseEntity<>(personResponse, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(personResponse, HttpStatus.OK);
    }

}

