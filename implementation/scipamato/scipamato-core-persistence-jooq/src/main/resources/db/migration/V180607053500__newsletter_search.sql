/**
 * Adds two columns for searching by newsletter topic or headline.
 *
 * In order to maintain a neat column order, we're creating a new table and copy
 * the previous tables data into it. The previous table is renamed first and dropped after the operation.
 */

ALTER TABLE search_condition RENAME TO _old_search_condition;
ALTER SEQUENCE search_condition_search_condition_id_seq RENAME TO search_condition_search_condition_id_seq1;

CREATE TABLE search_condition (
  search_condition_id bigserial PRIMARY KEY,
  search_order_id bigint NOT NULL,
  created_term text NULL,
  modified_term text NULL,
  newsletter_topic_id int  NULL,
  newsletter_headline text NULL,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

ALTER TABLE search_condition ADD FOREIGN KEY (search_order_id) REFERENCES search_order(id) on delete cascade on update cascade;

INSERT INTO search_condition(
    search_condition_id, search_order_id, created_term, modified_term, version, created, created_by, last_modified, last_modified_by
)
SELECT
    search_condition_id, search_order_id, created_term, modified_term, version, created, created_by, last_modified, last_modified_by
FROM _old_search_condition;

SELECT setval(pg_get_serial_sequence('search_condition', 'search_condition_id'), coalesce(max(search_condition_id), 0)+1 , false)
FROM search_condition;

ALTER TABLE search_term           DROP CONSTRAINT IF EXISTS           search_term_search_condition_id_fkey;
ALTER TABLE search_condition_code DROP CONSTRAINT IF EXISTS search_condition_code_search_condition_id_fkey;

ALTER TABLE search_term           ADD FOREIGN KEY (search_condition_id)
  REFERENCES search_condition(search_condition_id) on delete cascade on update cascade;
ALTER TABLE search_condition_code ADD FOREIGN KEY (search_condition_id)
  REFERENCES search_condition(search_condition_id) on delete cascade on update cascade;

DROP TABLE _old_search_condition CASCADE;

