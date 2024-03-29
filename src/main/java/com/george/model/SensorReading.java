package com.george.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "sensor_reading")
public class SensorReading {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "place_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Place place;

    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    @Column(name = "moisture", nullable = false)
    private Double moisture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Double getMoisture() {
        return moisture;
    }

    public void setMoisture(Double moisture) {
        this.moisture = moisture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorReading that = (SensorReading) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(place, that.place) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(moisture, that.moisture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, place, timestamp, moisture);
    }

    @Override
    public String toString() {
        return "SensorReading{" +
                "id=" + id +
                ", place=" + place +
                ", timestamp=" + timestamp +
                ", moisture=" + moisture +
                '}';
    }

}
