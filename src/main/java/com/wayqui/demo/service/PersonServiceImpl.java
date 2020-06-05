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
import java.util.Optional;

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

    @Override
    public PersonDto getPerson(String id) {
        Optional<Person> personFound = repository.findById(id);
        PersonDto personDto = new PersonDto();
        if (personFound.isPresent()) {
            personDto = PersonMapper.INSTANCE.entityToDto(personFound.get());
            personDto.setAge(Period.between(personDto.getBirthDate(), LocalDate.now()).getYears());
        }
        return personDto;
    }
}
