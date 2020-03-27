--
-- Structure of table blog_blog_content
--

DROP TABLE IF EXISTS blog_blog_content;
CREATE TABLE blog_blog_content (
id_blog int NOT NULL,
id_document int NOT NULL,
PRIMARY KEY (id_blog, id_document),
INDEX fk_blog (id_blog)
);

--
-- Copy id's of blog and document into blog_blog_content
--

insert into blog_blog_content (id_blog,id_document) 
SELECT a.id_blog, id_document from blog_content a, blog_blog b where b.id_blog = a.id_blog;

--
-- Delete column id_blog from table blog_content
--

ALTER TABLE blog_content
DROP COLUMN id_blog;