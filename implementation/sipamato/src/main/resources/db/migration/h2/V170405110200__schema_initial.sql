CREATE TABLE IF NOT EXISTS paper (
  id BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  doi VARCHAR,
  pm_id INT,
  authors VARCHAR,
  first_author VARCHAR,
  first_author_overridden BOOLEAN NOT NULL DEFAULT 0,
  title VARCHAR,
  location VARCHAR,
  publication_year INT,
  
  goals VARCHAR,
  population VARCHAR,
  population_place VARCHAR,
  population_participants VARCHAR,
  population_duration VARCHAR,
  exposure_pollutant VARCHAR,
  exposure_assessment VARCHAR,
  methods VARCHAR,
  method_study_design VARCHAR,
  method_outcome VARCHAR,
  method_statistics VARCHAR,
  method_confounders VARCHAR,
  
  result VARCHAR,
  result_exposure_range VARCHAR,
  result_effect_estimate VARCHAR,
  result_measured_outcome VARCHAR,
  comment VARCHAR,
  intern VARCHAR,

  original_abstract VARCHAR,

  main_code_of_codeclass1 CHAR(2) NULL,

  version INT DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp(),
  created_by INT DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp(),
  last_modified_by INT DEFAULT 1,
);


CREATE TABLE IF NOT EXISTS code_class (
  id INT NOT NULL PRIMARY KEY,

  version INT DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp(),
  created_by INT DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp(),
  last_modified_by INT DEFAULT 1,
);

CREATE TABLE IF NOT EXISTS code_class_tr (
  id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  code_class_id INT NOT NULL,
  lang_code VARCHAR NOT NULL,
  name VARCHAR NOT NULL,
  description VARCHAR NOT NULL,

  version INT DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp(),
  created_by INT DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp(),
  last_modified_by INT DEFAULT 1,

  FOREIGN KEY (code_class_id) REFERENCES code_class(id) on delete cascade on update cascade
);

DROP INDEX IF EXISTS idx_code_class_tr_unique;
CREATE UNIQUE INDEX idx_code_class_tr_unique ON code_class_tr (code_class_id, lang_code);


CREATE TABLE IF NOT EXISTS code (
  code CHAR(2) NOT NULL PRIMARY KEY,
  code_class_id INT NOT NULL,
  sort INT NOT NULL,
  internal BOOLEAN NOT NULL DEFAULT 0,

  version INT DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp(),
  created_by INT DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp(),
  last_modified_by INT DEFAULT 1,
);

DROP INDEX IF EXISTS idx_code_unique;
CREATE UNIQUE INDEX idx_code_unique ON code (code_class_id, sort);


CREATE TABLE IF NOT EXISTS code_tr (
  id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  code CHAR(2) NOT NULL,
  lang_code VARCHAR NOT NULL,

  name VARCHAR NOT NULL,
  comment VARCHAR NULL,

  version INT DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp(),
  created_by INT DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp(),
  last_modified_by INT DEFAULT 1,

  FOREIGN KEY (code) REFERENCES code(code) on delete cascade on update cascade
);

DROP INDEX IF EXISTS idx_code_tr_unique;
CREATE UNIQUE INDEX idx_code_tr_unique ON code_tr (code, lang_code);


CREATE TABLE IF NOT EXISTS paper_code (
  paper_id BIGINT NOT NULL,
  code CHAR(2) NOT NULL,

  version INT DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp(),
  created_by INT DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp(),
  last_modified_by INT DEFAULT 1,

  FOREIGN KEY (paper_id) REFERENCES paper(id) on delete cascade on update cascade,
  FOREIGN KEY (code) REFERENCES code(code) on update cascade,

  PRIMARY KEY (paper_id, code)
);

DROP INDEX IF EXISTS idx_paper_code;
CREATE UNIQUE INDEX idx_paper_code ON paper_code (paper_id, code);


CREATE TABLE IF NOT EXISTS search_order (
  id BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  name VARCHAR NULL,
  owner INT DEFAULT 1,
  global BOOLEAN NOT NULL DEFAULT false,

  version INT DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp(),
  created_by INT DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp(),
  last_modified_by INT DEFAULT 1,
);

CREATE TABLE IF NOT EXISTS search_exclusion (
  id BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  search_order_id BIGINT NOT NULL,
  paper_id BIGINT NOT NULL,
  comment VARCHAR NULL,
  
  version INT DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp(),
  created_by INT DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp(),
  last_modified_by INT DEFAULT 1,

  FOREIGN KEY (search_order_id) REFERENCES search_order(id) on delete cascade on update cascade,
  FOREIGN KEY (paper_id) REFERENCES paper(id) on delete cascade on update cascade
);


CREATE TABLE IF NOT EXISTS search_condition (
  search_condition_id BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  search_order_id BIGINT NOT NULL,
  created_term VARCHAR NULL,
  modified_term VARCHAR NULL,
  
  version INT DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp(),
  created_by INT DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp(),
  last_modified_by INT DEFAULT 1,

  FOREIGN KEY (search_order_id) REFERENCES search_order(id) on delete cascade on update cascade
);


CREATE TABLE IF NOT EXISTS search_term (
  id BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  search_condition_id BIGINT NOT NULL,
  search_term_type INT NOT NULL,
  field_name VARCHAR NOT NULL,
  raw_value VARCHAR NOT NULL,
  
  version INT DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp(),
  created_by INT DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp(),
  last_modified_by INT DEFAULT 1,

  FOREIGN KEY (search_condition_id) REFERENCES search_condition(search_condition_id) on delete cascade on update cascade
);


CREATE TABLE IF NOT EXISTS search_condition_code (
  search_condition_id BIGINT NOT NULL,
  code CHAR(2) NOT NULL,

  version INT DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp(),
  created_by INT DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp(),
  last_modified_by INT DEFAULT 1,

  FOREIGN KEY (search_condition_id) REFERENCES search_condition(search_condition_id) on delete cascade on update cascade,
  FOREIGN KEY (code) REFERENCES code(code) on update cascade,

  PRIMARY KEY (search_condition_id, code)
);


CREATE TABLE IF NOT EXISTS sipamato_user (
  id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  user_name VARCHAR(30) NOT NULL,
  first_name VARCHAR NOT NULL,
  last_name VARCHAR NOT NULL,
  email VARCHAR NOT NULL,
  password VARCHAR NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT false,
  
  version INT DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp(),
  created_by INT DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp(),
  last_modified_by INT DEFAULT 1,
);

DROP INDEX IF EXISTS idx_sipamato_user_username;
CREATE UNIQUE INDEX idx_sipamato_user_username ON sipamato_user (user_name);


CREATE TABLE IF NOT EXISTS user_role (
  id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  
  version INT DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp(),
  created_by INT DEFAULT 1,
  last_modified TIMESTAMP DEFAULT current_timestamp(),
  last_modified_by INT DEFAULT 1,

  FOREIGN KEY (user_id) REFERENCES sipamato_user(id) on delete cascade on update cascade
);