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

    @GetMapping
    public IrrigationStatus getIrrigationStatus(@PathVariable String name) {
        return irrigationService.getIrrigationStatus(name);
    }

    @PutMapping
    public void setIrrigationStatus(@PathVariable String name, @RequestBody IrrigationStatus irrigationStatus) throws IOException {
        irrigationService.setIrrigationStatus(name, irrigationStatus);
    }

}
