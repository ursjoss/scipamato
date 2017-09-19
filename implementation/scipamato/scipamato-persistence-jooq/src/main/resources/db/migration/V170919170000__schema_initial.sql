/*
 * Creates the basic schema for database scipamato (PostgreSQL)
 */
 
/* 
 * TABLE paper
 */
CREATE TABLE paper (
  id bigserial PRIMARY KEY,
  number bigint NOT NULL,
  doi text,
  pm_id integer,
  authors text,
  first_author text,
  first_author_overridden boolean NOT NULL DEFAULT FALSE,
  title text,
  location text,
  publication_year integer,
  
  goals text,
  population text,
  population_place text,
  population_participants text,
  population_duration text,
  exposure_pollutant text,
  exposure_assessment text,
  methods text,
  method_study_design text,  
  method_outcome text,
  method_statistics text,
  method_confounders text,
  
  result text,
  result_exposure_range text,
  result_effect_estimate text,
  result_measured_outcome text,
  comment text,
  intern text,

  original_abstract text,

  main_code_of_codeclass1 char(2) NULL,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

ALTER TABLE paper ADD CONSTRAINT idx_number UNIQUE (number);

/* 
 * TABLE code_class with translation table code_class_tr
 */

CREATE TABLE code_class (
  id integer PRIMARY KEY,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);


CREATE TABLE code_class_tr (
  id serial PRIMARY KEY,
  code_class_id integer NOT NULL,
  lang_code text NOT NULL,
  name text NOT NULL,
  description text NOT NULL,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

ALTER TABLE code_class_tr ADD FOREIGN KEY (code_class_id) REFERENCES code_class(id) on delete cascade on update cascade;

CREATE UNIQUE INDEX idx_code_class_tr_unique ON code_class_tr (code_class_id, lang_code);


/* 
 * TABLE code with translation table code_tr
 */

CREATE TABLE code (
  code char(2) PRIMARY KEY,
  code_class_id integer NOT NULL,
  sort integer NOT NULL,
  internal boolean NOT NULL DEFAULT FALSE,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

CREATE UNIQUE INDEX idx_code_unique ON code (code_class_id, sort);


CREATE TABLE code_tr (
  id serial PRIMARY KEY,
  code char(2) NOT NULL,
  lang_code text NOT NULL,

  name text NOT NULL,
  comment text NULL,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

ALTER TABLE code_tr ADD FOREIGN KEY (code) REFERENCES code(code) on delete cascade on update cascade;

CREATE UNIQUE INDEX idx_code_tr_unique ON code_tr (code, lang_code);


/* 
 * TABLE paper_code
 */

CREATE TABLE paper_code (
  paper_id bigint NOT NULL,
  code char(2) NOT NULL,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

ALTER TABLE paper_code ADD PRIMARY KEY (paper_id, code);
ALTER TABLE paper_code ADD FOREIGN KEY (paper_id) REFERENCES paper(id) on delete cascade on update cascade;
ALTER TABLE paper_code ADD FOREIGN KEY (code) REFERENCES code(code) on update cascade;

CREATE UNIQUE INDEX idx_paper_code ON paper_code (paper_id, code);


/* 
 * TABLE search_order
 *         search_exclusion
 *         search_condition
 *            search_term
 */

CREATE TABLE search_order (
  id bigserial PRIMARY KEY,
  name text NULL,
  owner integer DEFAULT 1,
  global boolean NOT NULL DEFAULT FALSE,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);


CREATE TABLE search_exclusion (
  id bigserial PRIMARY KEY,
  search_order_id bigint NOT NULL,
  paper_id bigint NOT NULL,
  comment text NULL,
  
  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

ALTER TABLE search_exclusion ADD FOREIGN KEY (search_order_id) REFERENCES search_order(id) on delete cascade on update cascade;
ALTER TABLE search_exclusion ADD FOREIGN KEY (paper_id) REFERENCES paper(id) on delete cascade on update cascade;

CREATE UNIQUE INDEX idx_search_exclusion_unique ON search_exclusion (search_order_id, paper_id);

CREATE TABLE search_condition (
  search_condition_id bigserial PRIMARY KEY,
  search_order_id bigint NOT NULL,
  created_term text NULL,
  modified_term text NULL,
  
  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

ALTER TABLE search_condition ADD FOREIGN KEY (search_order_id) REFERENCES search_order(id) on delete cascade on update cascade;


CREATE TABLE search_term (
  id bigserial PRIMARY KEY,
  search_condition_id bigint NOT NULL,
  search_term_type integer NOT NULL,
  field_name text NOT NULL,
  raw_value text NOT NULL,
  
  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

ALTER TABLE search_term ADD FOREIGN KEY (search_condition_id) REFERENCES search_condition(search_condition_id) on delete cascade on update cascade;


/*
 * TABLE search_condition_code
 */
 
CREATE TABLE search_condition_code (
  search_condition_id bigint NOT NULL,
  code char(2) NOT NULL,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

ALTER TABLE search_condition_code ADD PRIMARY KEY (search_condition_id, code);
ALTER TABLE search_condition_code ADD FOREIGN KEY (search_condition_id) REFERENCES search_condition(search_condition_id) on delete cascade on update cascade;
ALTER TABLE search_condition_code ADD FOREIGN KEY (code) REFERENCES code(code) on update cascade;

CREATE UNIQUE INDEX idx_search_condition_code_unique ON search_condition_code (search_condition_id, code);

/*
 * TABLE scipamato_user
 */

CREATE TABLE scipamato_user (
  id serial PRIMARY KEY,
  user_name varchar(30) NOT NULL,
  first_name text NOT NULL,
  last_name text NOT NULL,
  email text NOT NULL,
  password text NOT NULL,
  enabled boolean NOT NULL DEFAULT FALSE,
  
  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

CREATE UNIQUE INDEX idx_scipamato_user_username ON scipamato_user (user_name);


/*
 * TABLE user_role
 */

CREATE TABLE user_role (
  id serial PRIMARY KEY,
  user_id integer NOT NULL,
  role_id integer NOT NULL,
  
  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  created_by integer DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_modified_by integer DEFAULT 1
);

ALTER TABLE user_role ADD FOREIGN KEY (user_id) REFERENCES scipamato_user(id) on delete cascade on update cascade;

CREATE UNIQUE INDEX idx_user_role_unique ON user_role (user_id, role_id);

/*
 * TABLE attachment
 */

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
