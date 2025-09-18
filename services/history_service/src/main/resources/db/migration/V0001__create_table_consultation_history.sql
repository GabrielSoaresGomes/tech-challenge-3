CREATE TABLE IF NOT EXISTS consultation_history (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    consultation_id BIGINT NOT NULL,
    patient_name   VARCHAR(150) NOT NULL,
    doctor_name    VARCHAR(150) NOT NULL,
    consultation_date TIMESTAMPTZ NOT NULL,
    status         VARCHAR(20) NOT NULL CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED', 'NO_SHOW', 'RESCHEDULED')),
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT NOW()
);