package com.wayqui.demo.controller.integration;

import com.wayqui.demo.controller.request.PersonRequest;
import com.wayqui.demo.controller.response.PersonResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Slf4j
public class PersonControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void getAllPersonsTest() {
        // Given
        int numberOfRecordsInTestData = 3;

        // When
        ResponseEntity<PersonResponse[]> response = restTemplate
                .getForEntity("/persons", PersonResponse[].class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(numberOfRecordsInTestData, response.getBody().length);
    }

    @Test
    public void getANonExistentPersonTest() {
        // Given
        String idNonExistentPerson = UUID.randomUUID().toString();

        // When
        ResponseEntity<PersonResponse> response = restTemplate
                .getForEntity("/persons/"+idNonExistentPerson, PersonResponse.class);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getId());
        assertNull(response.getBody().getAge());
        assertNull(response.getBody().getLastName());
        assertNull(response.getBody().getFirstName());
        assertNull(response.getBody().getEmail());
        assertNull(response.getBody().getBirthDate());
    }

    @Test
    public void getAExistentPersonTest() {
        // Given
        String existentId = "5eff7a57-f5df-409b-8ba5-70644fb34ece";

        // When
        ResponseEntity<PersonResponse> response = restTemplate
                .getForEntity("/persons/" + existentId, PersonResponse.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(existentId, response.getBody().getId());
        assertNotNull(response.getBody().getAge());
        assertNotNull(response.getBody().getLastName());
        assertNotNull(response.getBody().getFirstName());
        assertNotNull(response.getBody().getEmail());
        assertNotNull(response.getBody().getBirthDate());
    }

    @Test
    public void createANewPersonTest() {
        // Given
        PersonRequest newPerson = PersonRequest.builder()
                .birthDate(LocalDate.of(1981, 1, 1))
                .email("newuser@testing.comm")
                .firstName("Gabriel")
                .lastName("García Márquez")
                .build();

        // When
        ResponseEntity<PersonResponse> response = restTemplate
                .postForEntity("/persons", newPerson, PersonResponse.class);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        PersonResponse personCreated = response.getBody();

        assertNotNull(personCreated.getId());
        assertEquals(newPerson.getLastName(), personCreated.getLastName());
        assertEquals(newPerson.getFirstName(), personCreated.getFirstName());
        assertEquals(newPerson.getEmail(), personCreated.getEmail());
        assertEquals(newPerson.getBirthDate(), personCreated.getBirthDate());

        int expectedAge = Period.between(newPerson.getBirthDate(), LocalDate.now())
                .getYears();
        assertEquals(expectedAge, personCreated.getAge());
    }

    @Test
    public void createAnEmptyPersonTest() {
        // Given
        PersonRequest newPerson = PersonRequest.builder()
                .build();

        // When
        ResponseEntity<PersonResponse> response = restTemplate
                .postForEntity("/persons", newPerson, PersonResponse.class);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        PersonResponse personCreated = response.getBody();

        assertNotNull(personCreated.getId());
        assertNull(personCreated.getLastName());
        assertNull(personCreated.getFirstName());
        assertNull(personCreated.getEmail());
        assertNull(personCreated.getBirthDate());
        assertNull(personCreated.getAge());
    }

}
