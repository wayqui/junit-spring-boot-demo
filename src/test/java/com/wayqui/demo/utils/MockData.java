package com.wayqui.demo.utils;

import com.wayqui.demo.dto.PersonDto;
import com.wayqui.demo.entity.Person;
import com.wayqui.demo.mapper.PersonMapper;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MockData {

    public static final List<Person> PERSONS_MOCKED = Arrays.asList(
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

    public static final List<PersonDto> PERSONS_DTO_MOCKED = PERSONS_MOCKED.stream()
            .map(p -> {
                PersonDto dto = PersonMapper.INSTANCE.entityToDto(p);
                dto.setAge(Period.between(p.getBirthDate(), LocalDate.now()).getYears());
                return dto;
            })
            .collect(Collectors.toList());
}
