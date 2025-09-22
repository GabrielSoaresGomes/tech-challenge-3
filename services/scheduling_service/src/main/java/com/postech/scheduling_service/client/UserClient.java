package com.postech.scheduling_service.client;

import com.postech.scheduling_service.configuration.FeignHeadersConfig;
import com.postech.scheduling_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "auth",
        url = "${app.services.auth}",
        configuration = FeignHeadersConfig.class
)
public interface UserClient {
    @GetMapping("/internal/user/{id}")
    UserDto getUser(@PathVariable("id") Long id);
}
