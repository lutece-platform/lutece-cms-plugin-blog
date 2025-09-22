-- liquibase formatted sql
-- changeset blog:init_db_blog.sql
-- preconditions onFail:MARK_RAN onError:WARN
INSERT INTO blog_content_type values(1, 'Image'), (2, 'PDF');