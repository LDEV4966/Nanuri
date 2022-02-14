package com.example.nanuri.domain.lesson.registration;

import lombok.Getter;

@Getter
public enum RegistrationStatus {

    DENIED("DENIED"),
    ACCEPTED("ACCEPTED"),
    WAITING("WAITING");

    private String status;

    RegistrationStatus(String status){
        this.status = status;
    }
}
