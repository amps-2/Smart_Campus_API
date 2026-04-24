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
import com.example.dao.GenericDAO;
import com.example.exception.LinkedResourceNotFoundException;
import com.example.model.Sensor;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/sensors")
public class SensorResource {

    private GenericDAO<Sensor> sensorDAO = new GenericDAO<>(DataStore.sensors);

    // GET /api/v1/sensors - Get all sensors (with optional type filter)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensors = sensorDAO.getAll();

        if (type != null && !type.isEmpty()) {
            sensors = sensors.stream()
                    .filter(s -> s.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }

        return Response.ok(sensors).build();
    }

    // POST /api/v1/sensors - Register a new sensor
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSensor(Sensor sensor) {
        // Validate sensor ID
        if (sensor.getId() == null || sensor.getId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Sensor ID is required\"}")
                    .build();
        }

        // Validate that the roomId exists
        if (sensor.getRoomId() == null || !DataStore.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                "Room with ID " + sensor.getRoomId() + " does not exist"
            );
        }

        // Check sensor already exists
        if (sensorDAO.exists(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\": \"Sensor already exists\"}")
                    .build();
        }

        // Add sensor and update room's sensorIds list
        sensorDAO.add(sensor);
        DataStore.rooms.get(sensor.getRoomId()).getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }

    // GET /api/v1/sensors/{sensorId} - Get a specific sensor
    @GET
    @Path("/{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensorDAO.getById(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Sensor not found\"}")
                    .build();
        }
        return Response.ok(sensor).build();
    }

    // Sub-resource locator for sensor readings
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensorDAO.getById(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor with ID " + sensorId + " not found");
        }
        return new SensorReadingResource(sensorId);
    }
}