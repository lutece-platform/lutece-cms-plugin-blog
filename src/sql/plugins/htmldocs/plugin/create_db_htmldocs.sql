
--
-- Structure for table htmldocs
--

DROP TABLE IF EXISTS htmldocs;
CREATE TABLE htmldocs (
id_html_doc int NOT NULL,
version int default '0' NOT NULL,
content_label varchar(50) default '' NOT NULL,
creation_date datetime NOT NULL,
update_date datetime NOT NULL,
html_content LONG VARCHAR,
user_editor varchar(100) default '' NOT NULL,
user_creator varchar(100) default '' NOT NULL,
attached_portlet_id int NOT NULL,
edit_comment varchar(100) default '' NOT NULL,
description long varchar,
shareable int default 0 NOT NULL,

PRIMARY KEY (id_html_doc)
);

--
-- Structure for table document_content
--
DROP TABLE IF EXISTS htmldocs_content;
CREATE TABLE htmldocs_content (
	id_html_doc int NOT NULL,
	id_document int default 0 NOT NULL,
	text_value long varchar,
	mime_type varchar(255) default NULL,
	binary_value long varbinary,
	PRIMARY KEY (id_document)
);

--

--
-- Structure for table htmldocs_portlet
--

DROP TABLE IF EXISTS htmldocs_portlet;
CREATE TABLE htmldocs_portlet (
id_portlet int NOT NULL,
name varchar(50) default '' NOT NULL,
content_id int NOT NULL,
PRIMARY KEY (id_portlet)
);

--
-- Structure for table htmldocs_versions
--

DROP TABLE IF EXISTS htmldocs_versions;
CREATE TABLE htmldocs_versions (
id_version int NOT NULL,
id_html_doc int NOT NULL,
version int default '0' NOT NULL,
content_label varchar(50) default '' NOT NULL,
creation_date datetime NOT NULL,
update_date datetime NOT NULL,
html_content LONG VARCHAR,
user_editor varchar(100) default '' NOT NULL,
user_creator varchar(100) default '' NOT NULL,
attached_portlet_id int NOT NULL,
edit_comment varchar(100) default '' NOT NULL,
description long varchar,
shareable int default 0 NOT NULL,

PRIMARY KEY (id_version)
);

--
-- Structure for table htmldocs_tag
--

DROP TABLE IF EXISTS htmldocs_tag;
CREATE TABLE htmldocs_tag (
id_tag int NOT NULL,
name varchar(50) NOT NULL,
PRIMARY KEY (id_tag)
);


--
-- Structure for table htmldocs_tag
--

DROP TABLE IF EXISTS htmldocs_tag_document;
CREATE TABLE htmldocs_tag_document (
id_tag int NOT NULL,
id_html_doc int NOT NULL,
CONSTRAINT `fk_htmldocs` FOREIGN KEY(`id_html_doc`) references htmldocs (`id_html_doc`),
CONSTRAINT `fk_id_tag` FOREIGN KEY(`id_tag`) references htmldocs_tag(`id_tag`),

PRIMARY KEY (id_tag, id_html_doc)
);

/*==============================================================*/
/* Table structure for table htmldocs_indexer_action				*/
/*==============================================================*/
DROP TABLE IF EXISTS htmldocs_indexer_action;
CREATE TABLE htmldocs_indexer_action (
  id_action INT DEFAULT 0 NOT NULL,
  id_htmldoc INT DEFAULT 0 NOT NULL,
  id_task INT DEFAULT 0 NOT NULL ,
  PRIMARY KEY (id_action)
  );
CREATE INDEX htmldocs_id_indexer_task ON htmldocs_indexer_action (id_task);

--
-- Table structure for table htmldocs_page_template
--
DROP TABLE IF EXISTS htmldocs_page_template;
CREATE TABLE htmldocs_page_template (
	id_page_template_document int default 0 NOT NULL,
	page_template_path varchar(255) default NULL,
	picture_path varchar(255) default NULL,
	description varchar(255) default NULL,
	
	PRIMARY KEY (id_page_template_document)
);

--
-- Table structure for table htmldocs_list_portlet
--
DROP TABLE IF EXISTS htmldocs_list_portlet;
CREATE TABLE htmldocs_list_portlet (
	id_portlet int default NULL,
	id_page_template_document int default 0 NOT NULL,
	
	PRIMARY KEY (id_portlet)
);

DROP TABLE IF EXISTS htmldocs_list_portlet_htmldocs;
CREATE TABLE htmldocs_list_portlet_htmldocs (
	id_portlet int default NULL,
	id_html_doc int NOT NULL,
    date_begin_publishing timestamp default CURRENT_TIMESTAMP NOT NULL,
	date_end_publishing timestamp default  "2030-01-01 11:59:59" NOT NULL,
	status int default 0 NOT NULL,
	document_order int default NULL,

	
    CONSTRAINT `fk_id_html_doc_portlet` FOREIGN KEY(`id_html_doc`) references htmldocs(`id_html_doc`),
	PRIMARY KEY (id_portlet, id_html_doc)
);




