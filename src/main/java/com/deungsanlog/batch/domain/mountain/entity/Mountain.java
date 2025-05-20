package com.deungsanlog.batch.domain.mountain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "mountains")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Mountain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100)
    private String location;

    private Integer elevation;

    @Column
    private BigDecimal latitude;

    @Column
    private BigDecimal longitude;

    @Column(length = 255)
    private String thumbnailImgUrl;

    @Column(length = 50)
    private String externalId;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "mountain", cascade = CascadeType.ALL, orphanRemoval = true)
    private MountainDescription description;

    @PrePersist
    public void setCreatedAtNow() {
        this.createdAt = LocalDateTime.now();
    }
}