--
-- Structure for table blog_content_type
--
DROP TABLE IF EXISTS blog_content_type;
CREATE TABLE blog_content_type (
	id_type int NOT NULL,
	type_label varchar(255),
	PRIMARY KEY (id_type)
);

--
-- Structure for table blog
--
DROP TABLE IF EXISTS blog_blog;
CREATE TABLE blog_blog (
id_blog int NOT NULL,
version int default '0' NOT NULL,
content_label varchar(255) default '' NOT NULL,
creation_date datetime NOT NULL,
update_date datetime NOT NULL,
html_content LONG VARCHAR,
user_editor varchar(255) default '' NOT NULL,
user_creator varchar(255) default '' NOT NULL,
attached_portlet_id int NOT NULL,
edit_comment varchar(255) default '' NOT NULL,
description long varchar,
shareable int default 0 NOT NULL,
url varchar(255) default '',

PRIMARY KEY (id_blog)
);

DROP TABLE IF EXISTS blog_content;
--
-- Structure for table blog_content
--
CREATE TABLE blog_content (
	id_document int default 0 NOT NULL,
	id_type int default 0 NOT NULL,
	text_value long varchar,
	mime_type varchar(255) default NULL,
	binary_value long varbinary,
	CONSTRAINT fk_content_type FOREIGN KEY(id_type) references blog_content_type (id_type),
	PRIMARY KEY (id_document)
);

--
-- Structure for table blog_blog_content
--
DROP TABLE IF EXISTS blog_blog_content;
CREATE TABLE blog_blog_content (
id_document int NOT NULL,
id_blog int NOT NULL,
priority int NOT NULL,
CONSTRAINT fk_id_blog_blog FOREIGN KEY(id_blog) references blog_blog(id_blog),
PRIMARY KEY (id_document, id_blog)
);


--
-- Structure for table blog_portlet
--
DROP TABLE IF EXISTS blog_portlet;
CREATE TABLE blog_portlet (
id_portlet int NOT NULL,
name varchar(255) default '' NOT NULL,
content_id int NOT NULL,
id_page_template_document int default 0 NOT NULL,

PRIMARY KEY (id_portlet)
);

--
-- Structure for table blog_versions
--
DROP TABLE IF EXISTS blog_versions;
CREATE TABLE blog_versions (
id_version int NOT NULL,
id_blog int NOT NULL,
version int default '0' NOT NULL,
content_label varchar(255) default '' NOT NULL,
creation_date datetime NOT NULL,
update_date datetime NOT NULL,
html_content LONG VARCHAR,
user_editor varchar(255) default '' NOT NULL,
user_creator varchar(255) default '' NOT NULL,
attached_portlet_id int NOT NULL,
edit_comment varchar(255) default '' NOT NULL,
description long varchar,
shareable int default 0 NOT NULL,
url varchar(255) default '',

PRIMARY KEY (id_version)
);

--
-- Structure for table blogs_tag
--
DROP TABLE IF EXISTS blog_tag;
CREATE TABLE blog_tag (
id_tag int NOT NULL,
name varchar(50) NOT NULL,
PRIMARY KEY (id_tag)
);

--
-- Structure for table blog_tag
--
DROP TABLE IF EXISTS blog_tag_document;
CREATE TABLE blog_tag_document (
id_tag int NOT NULL,
id_blog int NOT NULL,
priority int NOT NULL,
CONSTRAINT fk_blog FOREIGN KEY(id_blog) references blog_blog (id_blog),
CONSTRAINT fk_id_tag FOREIGN KEY(id_tag) references blog_tag(id_tag),
PRIMARY KEY (id_tag, id_blog)
);

/*==============================================================*/
/* Table structure for table blog_indexer_action				*/
/*==============================================================*/
DROP TABLE IF EXISTS blog_indexer_action;
CREATE TABLE blog_indexer_action (
  id_action INT NOT NULL,
  id_blog INT DEFAULT 0 NOT NULL,
  id_task INT DEFAULT 0 NOT NULL ,
  PRIMARY KEY (id_action)
  );
CREATE INDEX blog_id_indexer_task ON blog_indexer_action (id_task);

--
-- Table structure for table blog_page_template
--
DROP TABLE IF EXISTS blog_page_template;
CREATE TABLE blog_page_template (
	id_page_template_document int default 0 NOT NULL,
	page_template_path varchar(255) default NULL,
	picture_path varchar(255) default NULL,
	description varchar(255) default NULL,
	portlet_type varchar(255) default NULL,
	
	PRIMARY KEY (id_page_template_document)
);

--
-- Table structure for table blog_list_portlet
--
DROP TABLE IF EXISTS blog_list_portlet;
CREATE TABLE blog_list_portlet (
	id_portlet int NOT NULL,
	id_page_template_document int default 0 NOT NULL,
	
	PRIMARY KEY (id_portlet)
);

DROP TABLE IF EXISTS blog_list_portlet_htmldocs;
CREATE TABLE blog_list_portlet_htmldocs (
	id_portlet int NOT NULL,
	id_blog int NOT NULL,
    date_begin_publishing timestamp default CURRENT_TIMESTAMP NOT NULL,
	date_end_publishing timestamp default  '2050-01-01 11:59:59' NOT NULL,
	status int default 0 NOT NULL,
	document_order int default NULL,

	
    CONSTRAINT fk_id_blog_portlet FOREIGN KEY(id_blog) references blog_blog(id_blog),
	PRIMARY KEY (id_portlet, id_blog)
);

--
-- Table structure for table blog_rss_cf
--
DROP TABLE IF EXISTS blog_rss_cf;
CREATE TABLE blog_rss_cf (
	id_rss int NOT NULL,
	id_portlet int default 0 NOT NULL,
	
	PRIMARY KEY (id_rss)
);