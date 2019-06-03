CREATE TABLE newsletter_newsletter_topic (
  newsletter_id       integer NOT NULL,
  newsletter_topic_id integer NOT NULL,
  sort                integer NOT NULL,

  version             integer   DEFAULT 1,
  created             TIMESTAMP DEFAULT current_timestamp,
  created_by          integer   DEFAULT 1,
  last_modified       TIMESTAMP DEFAULT current_timestamp,
  last_modified_by    integer   DEFAULT 1
);


ALTER TABLE newsletter_newsletter_topic
  ADD FOREIGN KEY (newsletter_id) REFERENCES newsletter (id) on delete cascade on update cascade;
ALTER TABLE newsletter_newsletter_topic
  ADD FOREIGN KEY (newsletter_topic_id) REFERENCES newsletter_topic (id) on delete cascade on update cascade;

ALTER TABLE newsletter_newsletter_topic
  ADD PRIMARY KEY (newsletter_id, newsletter_topic_id);