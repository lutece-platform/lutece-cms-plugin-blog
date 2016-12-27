  
--
-- Structure for table htmldocs_portlet
--
DROP TABLE IF EXISTS htmldocs_portlet;
CREATE TABLE htmldocs_portlet (
  id_portlet int default '0' NOT NULL,
  htmldocs_feed_id varchar(100) default NULL,
  PRIMARY KEY  (id_portlet)
);
