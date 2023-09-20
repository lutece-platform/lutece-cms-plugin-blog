--
-- Dumping data for table core_portlet_type
--
DELETE FROM core_portlet_type where id_portlet_type = 'BLOG_PORTLET';
DELETE FROM core_portlet_type where id_portlet_type = 'BLOG_LIST_PORTLET';
INSERT INTO core_portlet_type (id_portlet_type,name,url_creation,url_update,home_class,plugin_name,url_docreate,create_script,create_specific,create_specific_form,url_domodify,modify_script,modify_specific,modify_specific_form) VALUES ('BLOG_PORTLET','blog.portlet.blogsBlogsPortlet.name','plugins/blog/CreatePortletBlog.jsp','plugins/blog/ModifyPortletBlog.jsp','fr.paris.lutece.plugins.blog.business.portlet.BlogPortletHome','blog','plugins/blog/DoCreatePortletBlog.jsp','/admin/portlet/script_create_portlet.html','/admin/plugins/blog/portlet/create_portletblogs.html','','plugins/blog/DoModifyPortletBlog.jsp','/admin/portlet/script_modify_portlet.html','/admin/plugins/blog/portlet/modify_portletblogs.html','','text-caption');
INSERT INTO core_portlet_type (id_portlet_type,name,url_creation,url_update,home_class,plugin_name,url_docreate,create_script,create_specific,create_specific_form,url_domodify,modify_script,modify_specific,modify_specific_form) VALUES ('BLOG_LIST_PORTLET','blog.portlet.blogsListBlogsPortlet.name','plugins/blog/CreatePortletBlogList.jsp','plugins/blog/ModifyPortletBlogList.jsp','fr.paris.lutece.plugins.blog.business.portlet.BlogListPortletHome','blog','plugins/blog/DoCreatePortletBlogList.jsp','/admin/portlet/script_create_portlet.html','/admin/plugins/blog/portlet/create_portletbloglist.html','','plugins/blog/DoModifyPortletBlogList.jsp','/admin/portlet/script_modify_portlet.html','/admin/plugins/blog/portlet/modify_portletbloglist.html','','list-details');

--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right IN ( 'BLOG_MANAGEMENT', 'BLOG_TAGS_MANAGEMENT');
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url ) VALUES
('BLOG_MANAGEMENT','blog.adminFeature.ManageBlogs.name',2,'jsp/admin/plugins/blog/ManageBlogs.jsp','blog.adminFeature.ManageBlogs.description',0,'blog','APPLICATIONS','ti ti-notebook','jsp/admin/documentation/AdminDocumentation.jsp?doc=admin-blog');
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('BLOG_TAGS_MANAGEMENT','blog.adminFeature.ManageBlogsTags.name',2,'jsp/admin/plugins/blog/ManageTags.jsp','blog.adminFeature.ManageBlogsTags.description',0,'blog','APPLICATIONS','ti ti-tags',NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'BLOG_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('BLOG_MANAGEMENT',1);

INSERT INTO core_datastore(entity_key, entity_value) VALUES ('number.documents.to.be.loaded', '10');
INSERT INTO core_datastore(entity_key, entity_value) VALUES ('use_upload_image_plugin', 'false');
INSERT INTO core_datastore(entity_key, entity_value) VALUES ('blog.duration.lock', '600000');