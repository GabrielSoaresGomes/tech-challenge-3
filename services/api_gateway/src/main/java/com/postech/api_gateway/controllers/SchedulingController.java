package com.postech.api_gateway.controllers;

import com.postech.api_gateway.dtos.CancellationSchedulingDTO;
import com.postech.api_gateway.dtos.CreationSchedulingDTO;
import com.postech.api_gateway.services.SchedulingPublisherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scheduling")
public class SchedulingController {

    private final SchedulingPublisherService schedulingPublisherService;

    public SchedulingController(SchedulingPublisherService schedulingPublisherService) {
        this.schedulingPublisherService = schedulingPublisherService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createScheduling(@RequestBody CreationSchedulingDTO creationSchedulingDTO) {
        schedulingPublisherService.sendNewScheduling(creationSchedulingDTO);
        return ResponseEntity.ok("Agendamento criado com sucesso");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelScheduling(@RequestBody CancellationSchedulingDTO cancellationSchedulingDTO) {
        schedulingPublisherService.sendCancellationScheduling(cancellationSchedulingDTO);
        return ResponseEntity.ok("Agendamento cancelado com sucesso");
    }
}
