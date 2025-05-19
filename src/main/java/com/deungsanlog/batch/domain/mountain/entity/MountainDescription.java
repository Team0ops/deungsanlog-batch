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

    @Column
    private String summary;

    @Column
    private String fullDescription;

    @Column
    private LocalTime sunriseTime;

    @Column
    private LocalTime sunsetTime;

    @Column
    private String nearbyTourInfo;
}
