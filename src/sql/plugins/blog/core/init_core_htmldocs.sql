--
-- Dumping data for table core_portlet_type
--
INSERT INTO core_portlet_type (id_portlet_type,name,url_creation,url_update,home_class,plugin_name,url_docreate,create_script,create_specific,create_specific_form,url_domodify,modify_script,modify_specific,modify_specific_form) VALUES ('HTMLDOCS_PORTLET','blog.portlet.htmldocsHtmldocsPortlet.name','plugins/blog/CreatePortletHtmldocs.jsp','plugins/blog/ModifyPortletHtmldocs.jsp','fr.paris.lutece.plugins.blog.business.portlet.HtmldocsPortletHome','blog','plugins/blog/DoCreatePortletHtmldocs.jsp','/admin/portlet/script_create_portlet.html','/admin/plugins/blog/portlet/create_portlethtmldocs.html','','plugins/blog/DoModifyPortletHtmldocs.jsp','/admin/portlet/script_modify_portlet.html','/admin/plugins/blog/portlet/modify_portlethtmldocs.html','');
INSERT INTO core_portlet_type (id_portlet_type,name,url_creation,url_update,home_class,plugin_name,url_docreate,create_script,create_specific,create_specific_form,url_domodify,modify_script,modify_specific,modify_specific_form) VALUES ('HTMLDOCSLIST_PORTLET','blog.portlet.htmldocsListHtmldocsPortlet.name','plugins/blog/CreatePortletHtmldocsList.jsp','plugins/blog/ModifyPortletHtmldocsList.jsp','fr.paris.lutece.plugins.blog.business.portlet.HtmlDocsListPortletHome','blog','plugins/blog/DoCreatePortletHtmldocsList.jsp','/admin/portlet/script_create_portlet.html','/admin/plugins/blog/portlet/create_portlethtmldocslist.html','','plugins/blog/DoModifyPortletHtmldocsList.jsp','/admin/portlet/script_modify_portlet.html','/admin/plugins/blog/portlet/modify_portlethtmldocslist.html','');

--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right IN ( 'HTMLDOCS_MANAGEMENT', 'HTMLDOCS_TAGS_MANAGEMENT');
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('HTMLDOCS_TAGS_MANAGEMENT','blog.adminFeature.ManageHtmldocsTags.name',1,'jsp/admin/plugins/blog/ManageTags.jsp','blog.adminFeature.ManageHtmldocsTags.description',0,'blog','CONTENT',NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'HTMLDOCS_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('HTMLDOCS_MANAGEMENT',1);

