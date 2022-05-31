package com.example.project;

import java.time.LocalDate;

public class Person {
    private String firstName;

    private String lastName;

    private Gender gender;

    private LocalDate birthday;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public Person(String firstName, String lastName, Gender gender, LocalDate birthday){
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public LocalDate getGender() {
        return this.birthday;
    }

    public LocalDate getDateOfBirth() {
        return this.birthday;
    }
}
