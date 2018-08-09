CREATE TABLE language(
  code text PRIMARY KEY,
  description text NOT NULL UNIQUE,
  main_language boolean NOT NULL DEFAULT FALSE,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

INSERT INTO language (code, description, main_language)
VALUES ('de', 'deutsch', TRUE), ('en', 'English', FALSE), ('fr', 'francais', FALSE);

ALTER TABLE             code_tr ADD FOREIGN KEY (lang_code) REFERENCES language(code) on delete cascade on update cascade;
ALTER TABLE       code_class_tr ADD FOREIGN KEY (lang_code) REFERENCES language(code) on delete cascade on update cascade;
ALTER TABLE newsletter_topic_tr ADD FOREIGN KEY (lang_code) REFERENCES language(code) on delete cascade on update cascade;