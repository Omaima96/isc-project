package com.isc.authentication.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank
    @Schema(description = "User email", pattern = "")
    private String email;

    @NotBlank
    @Schema(description = "User phonenumber", pattern = "")
    private int phoneNumber;

    @NotBlank
    @Schema(description = "User username", pattern = "")
    private String username;

    @NotBlank
    @Schema(description = "the user password")
    private String password;

    @Schema(description = "If enabled, returns a valid token for 30 days , otherwise an hour", defaultValue = "false")
    private Boolean rememberMe = Boolean.FALSE;
}
