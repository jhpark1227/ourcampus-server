ALTER TABLE alarm DROP INDEX idx_alarm_member_id;
ALTER TABLE alarm ADD INDEX idx_alarm_member_id (member_id);