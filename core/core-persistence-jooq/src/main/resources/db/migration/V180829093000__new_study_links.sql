CREATE TABLE new_study_page_link (
  lang_code     text    NOT NULL,
  sort          integer NOT NULL,
  title         text    NOT NULL,
  url           text    NOT NULL,
  version       integer   DEFAULT 1,
  created       TIMESTAMP DEFAULT current_timestamp,
  last_modified TIMESTAMP DEFAULT current_timestamp,

  PRIMARY KEY (lang_code, sort)
);

ALTER TABLE new_study_page_link ADD FOREIGN KEY (lang_code) REFERENCES language(code) on delete cascade on update cascade;
