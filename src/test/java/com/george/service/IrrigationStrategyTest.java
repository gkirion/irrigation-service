package com.george.service;

import com.george.exception.PlaceNotFoundException;
import com.george.model.Action;
import com.george.model.MoistureThresholds;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class IrrigationStrategyTest {

    @Mock
    private MoistureThresholdsService moistureThresholdsService;

    @InjectMocks
    private IrrigationStrategy irrigationStrategy;

    private MoistureThresholds moistureThresholds;

    @BeforeEach
    public void init() throws PlaceNotFoundException {
        moistureThresholds = new MoistureThresholds();
        moistureThresholds.setMinMoistureThreshold(200.0);
        moistureThresholds.setMaxMoistureThreshold(250.0);
        Mockito.when(moistureThresholdsService.getMoistureThresholds(Mockito.anyString())).thenReturn(Optional.of(moistureThresholds));
    }

    @Test
    public void placeNotFoundTest() {
        Mockito.when(moistureThresholdsService.getMoistureThresholds(Mockito.anyString())).thenThrow(new PlaceNotFoundException(""));
        Assertions.assertThrows(PlaceNotFoundException.class, () -> irrigationStrategy.evaluateAction("", 180.0));
    }

    @Test
    public void shouldStartIrrigationTest() {
        Assertions.assertEquals(Action.START, irrigationStrategy.evaluateAction("", 180.0));
    }

    @Test
    public void shouldStopIrrigationTest() {
        Assertions.assertEquals(Action.STOP, irrigationStrategy.evaluateAction("", 280.0));
    }

    @Test
    public void shouldDoNothingTest() {
        Assertions.assertEquals(Action.NOTHING, irrigationStrategy.evaluateAction("", 220.0));
    }

}
