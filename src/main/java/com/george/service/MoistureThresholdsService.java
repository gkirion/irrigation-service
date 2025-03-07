package com.george.service;

import com.george.exception.InvalidThresholdsException;
import com.george.model.MoistureThresholds;
import com.george.model.Place;
import com.george.repository.MoistureThresholdsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MoistureThresholdsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoistureThresholdsService.class);

    @Autowired
    private PlaceService placeService;

    @Autowired
    private MoistureThresholdsRepository moistureThresholdsRepository;

    public MoistureThresholds setMoistureThresholds(UUID id, MoistureThresholds updatedMoistureThresholds) {

        LOGGER.info("updating moisture thresholds of place: {} with value: {}", id, updatedMoistureThresholds);
        Place place = placeService.findById(id);
        MoistureThresholds moistureThresholds = moistureThresholdsRepository.findById(id).orElse(new MoistureThresholds());

        if (!isValidThresholds(updatedMoistureThresholds.getMinMoistureThreshold(), updatedMoistureThresholds.getMaxMoistureThreshold())) {
            throw new InvalidThresholdsException("min moisture threshold must be less than max moisture threshold");
        }

        moistureThresholds.setPlace(place);
        moistureThresholds.setMinMoistureThreshold(updatedMoistureThresholds.getMinMoistureThreshold());
        moistureThresholds.setMaxMoistureThreshold(updatedMoistureThresholds.getMaxMoistureThreshold());
        return moistureThresholdsRepository.save(moistureThresholds);
    }

    public Optional<MoistureThresholds> getMoistureThresholds(String name) {
        return moistureThresholdsRepository.findByPlaceName(name);
    }

    public Optional<MoistureThresholds> getMoistureThresholds(UUID id) {
        return moistureThresholdsRepository.findById(id);
    }

    /**
     * @param minMoistureThreshold the min moisture, any value below that will activate irrigation.
     * @param maxMoistureThreshold the max moisture, any value above that will deactivate irrigation.
     * @return false if minMoistureThreshold is not less than maxMoistureThreshold, true otherwise
     * @throws IllegalArgumentException if min or max threshold is null
     */
    private boolean isValidThresholds(Double minMoistureThreshold, Double maxMoistureThreshold) {

        if (minMoistureThreshold == null || maxMoistureThreshold == null) {
            throw new IllegalArgumentException("both min and max threshold must be specified");
        }
        if (minMoistureThreshold >= maxMoistureThreshold) {
            return false;
        }
        return true;
    }

}
