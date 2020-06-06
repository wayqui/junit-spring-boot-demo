package com.wayqui.demo.dao;

import com.wayqui.demo.entity.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository repository;

    @Test
    public void testFindAll() {
        assertEquals(0, repository.findAll().size());
    }

    @Test
    public void testFindOne() {
        // Given
        final String id = UUID.randomUUID().toString();

        // When
        Optional<Person> result = repository.findById(id);

        // Then
        assertFalse(result.isPresent());
    }


    @Test
    public void testCreatePerson(){
        // Given
        String id = UUID.randomUUID().toString();
        Person person = Person.builder()
                .firstName("Jose")
                .birthDate(LocalDate.of(1970, 1, 1))
                .email("newuser@gmail.com")
                .id(id)
                .lastName("Loras")
                .build();

        // When
        Person newPerson = repository.save(person);

        // Then
        assertNotNull(newPerson);
        assertEquals(person, newPerson);
        assertEquals(1, repository.findAll().size());
        assertTrue(repository.findById(id).isPresent());
    }
}
