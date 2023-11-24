package com.isc.authentication.dto.response.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class LoggedUser {

    @Schema(description = "Authentication token to do applicative operations. "
            + "The token must be passed in the 'Authorization' header as 'Bearer [token]'")
    private final String token;

    @Schema(description = "The name of the logged user")
    private String name;

    @Schema(description = "The surname of the logged user")
    private String surname;

}
