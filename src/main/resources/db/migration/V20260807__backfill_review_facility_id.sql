UPDATE review r
    JOIN reservation res ON r.reservation_id = res.id
SET r.facility_id = res.facility_id
WHERE r.facility_id IS NULL;