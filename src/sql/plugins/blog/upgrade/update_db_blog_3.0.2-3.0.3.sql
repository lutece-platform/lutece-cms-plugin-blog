INSERT INTO core_admin_role (role_key, role_description) VALUES('blog_resources', 'Blog resources administrator');
INSERT INTO core_admin_role_resource (role_key,resource_type,resource_id,permission) VALUES ('blog_resources','TAG','*','*'),
INSERT INTO core_admin_role_resource (role_key,resource_type,resource_id,permission) VALUES ('blog_resources','BLOG','*','*');
INSERT INTO core_user_role (role_key, id_user) VALUES('blog_resources', 1);

-- new admin dashboard
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES
    ('BLOG_AVANCED_CONFIGURATION','blog.adminFeature.AdvancedConfiguration.name',0,'jsp/admin/plugins/blog/ManageAdminDashboard.jsp','blog.adminFeature.AdvancedConfiguration.description',0,'blog','APPLICATIONS','ti ti-settings',NULL,5);

INSERT INTO core_user_right (id_right,id_user) VALUES ('BLOG_AVANCED_CONFIGURATION',1);

INSERT INTO core_admin_dashboard(dashboard_name, dashboard_column, dashboard_order) VALUES('blogAdminDashboardComponent', 1, 7);
