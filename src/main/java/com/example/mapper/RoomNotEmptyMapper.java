/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.mapper;

/**
 *
 * @author Welcome
 */

import com.example.exception.RoomNotEmptyException;
import com.example.model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RoomNotEmptyMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        ErrorMessage error = new ErrorMessage(
            exception.getMessage(),
            409,
            "https://smartcampus.com/api/docs/errors"
        );
        return Response.status(Response.Status.CONFLICT)
                .entity(error)
                .build();
    }
}