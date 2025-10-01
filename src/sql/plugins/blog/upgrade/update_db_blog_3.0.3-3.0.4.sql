-- liquibase formatted sql
-- changeset blog:update_db_blog_3.0.3-3.0.4.sql
-- preconditions onFail:MARK_RAN onError:WARN
ALTER TABLE blog_blog modify COLUMN id_blog int AUTO_INCREMENT;
ALTER TABLE blog_versions modify COLUMN id_version int AUTO_INCREMENT;
ALTER TABLE blog_tag modify COLUMN id_tag int AUTO_INCREMENT;
ALTER TABLE blog_content modify COLUMN id_document int AUTO_INCREMENT;
ALTER TABLE blog_indexer_action modify COLUMN id_action int AUTO_INCREMENT;
ALTER TABLE blog_page_template modify COLUMN id_page_template_document int AUTO_INCREMENT;

