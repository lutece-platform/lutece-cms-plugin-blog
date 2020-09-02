--
-- Structure of table blog_blog_content
--

DROP TABLE IF EXISTS blog_blog_content;
CREATE TABLE blog_blog_content (
id_document int NOT NULL,
id_blog int NOT NULL,
CONSTRAINT fk_id_blog_blog FOREIGN KEY(id_blog) references blog_blog(id_blog),
PRIMARY KEY (id_document, id_blog)
);

--
-- Copy id's of blog and document into blog_blog_content
--

INSERT INTO blog_blog_content (id_blog,id_document) SELECT a.id_blog, id_document from blog_content a, blog_blog b where b.id_blog = a.id_blog;

--
-- Delete column id_blog from table blog_content
--

ALTER TABLE blog_content DROP COLUMN id_blog;

--
-- Add column priority to table blog_blog_content
--

ALTER TABLE blog_blog_content ADD COLUMN priority INT DEFAULT 0 NOT NULL;

--
-- Set priority for existing data in blog_blog_content
--
CREATE TEMPORARY TABLE IF NOT EXISTS blog_blog_content_priority AS
SELECT a.id_document, a.id_blog, count(*) as priority 
FROM blog_blog_content a
JOIN blog_blog_content b ON a.id_blog = b.id_blog 
AND a.id_document >= b.id_document
GROUP BY a.id_blog, a.id_document;
TRUNCATE blog_blog_content;
INSERT INTO blog_blog_content SELECT * FROM  blog_blog_content_priority;

