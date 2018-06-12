CREATE TABLE newsletter (
  id integer PRIMARY KEY NOT NULL,
  issue text NOT NULL,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  last_modified TIMESTAMP DEFAULT current_timestamp
);
ALTER TABLE newsletter ADD CONSTRAINT idx_issue UNIQUE (issue);

-- this table corresponds to newsletter_topic_tr in core.
-- hence id is not unique here (and thus only part of the compound primary key together with lang_code)
CREATE TABLE newsletter_topic (
  id integer NOT NULL,
  lang_code text NOT NULL,
  title text NOT NULL,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  last_modified TIMESTAMP DEFAULT current_timestamp
);
ALTER TABLE newsletter_topic ADD PRIMARY KEY (id, lang_code);


CREATE TABLE new_study_topic (
  id serial PRIMARY KEY NOT NULL,
  newsletter_id integer NOT NULL,
  newsletter_topic_id integer NOT NULL,
  sort integer NOT NULL DEFAULT 0,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  last_modified TIMESTAMP DEFAULT current_timestamp
);
ALTER TABLE new_study_topic ADD CONSTRAINT idx_new_study_topic UNIQUE (newsletter_id, newsletter_topic_id);
ALTER TABLE new_study_topic ADD FOREIGN KEY (newsletter_id) REFERENCES newsletter(id) ON DELETE CASCADE ON UPDATE CASCADE;
-- cannot put foreign key on newsletter_topic, as id is not unique

-- paper_number is unique in table but not PK. We need number not id. avoiding join but unable to use FK.
CREATE TABLE new_study (
  newsletter_id integer NOT NULL,
  new_study_topic_id int,
  sort integer NOT NULL DEFAULT 0,
  paper_number bigint NOT NULL,
  year integer NOT NULL,
  authors text NOT NULL,
  headline text,
  description text,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  last_modified TIMESTAMP DEFAULT current_timestamp
);
ALTER TABLE new_study ADD PRIMARY KEY (newsletter_id, paper_number);
ALTER TABLE new_study ADD FOREIGN KEY (new_study_topic_id) REFERENCES new_study_topic(id);