package com.isc.authentication.controller;

import com.isc.authentication.dto.request.ConfirmRegistrationRequest;
import com.isc.authentication.service.RegistrationService;
import com.isc.authentication.utils.exception.ResponseError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Tag(name = "sign-up", description = "API for users registration")
@RequestMapping("/sign-up")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;


    @Operation(summary = "Confirm Registration", description = "API to confirm and enable user.",
            tags = {"registration"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User confirmed and enabled successfully."),
            @ApiResponse(responseCode = "400", description = "User already enabled",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "400", description = "Error validation request",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "404", description = "User non found",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", description = "Generic Error",
                    content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @PostMapping(value = "/confirm-registration")
    public ResponseEntity<?> confirmRegistration(@Valid @RequestBody
                                                   @Parameter(required = true, schema = @Schema(implementation = ConfirmRegistrationRequest.class)) ConfirmRegistrationRequest request) {
        registrationService.confirmRegistration(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
