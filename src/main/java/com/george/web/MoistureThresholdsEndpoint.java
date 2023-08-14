package com.george.web;

import com.george.model.MoistureThresholds;
import com.george.service.MoistureThresholdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/places/{name}/thresholds", produces = MediaType.APPLICATION_JSON_VALUE)
public class MoistureThresholdsEndpoint {

    @Autowired
    private MoistureThresholdsService moistureThresholdsService;

    @PutMapping
    public MoistureThresholds setMoistureThresholds(@PathVariable String name, @RequestBody MoistureThresholds moistureThresholds) {
        return moistureThresholdsService.setMoistureThresholds(name, moistureThresholds);
    }

    @GetMapping
    public Optional<MoistureThresholds> getMoistureThresholds(@PathVariable String name) {
        return moistureThresholdsService.getMoistureThresholds(name);
    }

}
