package ru.practicum.explore_with_me.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "locations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "lat")
    private Float lat;
    @Column(name = "lon")
    private Float lon;
}
