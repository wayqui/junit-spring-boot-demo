package com.wayqui.demo.controller.unit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.wayqui.demo.controller.PersonController;
import com.wayqui.demo.controller.response.PersonResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
@ExtendWith(SpringExtension.class)
@Slf4j
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                    LocalDate.class,
                    (JsonDeserializer<LocalDate>) (
                            json,
                            type,
                            jsonDeserializationContext) -> LocalDate.parse(json.getAsJsonPrimitive().getAsString()))
            .create();

    @Test
    void getPersons() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .get("/persons")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"firstName\":\"Jose\",\"lastName\":\"Bustamante\",\"birthDate\":\"2020-06-04\",\"age\":20,\"email\":\"joshelito@gmail.com\"}]"))
                .andReturn();

        Type listType = new TypeToken<ArrayList<PersonResponse>>(){}.getType();

        List<PersonResponse> persons = gson.fromJson(result.getResponse().getContentAsString(), listType);
        persons.forEach(person -> log.info(person.toString()));
    }
}