ALTER TABLE reservation
    ADD INDEX idx_reservation_facility_id_start_time (facility_id, start_time);