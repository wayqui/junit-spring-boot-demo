package com.wayqui.demo.service;

import com.wayqui.demo.dao.PersonDao;
import com.wayqui.demo.dto.PersonDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    
    @InjectMocks
    PersonServiceImpl personService;

    @Mock
    PersonDao personDao;
    
    @Test
    public void getAllPersonsTests() {
        List<PersonDto> persons = personService.getAllPersons();
        assertNotNull(persons);
        assertEquals(0, persons.size());
    }

}