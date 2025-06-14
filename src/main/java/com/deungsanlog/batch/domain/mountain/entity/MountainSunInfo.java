package com.deungsanlog.batch.domain.mountain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "mountain_sun_info")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MountainSunInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mountain_id", nullable = false)
    private Mountain mountain;

    private LocalDate date;

    @Column(name = "sunrise_time")
    private LocalTime sunriseTime;

    @Column(name = "sunset_time")
    private LocalTime sunsetTime;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
