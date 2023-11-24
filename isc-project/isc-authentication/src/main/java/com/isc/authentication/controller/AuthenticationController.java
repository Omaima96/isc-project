package com.isc.authentication.controller;

import com.isc.authentication.configuration.OpenApiConfig;
import com.isc.authentication.dto.request.LoginRequest;
import com.isc.authentication.dto.response.CheckEmailResponse;
import com.isc.authentication.dto.response.security.LoggedUser;
import com.isc.authentication.service.AuthenticationService;
import com.isc.authentication.utils.exception.ResponseError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "authentication", description = "API for the management of the user:  login, refresh token, cambio e reset password.")
@RequestMapping("/authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Operation(summary = "Login", description = "API to obtain an auth token", tags = {"authentication"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login",
                    content = @Content(schema = @Schema(implementation = LoggedUser.class))),
            @ApiResponse(responseCode = "400", description = "Error request validation",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "401", description = "Wrong credentials or Disabled user",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "403", description = "Expired credentials",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", description = "Generic Error",
                    content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @PostMapping("/login-user")
    public ResponseEntity<LoggedUser> login(@RequestBody LoginRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @Operation(summary = "Refresh Token", description = "API to obtain a new autenticazion token. "
            + "Needs in the Header 'Authorization' the old token (non the expired one). "
            + "In the case of token remember-me the new token is valid for 30 days, otherwise an hour.", tags = {"authentication"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
                    content = @Content(schema = @Schema(implementation = LoggedUser.class))),
            @ApiResponse(responseCode = "401", description = "Old token non valid or expired",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", description = "Generic Error",
                    content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @GetMapping(value = "/refresh-token",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    public ResponseEntity<LoggedUser> refreshToken() {
        return ResponseEntity.ok(authenticationService.refreshToken());
    }

    @Operation(summary = "Check email", description = "API to control if the email already exists in the system.",
            tags = {"authentication"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email not present",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", description = "Generic Error",
                    content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @GetMapping(value = "/check-email",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    public ResponseEntity<CheckEmailResponse> checkEmail(@RequestParam(value = "email") String email) {
        return ResponseEntity.ok(authenticationService.checkEmail(email));
    }

}