package com.postech.scheduling_service.controller;

import com.postech.scheduling_service.dto.CreateSchedulingDto;
import com.postech.scheduling_service.dto.SchedulingDto;
import com.postech.scheduling_service.dto.UpdateSchedulingDto;
import com.postech.scheduling_service.use_cases.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/v1/schedulings", produces = "application/json")
@RequiredArgsConstructor
public class SchedulingController {

    private final CreateSchedulingUseCase createUC;
    private final UpdateSchedulingUseCase updateUC;
    private final FindSchedulingUseCase findUC;
    private final ListSchedulingUseCase listUC;
    private final CancellationSchedulingUseCase cancelUC;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<SchedulingDto> create(@Validated @RequestBody CreateSchedulingDto dto) {
        var created = createUC.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<SchedulingDto> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateSchedulingDto dto
    ) {
        var updated = updateUC.execute(dto.withId(id));
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchedulingDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(findUC.execute(id));
    }

    @GetMapping
    public ResponseEntity<PageEnvelope<SchedulingDto>> list(
            @RequestParam(name = "patientId", required = false) Long patientId,
            @RequestParam(name = "doctorId", required = false) Long doctorId,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "fromDate", required = false) String fromDate,
            @RequestParam(name = "toDate", required = false) String toDate,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        var params = ListSchedulingUseCase.ListSchedulingParams.builder()
                .patientId(patientId)
                .doctorId(doctorId)
                .status(status)
                // .fromDate(fromDate) // descomente se existir no builder
                // .toDate(toDate)     // idem
                .page(page)
                .size(size)
                .build();

        var resultPage = listUC.execute(params); // deve retornar Page<SchedulingDto>
        var body = PageEnvelope.of(resultPage);  // evita problemas de JSON em Page
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable("id") Long id) {
        cancelUC.execute(id);
        return ResponseEntity.noContent().build();
    }

    // Envelope simples para serializar Page de forma estável
    public record PageEnvelope<T>(
            java.util.List<T> content,
            int page,
            int size,
            long totalElements,
            int totalPages
    ) {
        public static <T> PageEnvelope<T> of(org.springframework.data.domain.Page<T> page) {
            return new PageEnvelope<>(
                    page.getContent(),
                    page.getNumber(),
                    page.getSize(),
                    page.getTotalElements(),
                    page.getTotalPages()
            );
        }
    }
}
