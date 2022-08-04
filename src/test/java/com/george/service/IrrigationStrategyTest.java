package com.george.service;

import com.george.exception.PlaceNotFoundException;
import com.george.model.IrrigationAction;
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
    public void shouldStartIrrigationTest() throws Exception {
        Assertions.assertEquals(IrrigationAction.START, irrigationStrategy.evaluateAction("", 180.0));
    }

    @Test
    public void shouldStopIrrigationTest() throws Exception {
        Assertions.assertEquals(IrrigationAction.STOP, irrigationStrategy.evaluateAction("", 280.0));
    }

    @Test
    public void shouldDoNothingTest() throws Exception {
        Assertions.assertEquals(IrrigationAction.NOTHING, irrigationStrategy.evaluateAction("", 220.0));
    }

}
