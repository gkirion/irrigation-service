package com.george.web;

import com.george.model.Place;
import com.george.repository.PlaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/place", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlaceEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceEndpoint.class);

    @Autowired
    private PlaceRepository placeRepository;

    @GetMapping
    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Place getPlace(@PathVariable UUID id) throws Exception {
        return placeRepository.findById(id).orElseThrow(() -> new Exception("place with id " + id + " does not exist"));
    }

    @PostMapping
    public Place createPlace(@RequestBody Place place) {
        return placeRepository.save(place);
    }

}
