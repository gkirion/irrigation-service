package com.george.service;

import com.george.model.IrrigationStatus;
import com.george.model.Place;
import com.george.model.Status;
import com.george.repository.IrrigationStatusRepository;
import com.george.repository.PlaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class IrrigationStatusServiceTest {

    @Autowired
    private IrrigationStatusRepository irrigationStatusRepository;

    @Autowired
    private PlaceRepository placeRepository;

    private final String PLACE_NAME = "a place";

    @Test
    public void placeDoesNotExistReturnEmptyTest() {
        Assertions.assertTrue(irrigationStatusRepository.findByPlaceName(PLACE_NAME).isEmpty());
    }

    @Test
    public void placeDoesNotHaveStatusReturnEmptyTest() {
        Place place = new Place();
        place.setName(PLACE_NAME);
        placeRepository.save(place);

        Assertions.assertTrue(placeRepository.findByName(PLACE_NAME).isPresent());
        Assertions.assertTrue(irrigationStatusRepository.findByPlaceName(PLACE_NAME).isEmpty());
    }

    @Test
    public void placeDoesHaveStatusReturnStatusTest() {
        Place place = new Place();
        place.setName(PLACE_NAME);
        placeRepository.save(place);
        IrrigationStatus irrigationStatus = new IrrigationStatus();
        irrigationStatus.setPlace(place);
        irrigationStatus.setStatus(Status.OFF);
        irrigationStatusRepository.save(irrigationStatus);

        Assertions.assertTrue(placeRepository.findByName(PLACE_NAME).isPresent());
        Assertions.assertTrue(irrigationStatusRepository.findByPlaceName(PLACE_NAME).isPresent());
    }

}
