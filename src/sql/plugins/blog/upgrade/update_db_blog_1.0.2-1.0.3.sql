
--
-- Structure for table blog_blog_content
--

ALTER TABLE blog_content
DROP COLUMN id_blog;

DROP TABLE IF EXISTS blog_blog_content;
CREATE TABLE blog_blog_content (
id_blog int NOT NULL,
id_document int NOT NULL,
PRIMARY KEY (id_blog, id_document),
INDEX fk_blog (id_blog)
);