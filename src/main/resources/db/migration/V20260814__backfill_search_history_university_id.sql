UPDATE search_history sh
    JOIN member m ON sh.member_id = m.id
SET sh.university_id = m.university_id
WHERE sh.university_id IS NULL;

DELETE FROM search_history WHERE university_id IS NULL;