-- liquibase formatted sql
-- changeset blog:update_db_blog_3.0.4-3.0.5.sql
-- preconditions onFail:MARK_RAN onError:WARN

update blog_blog set description = '' where description is null;
update blog_versions set description = '' where description is null;

ALTER TABLE blog_blog CHANGE COLUMN description description long varchar NOT NULL DEFAULT '';
ALTER TABLE blog_versions CHANGE COLUMN description description long varchar NOT NULL DEFAULT '';
