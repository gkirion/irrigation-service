package com.george.web;

import com.george.exception.InvalidThresholdsException;
import com.george.exception.PlaceNotFoundException;
import com.george.model.Place;
import com.george.service.PlaceService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/places", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlaceEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceEndpoint.class);

    @Autowired
    private PlaceService placeService;

    @GetMapping
    @Timed(value = "places.all", percentiles = {0.05, 0.95, 1.00})
    public List<Place> getAllPlaces() {
        return placeService.findAll();
    }

    @GetMapping("/{id}")
    @Timed(value = "places.id", percentiles = {0.05, 0.95, 1.00})
    public Place getPlace(@PathVariable UUID id) throws PlaceNotFoundException {
        return placeService.findById(id);
    }

    @PostMapping
    @Timed(value = "places.create", percentiles = {0.05, 0.95, 1.00})
    public Place createPlace(@RequestBody Place place) throws InvalidThresholdsException {
        return placeService.create(place);
    }

    @PutMapping("/{id}")
    @Timed(value = "places.update", percentiles = {0.05, 0.95, 1.00})
    public Place update(@PathVariable UUID id, @RequestBody Place place) throws PlaceNotFoundException, InvalidThresholdsException {
        return placeService.update(id, place);
    }

}
