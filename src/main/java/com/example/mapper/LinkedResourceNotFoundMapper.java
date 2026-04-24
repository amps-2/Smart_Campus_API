/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.mapper;

/**
 *
 * @author Welcome
 */

import com.example.exception.LinkedResourceNotFoundException;
import com.example.model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class LinkedResourceNotFoundMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        ErrorMessage error = new ErrorMessage(
            exception.getMessage(),
            422,
            "https://smartcampus.com/api/docs/errors"
        );
        return Response.status(422)
                .entity(error)
                .build();
    }
}