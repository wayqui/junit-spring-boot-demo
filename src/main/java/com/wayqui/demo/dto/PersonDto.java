package com.wayqui.demo.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PersonDto {

    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Integer age;
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDto personDto = (PersonDto) o;
        return Objects.equals(id, personDto.id) &&
                Objects.equals(firstName, personDto.firstName) &&
                Objects.equals(lastName, personDto.lastName) &&
                Objects.equals(birthDate, personDto.birthDate) &&
                Objects.equals(age, personDto.age) &&
                Objects.equals(email, personDto.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birthDate, age, email);
    }
}
