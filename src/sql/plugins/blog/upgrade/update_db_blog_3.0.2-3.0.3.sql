INSERT INTO core_admin_role (role_key, role_description) VALUES('blog_resources', 'Blog resources administrator');
INSERT INTO core_admin_role_resource (role_key,resource_type,resource_id,permission) VALUES ('blog_resources','TAG','*','*'),
INSERT INTO core_admin_role_resource (role_key,resource_type,resource_id,permission) VALUES ('blog_resources','BLOG','*','*');
INSERT INTO core_user_role (role_key, id_user) VALUES('blog_resources', 1);

INSERT INTO core_user_right (id_right,id_user) VALUES ('MANAGE_ADVANCED_PARAMETERS',1);
INSERT INTO core_admin_dashboard(dashboard_name, dashboard_column, dashboard_order) VALUES('blogAdminDashboardComponent', 1, 7);

INSERT INTO core_datastore(entity_key, entity_value) VALUES ('blog.advanced_parameters.number_mandatory_tags', '0');
