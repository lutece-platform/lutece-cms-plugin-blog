--
-- Dumping data for table core_portlet_type
--
INSERT INTO core_portlet_type (id_portlet_type,name,url_creation,url_update,home_class,plugin_name,url_docreate,create_script,create_specific,create_specific_form,url_domodify,modify_script,modify_specific,modify_specific_form) VALUES ('HTMLDOCS_PORTLET','htmldocs.portlet.htmldocsHtmldocsPortlet.name','plugins/htmldocs/CreatePortletHtmldocs.jsp','plugins/htmldocs/ModifyPortletHtmldocs.jsp','fr.paris.lutece.plugins.htmldocs.business.portlet.HtmldocsPortletHome','htmldocs','plugins/htmldocs/DoCreatePortletHtmldocs.jsp','/admin/portlet/script_create_portlet.html','/admin/plugins/htmldocs/portlet/create_portlethtmldocs.html','','plugins/htmldocs/DoModifyPortletHtmldocs.jsp','/admin/portlet/script_modify_portlet.html','/admin/plugins/htmldocs/portlet/modify_portlethtmldocs.html','');

--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'HTMLDOCS_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('HTMLDOCS_MANAGEMENT','htmldocs.adminFeature.ManageHtmldocs.name',1,'jsp/admin/plugins/htmldocs/ManageHtmlDocs.jsp','htmldocs.adminFeature.ManageHtmldocs.description',0,'htmldocs','CONTENT',NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'HTMLDOCS_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('HTMLDOCS_MANAGEMENT',1);

