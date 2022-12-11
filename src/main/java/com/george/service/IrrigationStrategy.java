package com.george.service;

import com.george.exception.PlaceNotFoundException;
import com.george.model.Action;
import com.george.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IrrigationStrategy {

    @Autowired
    private PlaceService placeService;

    public Action evaluateAction(String placeName, Double moisture) throws PlaceNotFoundException {

        Place place = placeService.findByName(placeName);
        Double minMoistureThreshold = place.getMinMoistureThreshold();
        Double maxMoistureThreshold = place.getMaxMoistureThreshold();

        if (moisture < minMoistureThreshold) {
            return Action.START;

        } else if (moisture > maxMoistureThreshold) {
            return Action.STOP;
        }
        return Action.NOTHING;
    }

}
