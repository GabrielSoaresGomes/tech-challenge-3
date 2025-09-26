package com.postech.history.repository;

import com.postech.history.dto.ConsultationHistoryDTO;
import com.postech.history.dto.ConsultationStatus;
import com.postech.history.dto.RegisterConsultationDTO;
import com.postech.history.dto.RegisterConsultationResponseDTO;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public class ConsultationHistoryRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public ConsultationHistoryRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static ConsultationHistoryDTO map(java.sql.ResultSet rs, int row) throws java.sql.SQLException {
        return new ConsultationHistoryDTO(
                rs.getLong("id"),
                rs.getLong("consultation_id"),
                rs.getString("patient_name"),
                rs.getString("doctor_name"),
                rs.getObject("consultation_date", OffsetDateTime.class),
                ConsultationStatus.valueOf(rs.getString("status")),
                rs.getObject("created_at", OffsetDateTime.class),
                rs.getObject("updated_at", OffsetDateTime.class)
        );
    }

    public List<ConsultationHistoryDTO> listAll() {
        var sql = """
      SELECT id, consultation_id, patient_name, doctor_name,
             consultation_date, status, created_at, updated_at
      FROM consultation_history
      ORDER BY id DESC
    """;
        return jdbc.getJdbcTemplate().query(sql, ConsultationHistoryRepository::map);
    }

    public List<ConsultationHistoryDTO> findByConsultationId(long id) {
        var sql = """
      SELECT id, consultation_id, patient_name, doctor_name,
             consultation_date, status, created_at, updated_at
      FROM consultation_history
      WHERE consultation_id = :id
      ORDER BY id DESC
    """;
        return jdbc.query(sql, new MapSqlParameterSource("id", id), ConsultationHistoryRepository::map);
    }

    public List<ConsultationHistoryDTO> findByPatientName(String name) {
        var sql = """
      SELECT id, consultation_id, patient_name, doctor_name,
             consultation_date, status, created_at, updated_at
      FROM consultation_history
      WHERE patient_name ILIKE :name
      ORDER BY id DESC
    """;
        return jdbc.query(sql, new MapSqlParameterSource("name", "%" + name + "%"), ConsultationHistoryRepository::map);
    }

    public RegisterConsultationResponseDTO insert(RegisterConsultationDTO dto) {
        var sql = """
      INSERT INTO consultation_history
        (consultation_id, patient_name, doctor_name, consultation_date, status)
      VALUES
        (:consultation_id, :patient_name, :doctor_name, :consultation_date, :status)
      RETURNING id
    """;
        var params = new MapSqlParameterSource()
                .addValue("consultation_id", dto.consultationId())
                .addValue("patient_name", dto.patientName())
                .addValue("doctor_name", dto.doctorName())
                .addValue("consultation_date", dto.consultationDate())
                .addValue("status", dto.status().name());

        Long id = jdbc.queryForObject(sql, params, Long.class);
        return new RegisterConsultationResponseDTO(id, dto.consultationId(), "Histórico de consulta registrado");
    }
}
