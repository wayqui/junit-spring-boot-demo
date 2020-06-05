package com.wayqui.demo.service;

import com.wayqui.demo.dto.PersonDto;

import java.util.List;

public interface PersonService {

    List<PersonDto> getAllPersons();

    PersonDto getPerson(String id);
}
