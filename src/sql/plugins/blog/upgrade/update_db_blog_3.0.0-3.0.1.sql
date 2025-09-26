-- liquibase formatted sql
-- changeset blog:update_db_blog_3.0.0-3.0.1.sql
-- preconditions onFail:MARK_RAN onError:WARN
ALTER TABLE blog_list_portlet_htmldocs MODIFY date_end_publishing date default '2050-01-01' NOT NULL;
