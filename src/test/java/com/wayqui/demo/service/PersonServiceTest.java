package com.wayqui.demo.service;

import com.wayqui.demo.dao.PersonRepository;
import com.wayqui.demo.dto.PersonDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    
    @InjectMocks
    PersonServiceImpl personService;

    @Mock
    PersonRepository repository;
    
    @Test
    public void getAllPersonsTests() {
        List<PersonDto> persons = personService.getAllPersons();
        assertNotNull(persons);
        assertEquals(0, persons.size());
    }

}