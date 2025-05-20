package com.deungsanlog.batch.domain.mountain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "mountain_descriptions")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class MountainDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn
    private Mountain mountain;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String fullDescription;

    @Column
    private LocalTime sunriseTime;

    @Column
    private LocalTime sunsetTime;

    @Column(columnDefinition = "TEXT")
    private String nearbyTourInfo;

    @Column(length = 50)
    private String difficulty;

}
