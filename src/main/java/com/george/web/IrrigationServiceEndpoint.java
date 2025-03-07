package com.george.web;

import com.george.model.Status;
import com.george.service.IrrigationService;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/places/{id}/irrigation", produces = MediaType.APPLICATION_JSON_VALUE)
public class IrrigationServiceEndpoint {

    @Autowired
    private IrrigationService irrigationService;

    @GetMapping
    @Timed(value = "irrigation.get", percentiles = {0.05, 0.95, 1.00})
    public Optional<Status> getIrrigationStatus(@PathVariable UUID id) {
        return irrigationService.getIrrigationStatus(id);
    }

    @PutMapping
    @Timed(value = "irrigation.update", percentiles = {0.05, 0.95, 1.00})
    public void setIrrigationStatus(@PathVariable UUID id, @RequestBody Status irrigationStatus) throws IOException {
        irrigationService.setIrrigationStatus(id, irrigationStatus);
    }

}
