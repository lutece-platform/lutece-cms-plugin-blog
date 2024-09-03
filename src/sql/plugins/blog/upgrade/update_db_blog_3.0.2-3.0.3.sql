INSERT INTO core_admin_role (role_key, role_description) VALUES('blog_resources', 'Blog resources administrator');
INSERT INTO core_admin_role_resource (role_key,resource_type,resource_id,permission) VALUES ('blog_resources','TAG','*','*'),
INSERT INTO core_admin_role_resource (role_key,resource_type,resource_id,permission) VALUES ('blog_resources','BLOG','*','*');
INSERT INTO core_user_role (role_key, id_user) VALUES('blog_resources', 1);
