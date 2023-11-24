package com.isc.authentication.controller;

import com.isc.authentication.configuration.OpenApiConfig;
import com.isc.authentication.model.Access;
import com.isc.authentication.service.AccessService;
import com.isc.authentication.utils.exception.ResponseError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "access-maagement", description = "API for the access management")
public class AccessController {

    @Autowired
    private AccessService accessService;

    @Operation(summary = "list of logs", description = "API for the log' s list.",
            tags = {"access-management"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Complete log list"),
            @ApiResponse(responseCode = "404", description = "log non found",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "401", description = "you are not authenticated",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", description = "Generic error",
                    content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    @GetMapping(value = "/access", produces = "application/json")
    public ResponseEntity<Page<Access>> getall(@RequestParam(value = "filter", required = false) String filter,
                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                               @RequestParam(defaultValue = "0") Integer pageNumber,
                                               @RequestParam(defaultValue = "ASC") Sort.Direction direction,
                                               @RequestParam(defaultValue = "name") String sortField) {
        PageRequest page = PageRequest.of(pageNumber, pageSize, direction, sortField);
        Page<Access> listAccess = accessService.getAllAccessi(filter, page);
        return new ResponseEntity<Page<Access>>(listAccess, HttpStatus.OK);
    }
}
