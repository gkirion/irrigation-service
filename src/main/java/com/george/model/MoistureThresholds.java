package com.george.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "moistureThresholds")
public class MoistureThresholds {

    @Id
    @Column(name = "place_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "place_id", columnDefinition = "BINARY(16)")
    @MapsId
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Place place;

    @Column(name = "min_moisture_threshold", nullable = false)
    private Double minMoistureThreshold;

    @Column(name = "max_moisture_threshold", nullable = false)
    private Double maxMoistureThreshold;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Double getMinMoistureThreshold() {
        return minMoistureThreshold;
    }

    /**
     * @param minMoistureThreshold The min moisture, any value below that will activate the irrigation.
     */
    public void setMinMoistureThreshold(Double minMoistureThreshold) {
        this.minMoistureThreshold = minMoistureThreshold;
    }

    public Double getMaxMoistureThreshold() {
        return maxMoistureThreshold;
    }

    /**
     * @param maxMoistureThreshold The max moisture, any value above that will deactivate the irrigation.
     */
    public void setMaxMoistureThreshold(Double maxMoistureThreshold) {
        this.maxMoistureThreshold = maxMoistureThreshold;
    }

    @Override
    public String toString() {
        return "MoistureThresholds{" +
                "place=" + place +
                ", minMoistureThreshold=" + minMoistureThreshold +
                ", maxMoistureThreshold=" + maxMoistureThreshold +
                '}';
    }

}
