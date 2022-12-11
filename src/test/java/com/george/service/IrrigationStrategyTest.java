package com.george.service;

import com.george.exception.PlaceNotFoundException;
import com.george.model.Action;
import com.george.model.Place;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IrrigationStrategyTest {

    @Mock
    private PlaceService placeService;

    @InjectMocks
    private IrrigationStrategy irrigationStrategy;

    private Place place;

    @BeforeEach
    public void init() throws PlaceNotFoundException {
        place = new Place();
        place.setMinMoistureThreshold(200.0);
        place.setMaxMoistureThreshold(250.0);
        Mockito.when(placeService.findByName(Mockito.anyString())).thenReturn(place);
    }

    @Test
    public void placeNotFoundTest() {
        Mockito.when(placeService.findByName(Mockito.anyString())).thenThrow(new PlaceNotFoundException(""));
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
