package com.isc.authentication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isc.authentication.dto.request.ConfirmRegistrationRequest;
import com.isc.authentication.model.User;
import com.isc.authentication.model.enums.UserTypeEnum;
import com.isc.authentication.repository.UserRepository;
import com.isc.authentication.security.JwtTokenUtil;
import com.isc.authentication.utils.exception.ResourceNotFoundException;
import com.isc.authentication.utils.messaging.MessagingService;
import com.isc.authentication.utils.messaging.UpdateRegistryMessage;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.validation.ValidationException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;

@Service
@Log
public class RegistrationService {

    private static final String SYSTEM_USERNAME = "admin@system.it";

    private static final String SYSTEM_PASSWORD = "alskFIa!niNGGA";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MessagingService messagingService;

    @Value("${application.url}")
    private String applicationUrl;



    @Bean(name = "regis")
    public Consumer<Message<String>> updateRegistry() {
        return (message) -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                UpdateRegistryMessage regis = mapper.readValue(message.getPayload(), UpdateRegistryMessage.class);
                //log.log(Level.INFO, "UPDATE REGISTRY RECEIVED! ID " + regis.getIdRegistry());
                User user = userRepository.findByRegistryIdAndEnabled(regis.getIdRegistry(), true).get();
                userRepository.save(user);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public void confirmRegistration(ConfirmRegistrationRequest request) {
        User user = userRepository.findByUuid(request.getUuid()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.isEnabled()) {
            throw new ValidationException("User already enabled");
        } else {
            user.setEnabled(true);
            userRepository.save(user);
            messagingService.finalizeInvitation(user.getEmail());
            log.log(Level.INFO, "Confirmed Registration");
        }
    }

    private void loginAsSystem() {
        User user = new User();
        user.setEmail(SYSTEM_USERNAME);
        user.setUsertype((UserTypeEnum.ADMIN));
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        requestAttributes.getRequest().getSession().setAttribute(HttpHeaders.AUTHORIZATION, "Bearer " + jwtTokenUtil.generateJwt(user, Boolean.FALSE));
    }

    public void enableUser(String uuid) {

        Optional<User> user = Optional.ofNullable(userRepository.findByUuid(uuid).orElseThrow(() -> new ResourceNotFoundException("User with uuid: " + uuid + " not found")));
        if (user.get().isEnabled()) {
            throw new RuntimeException("user already enabled");
        }
        user.get().setEnabled(true);
        userRepository.save(user.get());

    }

}
