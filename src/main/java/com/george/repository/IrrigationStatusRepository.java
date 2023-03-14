package com.george.repository;

import com.george.model.IrrigationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IrrigationStatusRepository extends JpaRepository<IrrigationStatus, UUID> {

    Optional<IrrigationStatus> findByPlaceName(String placeName);

}
