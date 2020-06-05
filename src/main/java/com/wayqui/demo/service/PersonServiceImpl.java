package com.wayqui.demo.service;

import com.wayqui.demo.dao.PersonRepository;
import com.wayqui.demo.dto.PersonDto;
import com.wayqui.demo.entity.Person;
import com.wayqui.demo.mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepository repository;

    @Override
    public List<PersonDto> getAllPersons() {
        List<Person> persons = repository.findAll();
        List<PersonDto> personsDto = PersonMapper.INSTANCE.entitiesToDtos(persons);
        personsDto.forEach(dto -> {
            int age = Period.between(dto.getBirthDate(), LocalDate.now()).getYears();
            dto.setAge(age);
        });
        return personsDto;
    }
}
