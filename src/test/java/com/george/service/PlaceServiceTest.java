package com.george.service;

import com.george.exception.PlaceNotFoundException;
import com.george.model.Place;
import com.george.repository.PlaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class PlaceServiceTest {

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private PlaceService placeService;

    @Test
    public void placeNotFoundTest() {
        Mockito.when(placeRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Assertions.assertThrows(PlaceNotFoundException.class, () -> placeService.update(UUID.randomUUID(), new Place()));
    }

    @Test
    public void placeNameEmptyTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> placeService.create(new Place()));
    }

    @Test
    public void placeOkTest() {
        Place place = new Place();
        place.setName("my pot");
        Assertions.assertDoesNotThrow(() -> placeService.create(place));
    }

}
