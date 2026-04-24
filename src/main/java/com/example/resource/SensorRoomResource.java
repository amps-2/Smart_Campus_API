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
import com.example.exception.RoomNotEmptyException;
import com.example.model.Room;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/rooms")
public class SensorRoomResource {

    private GenericDAO<Room> roomDAO = new GenericDAO<>(DataStore.rooms);

    // GET /api/v1/rooms - Get all rooms
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRooms() {
        List<Room> rooms = roomDAO.getAll();
        return Response.ok(rooms).build();
    }

    // POST /api/v1/rooms - Create a new room
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {
        if (room.getId() == null || room.getId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Room ID is required\"}")
                    .build();
        }
        if (roomDAO.exists(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\": \"Room already exists\"}")
                    .build();
        }
        roomDAO.add(room);
        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }

    // GET /api/v1/rooms/{roomId} - Get a specific room
    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = roomDAO.getById(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Room not found\"}")
                    .build();
        }
        return Response.ok(room).build();
    }

    // DELETE /api/v1/rooms/{roomId} - Delete a room
    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = roomDAO.getById(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Room not found\"}")
                    .build();
        }
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room " + roomId + " still has sensors assigned to it");
        }
        roomDAO.delete(roomId);
        return Response.noContent().build();
    }
}