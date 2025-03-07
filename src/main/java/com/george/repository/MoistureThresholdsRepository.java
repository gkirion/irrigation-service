package com.george.repository;

import com.george.model.MoistureThresholds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MoistureThresholdsRepository extends JpaRepository<MoistureThresholds, UUID> {

    Optional<MoistureThresholds> findByPlaceName(String name);

    Optional<MoistureThresholds> findByPlaceId(UUID id);

}
