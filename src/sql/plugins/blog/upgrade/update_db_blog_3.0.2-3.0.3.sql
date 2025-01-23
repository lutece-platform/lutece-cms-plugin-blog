INSERT INTO core_admin_role (role_key, role_description) VALUES('blog_resources', 'Blog resources administrator');
INSERT INTO core_admin_role_resource (role_key,resource_type,resource_id,permission) VALUES ('blog_resources','TAG','*','*');
INSERT INTO core_admin_role_resource (role_key,resource_type,resource_id,permission) VALUES ('blog_resources','BLOG','*','*');
INSERT INTO core_user_role (role_key, id_user) VALUES('blog_resources', 1);

INSERT INTO core_user_right (id_right,id_user) VALUES ('MANAGE_ADVANCED_PARAMETERS',1);
INSERT INTO core_admin_dashboard(dashboard_name, dashboard_column, dashboard_order,dashboard_state ) VALUES('blogAdminDashboardComponent', 1, 7, 0);

INSERT INTO core_datastore(entity_key, entity_value) VALUES ('blog.advanced_parameters.number_mandatory_tags', '0');
INSERT INTO core_datastore(entity_key, entity_value) VALUES ('blog.advanced_parameters.default_date_end_publishing', '01/01/2050');
INSERT INTO core_datastore(entity_key, entity_value) VALUES ('blog.advanced_parameters.editor', 'tinymce5');

-- add a version to blogs that does not have one
INSERT INTO blog_versions (id_version,id_blog,version,content_label,creation_date,update_date,html_content,user_editor,user_creator,attached_portlet_id,edit_comment,description,shareable,url)
    SELECT a.id_blog, a.id_blog, 1, a.content_label, a.creation_date, a.update_date, a.html_content, a.user_editor, a.user_creator , a.attached_portlet_id, a.edit_comment , a.description, a.shareable, a.url
    FROM blog_blog a
    WHERE a.version = 1
ON DUPLICATE KEY UPDATE id_version=id_version;

