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
        if (updatedPlace.getName() == null || updatedPlace.getName().isBlank()) {
            throw new IllegalArgumentException("place name can't be empty");
        }

        Place place = placeRepository.findById(id).orElseThrow(() -> new PlaceNotFoundException("place with id " + id + " does not exist"));
        place.setName(updatedPlace.getName());
        return placeRepository.save(place);
    }

}
