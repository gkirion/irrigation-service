package com.george.service;

import com.george.exception.PlaceNotFoundException;
import com.george.model.LandStatus;
import com.george.model.Place;
import com.george.model.SensorReading;
import com.george.repository.PlaceRepository;
import com.george.repository.SensorReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SensorReadingService {

    @Autowired
    private SensorReadingRepository sensorReadingRepository;

    @Autowired
    private PlaceRepository placeRepository;

    public SensorReading insert(LandStatus landStatus) throws PlaceNotFoundException {
        Place place = placeRepository.findByName(landStatus.getPlace()).orElseThrow(() -> new PlaceNotFoundException("place " + landStatus.getPlace() + " does not exist"));
        SensorReading sensorReading = new SensorReading();
        sensorReading.setPlace(place);
        sensorReading.setTimestamp(new Date());
        sensorReading.setValue(landStatus.getMoisture());
        return sensorReadingRepository.save(sensorReading);
    }

}
