-- liquibase formatted sql
-- changeset blog:update_db_blog_1.0.0-1.0.1.sql
-- preconditions onFail:MARK_RAN onError:WARN
INSERT INTO core_datastore(entity_key, entity_value) VALUES ('use_upload_image_plugin', 'false');