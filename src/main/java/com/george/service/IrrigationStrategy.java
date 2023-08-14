package com.george.service;

import com.george.exception.ThresholdsNotFoundException;
import com.george.model.Action;
import com.george.model.MoistureThresholds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IrrigationStrategy {

    @Autowired
    private MoistureThresholdsService moistureThresholdsService;

    public Action evaluateAction(String placeName, Double moisture) throws ThresholdsNotFoundException {

        MoistureThresholds moistureThresholds = moistureThresholdsService.getMoistureThresholds(placeName).orElseThrow(() -> new ThresholdsNotFoundException("No moisture thresholds found for place: " + placeName));
        Double minMoistureThreshold = moistureThresholds.getMinMoistureThreshold();
        Double maxMoistureThreshold = moistureThresholds.getMaxMoistureThreshold();

        if (moisture < minMoistureThreshold) {
            return Action.START;

        } else if (moisture > maxMoistureThreshold) {
            return Action.STOP;
        }
        return Action.NOTHING;
    }

}
