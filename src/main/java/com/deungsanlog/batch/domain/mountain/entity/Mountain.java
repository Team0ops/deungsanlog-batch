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

    @Column(nullable = false)
    private String name;

    @Column
    private String location;

    private Integer elevation;

    @Column
    private BigDecimal latitude;

    @Column
    private BigDecimal longitude;

    @Column
    private String thumbnailImgUrl;

    @Column
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