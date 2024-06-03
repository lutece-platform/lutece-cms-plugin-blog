--
-- Table structure for table blog_admin_dashboard
--
DROP TABLE IF EXISTS blog_admin_dashboard;
CREATE TABLE blog_admin_dashboard (
                                      id_dashboard int NOT NULL,
                                      number_mandatory_tags int default 0 NOT NULL,
                                      maximum_publication_date date default '2050-01-01' NOT NULL

);
INSERT INTO blog_admin_dashboard (id_dashboard,number_mandatory_tags, maximum_publication_date) VALUES (1,0, '2030-01-01');

INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES
    ('BLOG_AVANCED_CONFIGURATION','blog.adminFeature.AdvancedConfiguration.name',0,'jsp/admin/plugins/blog/ManageAdminDashboard.jsp','blog.adminFeature.AdvancedConfiguration.description',0,'blog','APPLICATIONS','ti ti-settings',NULL,5);

INSERT INTO core_user_right (id_right,id_user) VALUES ('BLOG_AVANCED_CONFIGURATION',1);

INSERT INTO core_admin_dashboard(dashboard_name, dashboard_column, dashboard_order) VALUES('blogAdminDashboardComponent', 1, 7);
