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
