-- liquibase formatted sql
-- changeset blog:update_db_blog_4.0.0-4.0.1.sql
-- preconditions onFail:MARK_RAN onError:WARN

UPDATE core_admin_right SET icon_url='ti ti-list-letters' WHERE  id_right='BLOG_MANAGEMENT';
UPDATE core_admin_right SET icon_url='ti ti-tags' WHERE  id_right='BLOG_TAGS_MANAGEMENT';