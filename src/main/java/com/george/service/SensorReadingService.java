package com.george.service;

import com.george.exception.PlaceNotFoundException;
import com.george.model.LandStatus;
import com.george.model.Place;
import com.george.model.SensorReading;
import com.george.repository.SensorReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SensorReadingService {

    @Autowired
    private SensorReadingRepository sensorReadingRepository;

    @Autowired
    private PlaceService placeService;

    public SensorReading insert(LandStatus landStatus) throws PlaceNotFoundException {
        Place place = placeService.findByName(landStatus.getPlace());
        SensorReading sensorReading = new SensorReading();
        sensorReading.setPlace(place);
        sensorReading.setTimestamp(new Date());
        sensorReading.setValue(landStatus.getMoisture());
        return sensorReadingRepository.save(sensorReading);
    }

}
