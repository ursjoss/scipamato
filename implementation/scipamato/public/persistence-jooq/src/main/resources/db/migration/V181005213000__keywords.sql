/*
 * Add table keyword
 *
 * Note: This table is a mixture of the core tables keyword and keyword_tr.
 *       id         is core.keyword_tr.id
 *       keyword_id is core.keyword.id
 */
CREATE TABLE keyword (
  id              integer   NOT NULL PRIMARY KEY,
  keyword_id      integer   NOT NULL,
  lang_code       text      NOT NULL,
  name            text      NOT NULL,
  version         integer            DEFAULT 1,
  created         TIMESTAMP          DEFAULT current_timestamp,
  last_modified   TIMESTAMP          DEFAULT current_timestamp,
  last_synched    TIMESTAMP NOT NULL DEFAULT current_timestamp,
  search_override text      NULL,

  CONSTRAINT idx_keyword_unique UNIQUE (keyword_id, lang_code, name),
  CONSTRAINT idx_keyword_unique2 UNIQUE (lang_code, name),
  FOREIGN KEY (lang_code) REFERENCES language (code) ON DELETE CASCADE ON UPDATE CASCADE
);
