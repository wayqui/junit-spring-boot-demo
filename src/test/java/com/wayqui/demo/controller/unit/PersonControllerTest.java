package com.wayqui.demo.controller.unit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.wayqui.demo.controller.PersonController;
import com.wayqui.demo.controller.request.PersonRequest;
import com.wayqui.demo.controller.response.PersonResponse;
import com.wayqui.demo.dto.PersonDto;
import com.wayqui.demo.mapper.PersonMapper;
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
import java.time.Period;
import java.util.*;

import static com.wayqui.demo.utils.MockData.PERSONS_DTO_MOCKED;
import static com.wayqui.demo.utils.MockData.PERSONS_MOCKED;
import static org.junit.jupiter.api.Assertions.*;
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

    private final Type listType = new TypeToken<ArrayList<PersonResponse>>() {
    }.getType();

    @Test
    public void given_mock_data_when_get_all_persons_then_returns_ok() throws Exception {
        // Given
        RequestBuilder request = MockMvcRequestBuilders
                .get("/persons")
                .accept(MediaType.APPLICATION_JSON);

        // When
        when(personService.getAllPersons()).thenReturn(PERSONS_DTO_MOCKED);

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<PersonResponse> personsResponse = new GsonBuilder()
                .create()
                .fromJson(result.getResponse().getContentAsString(), listType);

        assertEquals(PERSONS_DTO_MOCKED.size(), personsResponse.size());

        PERSONS_MOCKED.forEach(personMocked -> {
            Optional<PersonResponse> personFound = personsResponse
                    .stream()
                    .filter(p -> p.getId().equals(personMocked.getId()))
                    .findFirst();
            assertTrue(personFound.isPresent());
            PersonResponse personResponse = personFound.get();
            assertEquals(personMocked.getBirthDate(), personResponse.getBirthDate());
            assertEquals(personMocked.getId(), personResponse.getId());
            assertEquals(personMocked.getEmail(), personResponse.getEmail());
            assertEquals(personMocked.getFirstName(), personResponse.getFirstName());
            assertEquals(personMocked.getLastName(), personResponse.getLastName());
            int expectedAge = Period.between(personMocked.getBirthDate(), LocalDate.now())
                    .getYears();
            assertEquals(expectedAge, personResponse.getAge());
        });
    }

    @Test
    public void given_existing_id_when_get_element_then_returns_ok() throws Exception {
        // Given
        PersonDto personDto = PERSONS_DTO_MOCKED.stream().findFirst().get();
        RequestBuilder request = MockMvcRequestBuilders
                .get("/persons/{id}", personDto.getId())
                .accept(MediaType.APPLICATION_JSON);

        // When
        when(personService.getPerson(personDto.getId())).thenReturn(personDto);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        // Then
        PersonResponse personResponse = new GsonBuilder().create()
                .fromJson(result.getResponse().getContentAsString(), PersonResponse.class);

        assertNotNull(personResponse);
        assertEquals(PersonMapper.INSTANCE.dtoToResponse(personDto), personResponse);
    }

    @Test
    public void given_non_existent_id_when_get_element_then_returns_not_found() throws Exception {
        // Given
        String nonExistentId = UUID.randomUUID().toString();

        RequestBuilder request = MockMvcRequestBuilders
                .get("/persons/{id}", nonExistentId)
                .accept(MediaType.APPLICATION_JSON);

        // When
        when(personService.getPerson(nonExistentId)).thenReturn(new PersonDto());
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andReturn();

        // Then
        PersonResponse personResponse = new GsonBuilder().create()
                .fromJson(result.getResponse().getContentAsString(), PersonResponse.class);
        assertNotNull(personResponse);
        assertNull(personResponse.getId());
        assertNull(personResponse.getFirstName());
        assertNull(personResponse.getLastName());
        assertNull(personResponse.getEmail());
        assertNull(personResponse.getAge());
    }

    @Test
    public void given_a_new_person_when_post_element_then_returns_created() throws Exception {
        // Given
        PersonRequest newPerson = PersonRequest.builder()
                .birthDate(LocalDate.of(1981, 1, 1))
                .email("newperson@gmail.com")
                .firstName("Alex")
                .lastName("Supertramp")
                .build();

        RequestBuilder builder = MockMvcRequestBuilders
                .post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(newPerson))
                .accept(MediaType.APPLICATION_JSON);

        // When
        MvcResult result = mockMvc.perform(builder).andExpect(status().isCreated()).andReturn();

        // Then
        PersonResponse personResponse = new GsonBuilder().create()
                .fromJson(result.getResponse().getContentAsString(), PersonResponse.class);
        assertNotNull(personResponse);
        assertEquals(newPerson.getBirthDate(), personResponse.getBirthDate());
        assertEquals(newPerson.getEmail(), personResponse.getEmail());
        assertEquals(newPerson.getFirstName(), personResponse.getFirstName());
        assertEquals(newPerson.getLastName(), personResponse.getLastName());
    }
}