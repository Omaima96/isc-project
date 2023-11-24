package com.isc.authentication.dto.request;

import com.isc.authentication.utils.Utility;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegistrationRequest {


    @Email(message = "Not valid Email")
    @NotBlank(message = "Email empty or null")
    @Schema(description = "User email")
    private String email;

    @NotBlank(message = "Username empty or null")
    @Schema(description = "User username")
    private String username;

    @NotBlank(message = "PhoneNumber empty or null")
    @Schema(description = "User phoneNumber")
    private int phoneNumber;

    @NotBlank(message = "Password empty or null")
    @Pattern(regexp = Utility.PASSWORD_PATTERN, message = "This password does not respect the security criteria")
    @Size(min = 8, max = 64, message = "The length of the password must be between  {min} and {max} caracters")
    @Schema(description = "User password")
    private String password;
}