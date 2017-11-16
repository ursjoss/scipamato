/*
 * Creates the basic schema for database scipamato_public (PostgreSQL)
 */
 
/* 
 * TABLE paper
 */
CREATE TABLE paper (
  id bigint PRIMARY KEY,
  number bigint NOT NULL,
  pm_id integer,
  authors text,
  title text,
  location text,
  publication_year integer,
  
  goals text,
  methods text,
  population text,
  result text,
  comment text,
  
  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  last_modified TIMESTAMP DEFAULT current_timestamp
);

ALTER TABLE paper ADD CONSTRAINT idx_number UNIQUE (number);
