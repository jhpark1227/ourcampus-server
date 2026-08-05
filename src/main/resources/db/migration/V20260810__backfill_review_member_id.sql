UPDATE review r
    JOIN reservation res ON r.reservation_id = res.id
SET r.member_id = res.member_id
WHERE r.member_id IS NULL;