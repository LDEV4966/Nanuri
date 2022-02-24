package com.example.nanuri.domain.lesson.registration;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter
@NoArgsConstructor
@Entity
public class Registration {

    @EmbeddedId
    private RegistrationId registrationId;

    private String registrationForm;

    @Builder
    public Registration(RegistrationId registrationId,String registrationForm) {
        this.registrationId = registrationId;
        this.registrationForm = registrationForm;
    }

}
