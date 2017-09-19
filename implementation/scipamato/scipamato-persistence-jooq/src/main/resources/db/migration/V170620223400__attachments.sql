CREATE TABLE paper_attachment(
  id serial PRIMARY KEY,
  paper_id bigint NOT NULL,
  name text NOT NULL,
  content bytea NOT NULL,
  content_type text NOT NULL,
  size bigint NULL,
  
  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

ALTER TABLE paper_attachment ADD FOREIGN KEY (paper_id) REFERENCES paper(id) on delete cascade on update cascade;
CREATE UNIQUE INDEX idx_paper_attachment_unique ON paper_attachment (paper_id, name);
