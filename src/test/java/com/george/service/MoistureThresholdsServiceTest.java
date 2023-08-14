package com.george.service;

import com.george.exception.InvalidThresholdsException;
import com.george.exception.PlaceNotFoundException;
import com.george.model.MoistureThresholds;
import com.george.repository.MoistureThresholdsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MoistureThresholdsServiceTest {

    @Mock
    private  PlaceService placeService;

    @Mock
    private MoistureThresholdsRepository moistureThresholdsRepository;

    @InjectMocks
    private MoistureThresholdsService moistureThresholdsService;

    private final String PLACE_NAME = "a place";

    @Test
    public void placeNotFoundTest() {
        Mockito.when(placeService.findByName(Mockito.any())).thenThrow(PlaceNotFoundException.class);
        Assertions.assertThrows(PlaceNotFoundException.class, () -> moistureThresholdsService.setMoistureThresholds(PLACE_NAME, new MoistureThresholds()));
    }

    @Test
    public void placeThresholdMissingTest() {
        MoistureThresholds moistureThresholds = new MoistureThresholds();
        moistureThresholds.setMinMoistureThreshold(200.0);
        Assertions.assertThrows(IllegalArgumentException.class, () -> moistureThresholdsService.setMoistureThresholds(PLACE_NAME, moistureThresholds));
    }

    @Test
    public void minEqualToMaxMoistureTest() {
        MoistureThresholds moistureThresholds = new MoistureThresholds();
        moistureThresholds.setMinMoistureThreshold(200.0);
        moistureThresholds.setMaxMoistureThreshold(200.0);
        Assertions.assertThrows(InvalidThresholdsException.class, () -> moistureThresholdsService.setMoistureThresholds(PLACE_NAME, moistureThresholds));
    }

    @Test
    public void minMoreThanMaxMoistureTest() {
        MoistureThresholds moistureThresholds = new MoistureThresholds();
        moistureThresholds.setMinMoistureThreshold(250.0);
        moistureThresholds.setMaxMoistureThreshold(200.0);
        Assertions.assertThrows(InvalidThresholdsException.class, () -> moistureThresholdsService.setMoistureThresholds(PLACE_NAME, moistureThresholds));
    }

    @Test
    public void minLessThanMaxMoistureTest() {
        MoistureThresholds moistureThresholds = new MoistureThresholds();
        moistureThresholds.setMinMoistureThreshold(200.0);
        moistureThresholds.setMaxMoistureThreshold(250.0);
        Assertions.assertDoesNotThrow(() -> moistureThresholdsService.setMoistureThresholds(PLACE_NAME, moistureThresholds));
    }

}
