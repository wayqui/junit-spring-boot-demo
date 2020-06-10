package com.wayqui.demo.controller.unit;

import com.google.gson.Gson;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.wayqui.demo.utils.MockData.PERSONS_DTO_MOCKED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
@ExtendWith(SpringExtension.class)
@Slf4j
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private PersonService personService;

    private final Type listType = new TypeToken<ArrayList<PersonResponse>>(){}.getType();

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
        List<PersonResponse> personsResponse = gson.fromJson(result.getResponse().getContentAsString(),
                listType);

        assertEquals(PERSONS_DTO_MOCKED.size(), personsResponse.size());

        PERSONS_DTO_MOCKED.forEach(personDtoMocked -> {
            Optional<PersonResponse> personFound = personsResponse
                    .stream()
                    .filter(p -> p.getId().equals(personDtoMocked.getId()))
                    .findFirst();

            assertThat(personFound.isPresent()).isTrue();
            PersonResponse personResponse = personFound.get();

            this.assertDtoEqualsResponse(personDtoMocked, personResponse);
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
        PersonResponse personResponse = gson.fromJson(result.getResponse().getContentAsString(),
                PersonResponse.class);

        this.assertDtoEqualsResponse(personDto, personResponse);
    }

    @Test
    public void given_non_existent_id_when_get_element_then_returns_not_found() throws Exception {
        // Given
        String nonExistentId = UUID.randomUUID().toString();

        RequestBuilder request = MockMvcRequestBuilders
                .get("/persons/{id}", nonExistentId)
                .accept(MediaType.APPLICATION_JSON);

        // When
        PersonDto newPerson = new PersonDto();
        when(personService.getPerson(nonExistentId)).thenReturn(newPerson);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andReturn();

        // Then
        PersonResponse personResponse = gson.fromJson(result.getResponse().getContentAsString(),
                PersonResponse.class);

        this.assertDtoEqualsResponse(newPerson, personResponse);
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
                .content(gson.toJson(newPerson))
                .accept(MediaType.APPLICATION_JSON);

        // When
        PersonDto personDto = PersonMapper.INSTANCE.requestToDto(newPerson);
        when(personService.createPerson(personDto)).thenReturn(personDto);
        MvcResult result = mockMvc.perform(builder).andExpect(status().isCreated()).andReturn();

        // Then
        PersonResponse personResponse = gson.fromJson(result.getResponse().getContentAsString(),
                PersonResponse.class);

        this.assertDtoEqualsResponse(personDto, personResponse);
    }

    private void assertDtoEqualsResponse(PersonDto personDtoMocked, PersonResponse personResponse) {
        assertThat(personDtoMocked)
                .isNotNull()
                .returns(personResponse.getId(), from(PersonDto::getId))
                .returns(personResponse.getEmail(), from(PersonDto::getEmail))
                .returns(personResponse.getFirstName(), from(PersonDto::getFirstName))
                .returns(personResponse.getLastName(), from(PersonDto::getLastName))
                .returns(personResponse.getBirthDate(), from(PersonDto::getBirthDate))
                .returns(personResponse.getAge(), from(PersonDto::getAge));
    }
}