package com.wayqui.demo.service;

import com.wayqui.demo.dao.PersonRepository;
import com.wayqui.demo.dto.PersonDto;
import com.wayqui.demo.entity.Person;
import com.wayqui.demo.mapper.PersonMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.wayqui.demo.utils.MockData.PERSONS_MOCKED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    
    @InjectMocks
    PersonServiceImpl personService;

    @Mock
    PersonRepository repository;
    
    @Test
    public void getAllPersonsTests() {
        // Given
        when(repository.findAll()).thenReturn(PERSONS_MOCKED);

        // When
        List<PersonDto> personsFound = personService.getAllPersons();

        // Then
        assertThat(personsFound).isNotNull();
        assertThat(personsFound.size()).isEqualTo(PERSONS_MOCKED.size());

        PERSONS_MOCKED.forEach(personMocked -> {
            Optional<PersonDto> personFound = personsFound
                    .stream()
                    .filter(p -> p.getId().equals(personMocked.getId()))
                    .findFirst();

            assertThat(personFound.isPresent()).isTrue();
            PersonDto personDto = personFound.get();

            assertDtoEqualsToEntity(personDto, personMocked);
        });
    }

    @Test
    public void getSinglePersonWithExistentIdTest() {
        // Given
        Person aPerson = PERSONS_MOCKED.stream().findFirst().get();

        // When
        when(repository.findById(aPerson.getId())).thenReturn(Optional.of(aPerson));
        PersonDto personFound = personService.getPerson(aPerson.getId());
        
        // Then
        assertThat(personFound).isNotNull();
        assertThat(PersonMapper.INSTANCE.dtoToEntity(personFound)).isEqualTo(aPerson);
        assertThat(personFound.getAge())
                .isEqualTo(Period.between(aPerson.getBirthDate(), LocalDate.now())
                        .getYears());
    }

    @Test
    public void getSinglePersonWithNonExistentIdTest() {
        // Given
        String nonExistentId = UUID.randomUUID().toString();

        // When
        PersonDto nonExistentPerson = personService.getPerson(nonExistentId);

        // Then
        assertThat(nonExistentPerson).isEqualTo(new PersonDto());
    }

    @Test
    public void createPersonTest() {
        // Given
        PersonDto newPersonDto = PersonDto.builder()
                .birthDate(LocalDate.of(1981, 1, 1))
                .email("newuser@gmail.com")
                .firstName("Maria")
                .lastName("De Pedro")
                .build();

        // When
        Person newPerson = PersonMapper.INSTANCE.dtoToEntity(newPersonDto);
        newPerson.setId(UUID.randomUUID().toString());
        when(repository.save(any(Person.class))).thenReturn(newPerson);
        PersonDto createdPersonDto = personService.createPerson(newPersonDto);

        // Then
        assertDtoEqualsToEntity(createdPersonDto, newPerson);
    }

    @Test
    public void createPersonWithNoDataTest() {
        // Given
        PersonDto newEmptyPersonDto = PersonDto.builder()
                .build();

        // When
        Person newPerson = PersonMapper.INSTANCE.dtoToEntity(newEmptyPersonDto);
        newPerson.setId(UUID.randomUUID().toString());
        when(repository.save(any(Person.class))).thenReturn(newPerson);
        PersonDto createdPersonDto = personService.createPerson(newEmptyPersonDto);

        // Then
        assertDtoEqualsToEntity(createdPersonDto, newPerson);
    }

    private void assertDtoEqualsToEntity(PersonDto personDto, Person person) {
        assertThat(personDto)
                .isNotNull()
                .returns(person.getId(), from(PersonDto::getId))
                .returns(person.getEmail(), from(PersonDto::getEmail))
                .returns(person.getFirstName(), from(PersonDto::getFirstName))
                .returns(person.getLastName(), from(PersonDto::getLastName))
                .returns(person.getBirthDate(), from(PersonDto::getBirthDate));
    }
}