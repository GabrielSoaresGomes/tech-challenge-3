package com.postech.scheduling_service.repository;

import com.postech.scheduling_service.entity.Scheduling;
import com.postech.scheduling_service.enums.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface SchedulingRepository extends JpaRepository<Scheduling, Long> {
    @Query("""
  select s from Scheduling s
  where (:patientId is null or s.patientId = :patientId)
    and (:doctorId  is null or s.doctorId  = :doctorId)
    and (:status    is null or s.status    = :status)
    and (:fromDate  is null or s.startAt  >= :fromDate)
    and (:toDate    is null or s.endAt    <= :toDate)
""")
    Page<Scheduling> findAllByFilters(Long patientId, Long doctorId, StatusEnum status,
                                      LocalDateTime fromDate, LocalDateTime toDate,
                                      org.springframework.data.domain.Pageable pageable);

}
