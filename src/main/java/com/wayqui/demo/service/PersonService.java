package com.wayqui.demo.service;

import com.wayqui.demo.dto.PersonDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PersonService {

    List<PersonDto> getAllPersons();
}
