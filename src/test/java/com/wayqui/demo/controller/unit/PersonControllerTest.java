package com.wayqui.demo.controller.unit;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.wayqui.demo.controller.PersonController;
import com.wayqui.demo.controller.response.PersonResponse;
import com.wayqui.demo.dto.PersonDto;
import com.wayqui.demo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
@ExtendWith(SpringExtension.class)
@Slf4j
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    private final Type listType = new TypeToken<ArrayList<PersonResponse>>(){}.getType();

    private PersonDto person = PersonDto.builder()
            .age(20)
            .id(UUID.randomUUID().toString())
            .birthDate(LocalDate.now())
            .firstName("Jose")
            .lastName("Bustamante")
            .email("joshelito@gmail.com")
            .id(UUID.randomUUID().toString())
            .build();
    private List<PersonDto> persons = Collections.singletonList(person);

    @Test
    void getOnePersonCorrectlyTest() throws Exception {

        // Given
        RequestBuilder request = MockMvcRequestBuilders
                .get("/persons")
                .accept(MediaType.APPLICATION_JSON);

        // When
        when(personService.getAllPersons()).thenReturn(persons);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<PersonResponse> personsResponse = new GsonBuilder().create().fromJson(result.getResponse().getContentAsString(), listType);
        assertEquals(persons.size(), personsResponse.size());
        personsResponse.forEach(personResponse -> {
            assertEquals(person.getAge(), personResponse.getAge());
            assertEquals(person.getBirthDate(), personResponse.getBirthDate());
            assertEquals(person.getEmail(), personResponse.getEmail());
            assertEquals(person.getFirstName(), personResponse.getFirstName());
            assertEquals(person.getLastName(), personResponse.getLastName());
            assertEquals(person.getId(), personResponse.getId());
        });
    }
}