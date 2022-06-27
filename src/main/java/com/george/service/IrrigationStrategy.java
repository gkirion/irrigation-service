package com.george.service;

import com.george.model.IrrigationAction;

public class IrrigationStrategy {

    private Double minMoistureThreshold;
    private Double maxMoistureThreshold;

    /**
     * @param minMoistureThreshold The min moisture, any value below that will activate the irrigation.
     * @param maxMoistureThreshold The max moisture, any value above that will deactivate the irrigation.
     * @throws IllegalArgumentException If minMoistureThreshold is not less than maxMoistureThreshold.
    */
    public IrrigationStrategy(Double minMoistureThreshold, Double maxMoistureThreshold) {

        if (minMoistureThreshold >= maxMoistureThreshold) {
            throw new IllegalArgumentException("min moisture threshold must be less than max moisture threshold");
        }
        this.minMoistureThreshold = minMoistureThreshold;
        this.maxMoistureThreshold = maxMoistureThreshold;
    }

    public Double getMinMoistureThreshold() {
        return minMoistureThreshold;
    }

    public Double getMaxMoistureThreshold() {
        return maxMoistureThreshold;
    }

    public IrrigationAction evaluateAction(Double moisture) {

        if (moisture < minMoistureThreshold) {
            return IrrigationAction.START;

        } else if (moisture > maxMoistureThreshold) {
            return IrrigationAction.STOP;
        }
        return IrrigationAction.NOTHING;
    }

}
