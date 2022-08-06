package com.george.service;

import com.george.exception.InvalidThresholdsException;
import com.george.exception.PlaceNotFoundException;
import com.george.model.Place;
import com.george.repository.PlaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlaceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceService.class);

    @Autowired
    private PlaceRepository placeRepository;

    public Place create(Place place) throws IllegalArgumentException, InvalidThresholdsException {
        LOGGER.info("creating place {}", place);
        if (place.getName() == null || place.getName().isBlank()) {
            throw new IllegalArgumentException("place name can't be empty");
        }
        if (!isValidThresholds(place.getMinMoistureThreshold(), place.getMaxMoistureThreshold())) {
            throw new InvalidThresholdsException("min moisture threshold must be less than max moisture threshold");
        }
        return placeRepository.save(place);
    }

    public Place findById(UUID id) throws PlaceNotFoundException {
        LOGGER.info("find place with id: {}", id);
        return placeRepository.findById(id).orElseThrow(() -> new PlaceNotFoundException("place with id " + id + " does not exist"));
    }

    public Place findByName(String name) throws PlaceNotFoundException {
        LOGGER.info("find place with name: {}", name);
        return placeRepository.findByName(name).orElseThrow(() -> new PlaceNotFoundException("place " + name + " does not exist"));
    }

    public List<Place> findAll() {
        LOGGER.info("find all places");
        return placeRepository.findAll();
    }

    public Place update(UUID id, Place updatedPlace) throws PlaceNotFoundException, IllegalArgumentException, InvalidThresholdsException {
        LOGGER.info("update place with id: {}", id);
        Place place = placeRepository.findById(id).orElseThrow(() -> new PlaceNotFoundException("place with id " + id + " does not exist"));

        place.setName(updatedPlace.getName());
        place.setMinMoistureThreshold(updatedPlace.getMinMoistureThreshold());
        place.setMaxMoistureThreshold(updatedPlace.getMaxMoistureThreshold());

        if (place.getName() == null || place.getName().isBlank()) {
            throw new IllegalArgumentException("place name can't be empty");
        }
        if (!isValidThresholds(place.getMinMoistureThreshold(), place.getMaxMoistureThreshold())) {
            throw new InvalidThresholdsException("min moisture threshold must be less than max moisture threshold");
        }
        return placeRepository.save(place);
    }

    public Place updateName(UUID id, Place updatedPlace) throws PlaceNotFoundException, IllegalArgumentException {
        LOGGER.info("update place name with id: {}", id);
        Place place = placeRepository.findById(id).orElseThrow(() -> new PlaceNotFoundException("place with id " + id + " does not exist"));

        place.setName(updatedPlace.getName());
        if (place.getName() == null || place.getName().isBlank()) {
            throw new IllegalArgumentException("place name can't be empty");
        }
        return placeRepository.save(place);
    }

    public Place updateThresholds(UUID id, Place updatedPlace) throws PlaceNotFoundException, InvalidThresholdsException {
        LOGGER.info("update thresholds of place with id: {}", id);
        Place place = placeRepository.findById(id).orElseThrow(() -> new PlaceNotFoundException("place with id " + id + " does not exist"));

        place.setMinMoistureThreshold(updatedPlace.getMinMoistureThreshold());
        place.setMaxMoistureThreshold(updatedPlace.getMaxMoistureThreshold());

        if (!isValidThresholds(place.getMinMoistureThreshold(), place.getMaxMoistureThreshold())) {
            throw new InvalidThresholdsException("min moisture threshold must be less than max moisture threshold");
        }
        return placeRepository.save(place);
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
