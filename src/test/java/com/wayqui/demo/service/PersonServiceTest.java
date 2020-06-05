package com.wayqui.demo.service;

import com.wayqui.demo.dao.PersonRepository;
import com.wayqui.demo.dto.PersonDto;
import com.wayqui.demo.entity.Person;
import com.wayqui.demo.mapper.PersonMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.wayqui.demo.utils.MockData.PERSONS_MOCKED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    
    @InjectMocks
    PersonServiceImpl personService;

    @Mock
    PersonRepository repository;
    
    @Test
    public void getAllPersonsTests() {
        // Given

        when(repository.findAll()).thenReturn(PERSONS_MOCKED);

        // When
        List<PersonDto> personsFound = personService.getAllPersons();

        // Then
        assertNotNull(personsFound);
        assertEquals(PERSONS_MOCKED.size(), personsFound.size());

        PERSONS_MOCKED.forEach(personMocked -> {
            Optional<PersonDto> personFound = personsFound
                    .stream()
                    .filter(p -> p.getId().equals(personMocked.getId()))
                    .findFirst();
            assertTrue(personFound.isPresent());
            PersonDto personDto = personFound.get();
            assertEquals(personMocked.getBirthDate(), personDto.getBirthDate());
            assertEquals(personMocked.getId(), personDto.getId());
            assertEquals(personMocked.getEmail(), personDto.getEmail());
            assertEquals(personMocked.getFirstName(), personDto.getFirstName());
            assertEquals(personMocked.getLastName(), personDto.getLastName());
            int expectedAge = Period.between(personMocked.getBirthDate(), LocalDate.now())
                    .getYears();
            assertEquals(expectedAge, personDto.getAge());
        });
    }

    @Test
    public void getSinglePersonWithExistentIdTest() {
        // Given
        Person aPerson = PERSONS_MOCKED.stream().findFirst().get();

        // When
        when(repository.findById(aPerson.getId())).thenReturn(Optional.of(aPerson));
        PersonDto personFound = personService.getPerson(aPerson.getId());
        
        // Then
        assertNotNull(personFound);
        int expectedAge = Period.between(aPerson.getBirthDate(), LocalDate.now())
                .getYears();
        assertEquals(expectedAge, personFound.getAge());
        assertEquals(aPerson, PersonMapper.INSTANCE.dtoToEntity(personFound));
    }

    @Test
    public void getSinglePersonWithNonExistentIdTest() {
        // Given
        String nonExistentId = UUID.randomUUID().toString();

        // When
        PersonDto nonExistentPerson = personService.getPerson(nonExistentId);

        // Then
        assertNotNull(nonExistentPerson);
        assertNull(nonExistentPerson.getId());
        assertNull(nonExistentPerson.getFirstName());
        assertNull(nonExistentPerson.getLastName());
        assertNull(nonExistentPerson.getEmail());
        assertNull(nonExistentPerson.getAge());
    }
    

}