package com.george.web;

import com.george.model.IrrigationStatus;
import com.george.service.IrrigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/places/{name}/irrigation", produces = MediaType.APPLICATION_JSON_VALUE)
public class IrrigationServiceEndpoint {

    @Autowired
    private IrrigationService irrigationService;

    @GetMapping(value = "/status")
    public IrrigationStatus getIrrigationStatus(@PathVariable String name) {
        return irrigationService.getIrrigationStatus(name);
    }

    @PostMapping(value = "/start")
    public void startIrrigation(@PathVariable String name) throws IOException {
        irrigationService.startIrrigation(name);
    }

    @PostMapping(value = "/stop")
    public void stopIrrigation(@PathVariable String name) throws IOException {
        irrigationService.stopIrrigation(name);
    }

}
