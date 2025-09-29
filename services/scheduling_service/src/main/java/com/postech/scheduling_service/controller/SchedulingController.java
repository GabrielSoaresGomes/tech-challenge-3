package com.postech.scheduling_service.controller;

import com.postech.scheduling_service.dto.CreateSchedulingDto;
import com.postech.scheduling_service.dto.SchedulingDto;
import com.postech.scheduling_service.dto.UpdateSchedulingDto;
import com.postech.scheduling_service.dto.history.ConsultationDto;
import com.postech.scheduling_service.use_cases.*;
import com.postech.scheduling_service.use_cases.history.GetHistoryByPatientNameUseCase;
import com.postech.scheduling_service.use_cases.history.GetSchedulingHistoryUseCase;
import com.postech.scheduling_service.use_cases.history.ListConsultationHistoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/schedulings", produces = "application/json")
@RequiredArgsConstructor
public class SchedulingController {

    private final CreateSchedulingUseCase createUC;
    private final UpdateSchedulingUseCase updateUC;
    private final FindSchedulingUseCase findUC;
    private final ListSchedulingUseCase listUC;
    private final CancellationSchedulingUseCase cancelUC;
    private final GetSchedulingHistoryUseCase getSchedulingHistoryUC;
    private final ListConsultationHistoryUseCase listConsultationHistoryUC;
    private final GetHistoryByPatientNameUseCase getHistoryByPatientNameUC;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<SchedulingDto> create(@Validated @RequestBody CreateSchedulingDto dto) {
        var created = createUC.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public SchedulingDto update(@PathVariable("id") Long id, @RequestBody UpdateSchedulingDto params) {
        params.setId(id);
        return this.updateUC.execute(params);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchedulingDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(findUC.execute(id));
    }

    @GetMapping
    public List<SchedulingDto> list() {
        return this.listUC.execute(null);
    }

    @DeleteMapping("/{id}")
    public void cancel(@PathVariable("id") Long id) {
        this.cancelUC.execute(id);
    }

    @GetMapping("/history")
    public List<ConsultationDto> listAllHistory() {
        return listConsultationHistoryUC.execute(null);
    }

    @GetMapping("/history/by-patient")
    public List<ConsultationDto> getHistoryByPatient(@RequestParam("name") String name) {
        return getHistoryByPatientNameUC.execute(name);
    }

    @GetMapping("/{schedulingId}/history")
    public List<ConsultationDto> getSchedulingHistory(@PathVariable("schedulingId") Long schedulingId) {
        return getSchedulingHistoryUC.execute(schedulingId);
    }
}
