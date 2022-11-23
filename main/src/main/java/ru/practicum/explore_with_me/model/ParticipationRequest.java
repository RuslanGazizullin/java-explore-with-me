package ru.practicum.explore_with_me.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participation_requests")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "created")
    private LocalDateTime created;
    @Column(name = "event")
    private Long event;
    @Column(name = "requester")
    private Long requester;
    @Column(name = "status")
    private RequestStatus status;
}
