CREATE TABLE newsletter(
  id serial PRIMARY KEY,
  issue text NOT NULL,
  issue_date date NULL,
  publication_status integer NOT NULL,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

CREATE UNIQUE INDEX idx_newsletter_issue_unique      ON newsletter (issue);
CREATE UNIQUE INDEX idx_newsletter_issue_date_unique ON newsletter (issue_date);


CREATE TABLE newsletter_topic(
  id serial PRIMARY KEY,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

CREATE TABLE newsletter_topic_tr(
  id serial PRIMARY KEY,
  newsletter_topic_id integer NOT NULL,
  lang_code text NOT NULL,
  title text NOT NULL,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

ALTER TABLE newsletter_topic_tr ADD FOREIGN KEY (newsletter_topic_id) REFERENCES newsletter_topic(id) on delete cascade on update cascade;

CREATE UNIQUE INDEX newsletter_topic_tr_unique ON newsletter_topic_tr (newsletter_topic_id, lang_code);


CREATE TABLE paper_newsletter(
  paper_id bigint not null,
  newsletter_id int not null,
  newsletter_topic_id int null,
  headline text null,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

ALTER TABLE paper_newsletter ADD PRIMARY KEY (newsletter_id, paper_id);
ALTER TABLE paper_newsletter ADD FOREIGN KEY (paper_id)            REFERENCES paper(id)            on update cascade;
ALTER TABLE paper_newsletter ADD FOREIGN KEY (newsletter_id)       REFERENCES newsletter(id)       on update cascade on delete cascade;
ALTER TABLE paper_newsletter ADD FOREIGN KEY (newsletter_topic_id) REFERENCES newsletter_topic(id) on update cascade;

CREATE UNIQUE INDEX idx_paper_newsletter_unique ON paper_newsletter(paper_id, newsletter_id);
