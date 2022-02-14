package com.example.nanuri.domain.lesson.participant;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Participant {

    @Id
    ParticipantId participantId;

    @Builder
    public Participant(ParticipantId participantId) {
        this.participantId = participantId;
    }
}
