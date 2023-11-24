package com.isc.authentication.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConfirmRegistrationRequest {

    @NotBlank(message = "UUID missed")
    @Schema(description = "User' UUID to be confirmed")
    private String uuid;

}
