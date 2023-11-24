package com.isc.authentication.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RegistryResponse{


    @Schema(description = "Registry id created")
    private Long idRegistry;

}
