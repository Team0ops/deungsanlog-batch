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
    @JoinColumn(name = "mountain_id")
    private Mountain mountain;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String fullDescription;

    @Column(columnDefinition = "TEXT")
    private String nearbyTourInfo;

    @Column(length = 50)
    private String difficulty;

    @Column(name = "hiking_point_info", columnDefinition = "TEXT")
    private String hikingPointInfo;

    @Column(name = "hiking_course_info", columnDefinition = "TEXT")
    private String hikingCourseInfo;

    @Column(name = "transport_info", columnDefinition = "TEXT")
    private String transportInfo;

}
