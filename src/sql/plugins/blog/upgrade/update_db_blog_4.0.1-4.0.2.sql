-- liquibase formatted sql
-- changeset blog:update_db_blog_4.0.1-4.0.2.sql
-- preconditions onFail:MARK_RAN onError:WARN

ALTER TABLE blog_blog ADD COLUMN display_toc boolean default false NOT NULL;
ALTER TABLE blog_blog ADD COLUMN display_related boolean default false NOT NULL;
ALTER TABLE blog_blog ADD COLUMN max_related int default 3 NOT NULL;

DELETE FROM core_attribute WHERE id_attribute=10;
INSERT INTO core_attribute (id_attribute, type_class_name, title, help_message, is_mandatory, is_shown_in_search, is_shown_in_result_list, is_field_in_line, attribute_position, plugin_name, anonymize) VALUES (10, 'fr.paris.lutece.portal.business.user.attribute.AttributeText', 'blog_author_title', 'Signature pour les articles  - utilisé dans le plugin Blog si rempli-', 0, 0, 0, 0, 1, NULL, NULL);
DELETE FROM core_attribute WHERE id_attribute=11;
INSERT INTO core_attribute (id_attribute, type_class_name, title, help_message, is_mandatory, is_shown_in_search, is_shown_in_result_list, is_field_in_line, attribute_position, plugin_name, anonymize) VALUES (11, 'fr.paris.lutece.portal.business.user.attribute.AttributeText', 'blog_author_role', 'Ajoute le type de poste dans le rendu d&#39;un article - plugin Blog -', 0, 0, 0, 0, 2, NULL, NULL);

DELETE FROM core_attribute_field WHERE id_field=10;
INSERT INTO core_attribute_field (id_field, id_attribute, title, DEFAULT_value, is_DEFAULT_value, height, width, max_size_enter, is_multiple, field_position) VALUES (10, 10, NULL, 'Ville de Paris', 0, 0, 50, 255, 0, 1);
DELETE FROM core_attribute_field WHERE id_field=11;
INSERT INTO core_attribute_field (id_field, id_attribute, title, DEFAULT_value, is_DEFAULT_value, height, width, max_size_enter, is_multiple, field_position) VALUES (11, 11, NULL, 'Rédacteur - Ville de Paris', 0, 0, 50, 255, 0, 2);


--
-- The FreeMarker templates of the blog portlets are now managed by the core (core_portlet_template, core_portlet.id_template,
-- "Gestion des modèles de rubrique" feature). The plugin's own registry (blog_page_template, blog_portlet.id_page_template_document,
-- blog_list_portlet.id_page_template_document) is migrated to it, then removed.
--
-- The plugin upgrade scripts run BEFORE the core upgrade script in the same liquibase run (sql/plugins/* sorts before sql/upgrade/*) :
-- the core structures are created here when they do not exist yet, with the very same statements as the core script, which is then skipped.
--

-- changeset blog:update_db_blog_4.0.1-4.0.2.sql-rev1.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = database() AND table_name = 'core_portlet' AND column_name = 'id_template'
ALTER TABLE core_portlet ADD COLUMN id_template int default 0 NOT NULL;

-- changeset blog:update_db_blog_4.0.1-4.0.2.sql-rev2.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = database() AND table_name = 'core_portlet_template'
CREATE TABLE IF NOT EXISTS core_portlet_template (
	id_template int AUTO_INCREMENT NOT NULL,
	id_portlet_type varchar(50) default NULL,
	description varchar(255) default NULL,
	template_path varchar(255) default NULL,
	PRIMARY KEY (id_template)
);

-- changeset blog:update_db_blog_4.0.1-4.0.2.sql-rev3.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_portlet_template WHERE id_portlet_type IN ('BLOG_PORTLET', 'BLOG_LIST_PORTLET')
INSERT INTO core_portlet_template (id_portlet_type, description, template_path) VALUES ('BLOG_PORTLET', 'Post template', 'skin/plugins/blog/portlet/default_portlet_blog.html');
INSERT INTO core_portlet_template (id_portlet_type, description, template_path) VALUES ('BLOG_LIST_PORTLET', 'Posts list template', 'skin/plugins/blog/portlet/default_portlet_list_blog.html');

-- changeset blog:update_db_blog_4.0.1-4.0.2.sql-rev4.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment Each blog portlet keeps its template : the plugin template is matched to the core template with the same path for its portlet type (templates added by the site included)
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = database() AND table_name = 'blog_page_template'
INSERT INTO core_portlet_template (id_portlet_type, description, template_path)
	SELECT bt.portlet_type, bt.description, bt.page_template_path FROM blog_page_template bt
	WHERE bt.portlet_type IN ('BLOG_PORTLET', 'BLOG_LIST_PORTLET') AND bt.page_template_path IS NOT NULL
	AND bt.page_template_path NOT IN (SELECT ct.template_path FROM core_portlet_template ct WHERE ct.id_portlet_type = bt.portlet_type AND ct.template_path IS NOT NULL);
UPDATE core_portlet SET id_template = COALESCE( (
		SELECT MIN(ct.id_template) FROM core_portlet_template ct, blog_page_template bt, blog_portlet bp
		WHERE bp.id_portlet = core_portlet.id_portlet AND bt.id_page_template_document = bp.id_page_template_document
		AND ct.id_portlet_type = core_portlet.id_portlet_type AND ct.template_path = bt.page_template_path ), 0 )
	WHERE id_portlet_type = 'BLOG_PORTLET'
	AND id_portlet IN (SELECT bp2.id_portlet FROM blog_portlet bp2, blog_page_template bt2 WHERE bt2.id_page_template_document = bp2.id_page_template_document AND bt2.page_template_path IS NOT NULL);
UPDATE core_portlet SET id_template = COALESCE( (
		SELECT MIN(ct.id_template) FROM core_portlet_template ct, blog_page_template bt, blog_list_portlet bp
		WHERE bp.id_portlet = core_portlet.id_portlet AND bt.id_page_template_document = bp.id_page_template_document
		AND ct.id_portlet_type = core_portlet.id_portlet_type AND ct.template_path = bt.page_template_path ), 0 )
	WHERE id_portlet_type = 'BLOG_LIST_PORTLET'
	AND id_portlet IN (SELECT bp2.id_portlet FROM blog_list_portlet bp2, blog_page_template bt2 WHERE bt2.id_page_template_document = bp2.id_page_template_document AND bt2.page_template_path IS NOT NULL);
DROP TABLE blog_page_template;

-- changeset blog:update_db_blog_4.0.1-4.0.2.sql-rev5.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = database() AND table_name = 'blog_portlet' AND column_name = 'id_page_template_document'
ALTER TABLE blog_portlet DROP COLUMN id_page_template_document;

-- changeset blog:update_db_blog_4.0.1-4.0.2.sql-rev6.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = database() AND table_name = 'blog_list_portlet' AND column_name = 'id_page_template_document'
ALTER TABLE blog_list_portlet DROP COLUMN id_page_template_document;
