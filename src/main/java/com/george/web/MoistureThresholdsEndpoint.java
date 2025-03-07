package com.george.web;

import com.george.model.MoistureThresholds;
import com.george.service.MoistureThresholdsService;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/places/{id}/thresholds", produces = MediaType.APPLICATION_JSON_VALUE)
public class MoistureThresholdsEndpoint {

    @Autowired
    private MoistureThresholdsService moistureThresholdsService;

    @PutMapping
    @Timed(value = "thresholds.update", percentiles = {0.05, 0.95, 1.00})
    public MoistureThresholds setMoistureThresholds(@PathVariable UUID id, @RequestBody MoistureThresholds moistureThresholds) {
        return moistureThresholdsService.setMoistureThresholds(id, moistureThresholds);
    }

    @GetMapping
    @Timed(value = "thresholds.get", percentiles = {0.05, 0.95, 1.00})
    public Optional<MoistureThresholds> getMoistureThresholds(@PathVariable UUID id) {
        return moistureThresholdsService.getMoistureThresholds(id);
    }

}
