package com.wayqui.demo.service;

import com.wayqui.demo.dao.PersonRepository;
import com.wayqui.demo.dto.PersonDto;
import com.wayqui.demo.entity.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        List<Person> personsMocked = Arrays.asList(
                Person.builder()
                        .id(UUID.randomUUID().toString())
                        .firstName("José")
                        .lastName("Bustamante")
                        .birthDate(LocalDate.of(1980, 1, 1))
                        .email("joselo@gmail.com").firstName("jose")
                        .build(),
                Person.builder()
                        .id(UUID.randomUUID().toString())
                        .firstName("Alex")
                        .lastName("Bustamante")
                        .birthDate(LocalDate.of(1970, 2, 2))
                        .email("alex@gmail.com").firstName("jose")
                        .build(),
                Person.builder()
                        .id(UUID.randomUUID().toString())
                        .firstName("Adaí")
                        .lastName("Bustamante")
                        .birthDate(LocalDate.of(1960, 3, 3))
                        .email("adai@gmail.com").firstName("jose")
                        .build()
        );
        when(repository.findAll()).thenReturn(personsMocked);

        // When
        List<PersonDto> personsFound = personService.getAllPersons();

        // Then
        assertNotNull(personsFound);
        assertEquals(personsMocked.size(), personsFound.size());

        personsMocked.forEach(personMocked -> {
            Optional<PersonDto> personFound = personsFound
                    .stream()
                    .filter(p -> p.getId().equals(personMocked.getId()))
                    .findFirst();
            assertTrue(personFound.isPresent());
            int expectedAge = Period.between(personMocked.getBirthDate(), LocalDate.now())
                    .getYears();
            assertEquals(expectedAge, personFound.get().getAge());
        });
    }

}