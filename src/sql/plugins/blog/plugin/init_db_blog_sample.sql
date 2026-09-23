-- liquibase formatted sql
-- changeset blog:init_db_blog_sample.sql
-- preconditions onFail:MARK_RAN onError:WARN
INSERT INTO blog_page_template(id_page_template_document, page_template_path, picture_path, description, portlet_type)
values(1,'skin/plugins/blog/portlet/default_portlet_blog.html','no picture','Post template', 'BLOG_PORTLET');

INSERT INTO blog_page_template(id_page_template_document, page_template_path, picture_path, description, portlet_type)
values(0,'skin/plugins/blog/portlet/default_portlet_list_blog.html','no picture','Posts list template', 'BLOG_LIST_PORTLET');

--
-- The plugin's own template registry (blog_page_template, id_page_template_document) is replaced by the core one (core_portlet_template,
-- core_portlet.id_template). Dropped here, after the inserts above, so that a fresh install plays the base changeset without error.
--
-- changeset blog:init_db_blog_sample.sql-rev1.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = database() AND table_name = 'blog_page_template'
DROP TABLE blog_page_template;

-- changeset blog:init_db_blog_sample.sql-rev2.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = database() AND table_name = 'blog_portlet' AND column_name = 'id_page_template_document'
ALTER TABLE blog_portlet DROP COLUMN id_page_template_document;

-- changeset blog:init_db_blog_sample.sql-rev3.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = database() AND table_name = 'blog_list_portlet' AND column_name = 'id_page_template_document'
ALTER TABLE blog_list_portlet DROP COLUMN id_page_template_document;
