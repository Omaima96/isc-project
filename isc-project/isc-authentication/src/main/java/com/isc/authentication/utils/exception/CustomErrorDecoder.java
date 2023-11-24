package com.isc.authentication.utils.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import javax.validation.ValidationException;
import java.io.IOException;
import java.io.InputStream;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        ExceptionMessage message = null;
        try (InputStream bodyIs = response.body()
                .asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, ExceptionMessage.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }

        switch (response.status()) {
            case 400:
                throw new ValidationException(message.getMessage());
            case 401:
                throw new UnauthenticatedException(message.getMessage());

            case 403:
                throw new UnauthorizedException(message.getMessage());
            case 404:
                throw new ResourceNotFoundException(message.getMessage());
            default:
                return new Exception(message.getMessage());
        }
    }
}
