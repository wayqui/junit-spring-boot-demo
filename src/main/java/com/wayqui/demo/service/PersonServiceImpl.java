package com.wayqui.demo.service;

import com.wayqui.demo.dao.PersonRepository;
import com.wayqui.demo.dto.PersonDto;
import com.wayqui.demo.entity.Person;
import com.wayqui.demo.mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepository repository;

    @Override
    public List<PersonDto> getAllPersons() {
        List<Person> persons = repository.findAll();
        return PersonMapper.INSTANCE.entitiesToDtos(persons);
    }

    @Override
    public PersonDto getPerson(String id) {
        Optional<Person> personFound = repository.findById(id);
        PersonDto personDto = new PersonDto();
        if (personFound.isPresent()) {
            personDto = PersonMapper.INSTANCE.entityToDto(personFound.get());
        }
        return personDto;
    }

    @Override
    public PersonDto createPerson(PersonDto personDto) {
        personDto.setId(UUID.randomUUID().toString());

        Person person = PersonMapper.INSTANCE.dtoToEntity(personDto);
        Person personCreated = repository.save(person);

        return PersonMapper.INSTANCE.entityToDto(personCreated);
    }
}
