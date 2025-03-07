package com.george.service;

import com.george.exception.PlaceNotFoundException;
import com.george.model.Action;
import com.george.model.LandStatus;
import com.george.model.Place;
import com.george.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class IrrigationService implements LandStatusListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(IrrigationService.class);

    @Autowired
    private IrrigationStrategy irrigationStrategy;

    @Autowired
    private SensorReadingService sensorReadingService;

    @Autowired
    private IrrigationStatusService irrigationStatusService;

    @Autowired
    private PlaceService placeService;

    private IrrigationQueue irrigationQueue;

    public IrrigationService(IrrigationQueue irrigationQueue) {
        this.irrigationQueue = irrigationQueue;
        irrigationQueue.addLandStatusListener(this);
    }

    @Override
    public void receiveLandStatus(LandStatus landStatus) {

        try {

            sensorReadingService.insert(landStatus.getPlace(), landStatus.getMoisture());
            irrigationStatusService.updateIrrigationStatus(landStatus.getPlace(), landStatus.getIrrigationStatus());

            Status irrigationStatus = landStatus.getIrrigationStatus();
            Action irrigationAction = irrigationStrategy.evaluateAction(landStatus.getPlace(), landStatus.getMoisture());

            if (irrigationStatus == Status.OFF && irrigationAction == Action.START) {
                setIrrigationStatus(landStatus.getPlace(), Status.ON);

            } else if (irrigationStatus == Status.ON && irrigationAction == Action.STOP) {
                setIrrigationStatus(landStatus.getPlace(), Status.OFF);
            }

        } catch (PlaceNotFoundException placeNotFoundException) {
            LOGGER.warn("place not found", placeNotFoundException);
        }

    }

    public void setIrrigationStatus(String place, Status irrigationStatus)  {
        LOGGER.info("place: {}, setting irrigation status to: {}", place, irrigationStatus);
        try {
            irrigationQueue.sendCommand(place, irrigationStatus);
        } catch (IOException e) {
            LOGGER.error("could not send data", e);
        }
    }

    public void setIrrigationStatus(UUID id, Status irrigationStatus)  {
        Place place = placeService.findById(id);
        setIrrigationStatus(place.getName(), irrigationStatus);
    }

    public Optional<Status> getIrrigationStatus(UUID id) {
        return irrigationStatusService.getIrrigationStatus(id)
                .map(irrigationStatus -> irrigationStatus.getStatus());
    }

}
