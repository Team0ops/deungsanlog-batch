package com.deungsanlog.batch.domain.mountain.repository;

import com.deungsanlog.batch.domain.mountain.entity.MountainSunInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MountainSunInfoRepository extends JpaRepository<MountainSunInfo, Long> {
    @Modifying
    @Query(value = "TRUNCATE TABLE mountain_sun_info", nativeQuery = true)
    void truncateTable();
}