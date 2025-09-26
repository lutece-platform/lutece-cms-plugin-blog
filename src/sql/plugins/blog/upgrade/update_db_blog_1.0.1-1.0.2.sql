-- liquibase formatted sql
-- changeset blog:update_db_blog_1.0.1-1.0.2.sql
-- preconditions onFail:MARK_RAN onError:WARN
--
-- Update name of html blog list portlet file.
--
UPDATE core_portlet_type
SET modify_specific = '/admin/plugins/blog/portlet/modify_portletbloglist.html'
WHERE id_portlet_type = 'BLOG_LIST_PORTLET';