package com.deungsanlog.batch.domain.mountain.repository;

import com.deungsanlog.batch.domain.mountain.entity.Mountain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MountainRepository extends JpaRepository<Mountain, Long> {
}
