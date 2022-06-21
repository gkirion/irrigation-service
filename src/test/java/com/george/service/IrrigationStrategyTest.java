package com.george.service;

import com.george.model.IrrigationAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IrrigationStrategyTest {

    @Test
    public void minEqualToMaxMoistureTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new IrrigationStrategy(200.0, 200.0));
    }

    @Test
    public void minMoreThanMaxMoistureTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new IrrigationStrategy(250.0, 200.0));
    }

    @Test
    public void minLessThanMaxMoistureTest() {
        Assertions.assertDoesNotThrow(() -> new IrrigationStrategy(200.0, 250.0));
    }

    @Test
    public void shouldStartIrrigationTest() {
        IrrigationStrategy irrigationStrategy = new IrrigationStrategy(200.0, 250.0);
        Assertions.assertEquals(IrrigationAction.START, irrigationStrategy.evaluateAction(180.0));
    }

    @Test
    public void shouldStopIrrigationTest() {
        IrrigationStrategy irrigationStrategy = new IrrigationStrategy(200.0, 250.0);
        Assertions.assertEquals(IrrigationAction.STOP, irrigationStrategy.evaluateAction(280.0));
    }

    @Test
    public void shouldDoNothingTest() {
        IrrigationStrategy irrigationStrategy = new IrrigationStrategy(200.0, 250.0);
        Assertions.assertEquals(IrrigationAction.NOTHING, irrigationStrategy.evaluateAction(220.0));
    }

}
