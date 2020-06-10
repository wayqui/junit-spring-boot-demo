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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

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
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(numberOfRecordsInTestData);
    }

    @Test
    public void getANonExistentPersonTest() {
        // Given
        String idNonExistentPerson = UUID.randomUUID().toString();

        // When
        ResponseEntity<PersonResponse> response = restTemplate
                .getForEntity("/persons/"+idNonExistentPerson, PersonResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .isNotNull()
                .returns(null, from(PersonResponse::getId))
                .returns(null, from(PersonResponse::getEmail))
                .returns(null, from(PersonResponse::getFirstName))
                .returns(null, from(PersonResponse::getLastName))
                .returns(null, from(PersonResponse::getBirthDate))
                .returns(null, from(PersonResponse::getAge));
    }

    @Test
    public void getAExistentPersonTest() {
        // Given
        String existentId = "5eff7a57-f5df-409b-8ba5-70644fb34ece";
        String existentEmail = "alex@supertramp.com";
        String existentFirstName = "Alex";
        String existentLastName = "Supertramp";

        // When
        ResponseEntity<PersonResponse> response = restTemplate
                .getForEntity("/persons/" + existentId, PersonResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .isNotNull()
                .returns(existentId, from(PersonResponse::getId))
                .returns(existentEmail, from(PersonResponse::getEmail))
                .returns(existentFirstName, from(PersonResponse::getFirstName))
                .returns(existentLastName, from(PersonResponse::getLastName));
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

        int calculatedAge = Period.between(newPerson.getBirthDate(), LocalDate.now()).getYears();

        // When
        ResponseEntity<PersonResponse> response = restTemplate
                .postForEntity("/persons", newPerson, PersonResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();

        PersonResponse personCreated = response.getBody();

        assertThat(personCreated)
                .isNotNull()
                .returns(newPerson.getEmail(), from(PersonResponse::getEmail))
                .returns(newPerson.getFirstName(), from(PersonResponse::getFirstName))
                .returns(newPerson.getLastName(), from(PersonResponse::getLastName))
                .returns(newPerson.getBirthDate(), from(PersonResponse::getBirthDate))
                .returns(calculatedAge, from(PersonResponse::getAge));
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

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();

        PersonResponse personCreated = response.getBody();

        assertThat(personCreated)
                .isNotNull()
                .returns(null, from(PersonResponse::getEmail))
                .returns(null, from(PersonResponse::getFirstName))
                .returns(null, from(PersonResponse::getLastName))
                .returns(null, from(PersonResponse::getBirthDate))
                .returns(null, from(PersonResponse::getAge));
    }

}
