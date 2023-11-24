package com.isc.authentication.client;

import com.isc.authentication.dto.request.RegistrationRequest;
import com.isc.authentication.dto.response.RegistryResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "registry", url = "gateway:8080", fallbackFactory = RegistryFallback.class)
@CircuitBreaker(name = "registry")
public interface RegistryUser {

    @PostMapping("/registry")
    RegistryResponse createRegistry(@RequestBody RegistrationRequest request);

}

@Slf4j
@Component
class RegistryFallback implements FallbackFactory<RegistryUser> {


    @Override
    public RegistryUser create(Throwable cause) {
        return new RegistryUser() {
            @Override
            public RegistryResponse createRegistry(RegistrationRequest request) {
                log.error("Fallback interface! - ERROR: {}", cause.getMessage());
                throw new RuntimeException("Error while creating registry - ERROR: " + cause);
            }
        };
    }
}

