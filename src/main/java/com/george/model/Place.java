package com.george.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "place")
public class Place {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(id, place.id) &&
                Objects.equals(name, place.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", minMoistureThreshold=" + minMoistureThreshold +
                ", maxMoistureThreshold=" + maxMoistureThreshold +
                '}';
    }

}
