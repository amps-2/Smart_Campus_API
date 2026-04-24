/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.resource;

/**
 *
 * @author Welcome
 */

import com.example.dao.DataStore;
import com.example.exception.SensorUnavailableException;
import com.example.model.Sensor;
import com.example.model.SensorReading;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SensorReadingResource {

    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // GET /api/v1/sensors/{sensorId}/readings - Get all readings for a sensor
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadings() {
        List<SensorReading> readings = DataStore.sensorReadings
                .getOrDefault(sensorId, new ArrayList<>());
        return Response.ok(readings).build();
    }

    // POST /api/v1/sensors/{sensorId}/readings - Add a new reading
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);

        // Check if sensor is in MAINTENANCE
        if (sensor.getStatus().equalsIgnoreCase("MAINTENANCE")) {
            throw new SensorUnavailableException(
                "Sensor " + sensorId + " is under maintenance and cannot accept readings"
            );
        }

        // Generate ID and timestamp if not provided
        if (reading.getId() == null || reading.getId().isEmpty()) {
            reading.setId(UUID.randomUUID().toString());
        }
        if (reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        // Add reading to DataStore
        DataStore.sensorReadings
                .computeIfAbsent(sensorId, k -> new ArrayList<>())
                .add(reading);

        // Update the parent sensor's currentValue
        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }
}