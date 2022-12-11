package com.george.service;

import com.george.exception.PlaceNotFoundException;
import com.george.model.Place;
import com.george.model.SensorReading;
import com.george.repository.SensorReadingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SensorReadingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorReadingService.class);

    @Autowired
    private SensorReadingRepository sensorReadingRepository;

    @Autowired
    private PlaceService placeService;

    public SensorReading insert(String placeName, Double moisture) throws PlaceNotFoundException {
        LOGGER.info("inserting sensor reading with value: {} for place: {}", moisture, placeName);
        Place place = placeService.findByName(placeName);
        SensorReading sensorReading = new SensorReading();
        sensorReading.setPlace(place);
        sensorReading.setTimestamp(new Date());
        sensorReading.setMoisture(moisture);
        return sensorReadingRepository.save(sensorReading);
    }

}
