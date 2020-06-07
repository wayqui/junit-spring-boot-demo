package com.wayqui.demo.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.Period;
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
    @Getter(AccessLevel.NONE)
    private Integer age;
    private String email;

    public Integer getAge() {
        if (this.getBirthDate() != null)
            this.age = Period.between(this.getBirthDate(), LocalDate.now()).getYears();
        return this.age;
    }

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

    @Override
    public String toString() {
        return "PersonDto{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }
}
