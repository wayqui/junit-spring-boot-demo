package com.wayqui.demo.mapper;

import com.wayqui.demo.controller.request.PersonRequest;
import com.wayqui.demo.controller.response.PersonResponse;
import com.wayqui.demo.dto.PersonDto;
import com.wayqui.demo.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper( PersonMapper.class );

    PersonDto requestToDto(PersonRequest person);
    List<PersonDto> requestsToDtos(List<PersonRequest> persons);

    PersonResponse dtoToResponse(PersonDto person);
    List<PersonResponse> dtosToResponses(List<PersonDto> persons);

    Person dtoToEntity(PersonDto person);
    List<Person> dtosToEntities(List<PersonDto> persons);

    PersonDto entityToDto(Person person);
    List<PersonDto> entitiesToDtos(List<Person> persons);


}
