package com.george.web;

import com.george.exception.InvalidThresholdsException;
import com.george.exception.PlaceNotFoundException;
import com.george.model.Place;
import com.george.service.PlaceService;
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
    private PlaceService placeService;

    @GetMapping
    public List<Place> getAllPlaces() {
        return placeService.findAll();
    }

    @GetMapping("/{id}")
    public Place getPlace(@PathVariable UUID id) throws PlaceNotFoundException {
        return placeService.findById(id);
    }

    @PostMapping
    public Place createPlace(@RequestBody Place place) throws InvalidThresholdsException {
        return placeService.create(place);
    }

    @PutMapping("/{id}")
    public Place update(@PathVariable UUID id, @RequestBody Place place) throws PlaceNotFoundException, InvalidThresholdsException {
        return placeService.update(id, place);
    }

    @PatchMapping("/{id}/name")
    public Place updateName(@PathVariable UUID id, @RequestBody Place place) throws PlaceNotFoundException {
        return placeService.updateName(id, place);
    }

    @PatchMapping("/{id}/thresholds")
    public Place updateThresholds(@PathVariable UUID id, @RequestBody Place place) throws PlaceNotFoundException, InvalidThresholdsException {
        return placeService.updateThresholds(id, place);
    }

}
