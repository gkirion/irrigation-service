package com.george.service;

import com.george.exception.PlaceNotFoundException;
import com.george.model.IrrigationStatus;
import com.george.model.Place;
import com.george.model.Status;
import com.george.repository.IrrigationStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class IrrigationStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IrrigationStatusService.class);

    @Autowired
    private IrrigationStatusRepository irrigationStatusRepository;

    @Autowired
    private PlaceService placeService;

    /**
     * @param placeName the name of the place
     * @param status the irrigation status of the place
     * @return Place the updated place
     * @throws PlaceNotFoundException if place with this name does not exist
     */
    @Transactional
    public IrrigationStatus updateIrrigationStatus(String placeName, Status status) {
        LOGGER.info("updating irrigation status of place: {} with value: {}", placeName, status);
        Place place = placeService.findByName(placeName);
        IrrigationStatus irrigationStatus = irrigationStatusRepository.findByPlaceName(placeName).orElse(new IrrigationStatus());

        irrigationStatus.setPlace(place);
        irrigationStatus.setStatus(status);
        return irrigationStatusRepository.save(irrigationStatus);
    }

    public Optional<IrrigationStatus> getIrrigationStatus(UUID id) {
        return irrigationStatusRepository.findById(id);
    }

    public Optional<IrrigationStatus> getIrrigationStatus(String placeName) {
        return irrigationStatusRepository.findByPlaceName(placeName);
    }

}
