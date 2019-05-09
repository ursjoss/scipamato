/*
 * TABLE keyword with translated keywords in keyword_tr
 */

CREATE TABLE keyword (
  id               serial PRIMARY KEY,

  search_override  text NULL,

  version          integer   DEFAULT 1,
  created          TIMESTAMP DEFAULT current_timestamp,
  created_by       integer   DEFAULT 1,
  last_modified    TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer   DEFAULT 1
);

CREATE TABLE keyword_tr (
  id               serial PRIMARY KEY,

  keyword_id       integer NOT NULL,
  lang_code        text    NOT NULL,

  name             text    NOT NULL,

  version          integer   DEFAULT 1,
  created          TIMESTAMP DEFAULT current_timestamp,
  created_by       integer   DEFAULT 1,
  last_modified    TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer   DEFAULT 1
);

CREATE UNIQUE INDEX idx_keyword_tr
  ON keyword_tr (keyword_id, lang_code, name);

CREATE UNIQUE INDEX idx_keyword_tr_unique
  ON keyword_tr (lang_code, name);

ALTER TABLE keyword_tr
  ADD FOREIGN KEY (keyword_id) REFERENCES keyword (id) on delete cascade on update cascade;
ALTER TABLE keyword_tr
  ADD FOREIGN KEY (lang_code) REFERENCES language (code) on delete cascade on update cascade;