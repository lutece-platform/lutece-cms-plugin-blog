
--
-- Structure for table htmldocs
--

DROP TABLE IF EXISTS htmldocs;
CREATE TABLE htmldocs (
id_html_doc int(6) NOT NULL,
version int(11) default '0' NOT NULL,
content_label varchar(50) default '' NOT NULL,
creation_date date NOT NULL,
update_date date NOT NULL,
html_content LONG VARCHAR,
user varchar(100) default '' NOT NULL,
user_creator varchar(100) default '' NOT NULL,
attached_portlet_id int(6) NOT NULL,
PRIMARY KEY (id_html_doc)
);

--
-- Structure for table htmldocs_portlet
--

DROP TABLE IF EXISTS htmldocs_portlet;
CREATE TABLE htmldocs_portlet (
id_portlet int(6) NOT NULL,
name varchar(50) default '' NOT NULL,
content_id int(6) NOT NULL,
PRIMARY KEY (id_portlet)
);

--
-- Structure for table htmldocs_versions
--

DROP TABLE IF EXISTS htmldocs_versions;
CREATE TABLE htmldocs_versions (
id_version int(6) NOT NULL,
id_html_doc int(6) NOT NULL,
version int(11) default '0' NOT NULL,
content_label varchar(50) default '' NOT NULL,
creation_date date NOT NULL,
update_date date NOT NULL,
html_content LONG VARCHAR,
user varchar(100) default '' NOT NULL,
user_creator varchar(100) default '' NOT NULL,
attached_portlet_id int(6) NOT NULL,
PRIMARY KEY (id_version)
);
