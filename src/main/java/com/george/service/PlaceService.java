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

    public Place create(Place place) throws InvalidThresholdsException {
        LOGGER.info("creating place {}", place);
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

    public Place update(UUID id, Place updatedPlace) throws PlaceNotFoundException, InvalidThresholdsException {
        LOGGER.info("update place with id: {}", id);
        Place place = placeRepository.findById(id).orElseThrow(() -> new PlaceNotFoundException("place with id " + id + " does not exist"));

        place.setName(updatedPlace.getName());
        place.setMinMoistureThreshold(updatedPlace.getMinMoistureThreshold());
        place.setMaxMoistureThreshold(updatedPlace.getMaxMoistureThreshold());

        if (!isValidThresholds(place.getMinMoistureThreshold(), place.getMaxMoistureThreshold())) {
            throw new InvalidThresholdsException("min moisture threshold must be less than max moisture threshold");
        }
        return placeRepository.save(place);
    }

    public Place updateName(UUID id, Place updatedPlace) throws PlaceNotFoundException {
        LOGGER.info("update place name with id: {}", id);
        Place place = placeRepository.findById(id).orElseThrow(() -> new PlaceNotFoundException("place with id " + id + " does not exist"));

        place.setName(updatedPlace.getName());
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
     * @param minMoistureThreshold The min moisture, any value below that will activate the irrigation.
     * @param maxMoistureThreshold The max moisture, any value above that will deactivate the irrigation.
     * @return false If minMoistureThreshold is not less than maxMoistureThreshold, true otherwise
     */
    private boolean isValidThresholds(Double minMoistureThreshold, Double maxMoistureThreshold) {
        if (minMoistureThreshold >= maxMoistureThreshold) {
            return false;
        }
        return true;
    }

}
