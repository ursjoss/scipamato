DROP TABLE IF EXISTS paper;

CREATE TABLE paper (
  id BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  doi VARCHAR,
  pm_id INT,
  authors VARCHAR NOT NULL,
  first_author VARCHAR NOT NULL,
  first_author_overridden BOOLEAN NOT NULL,
  title VARCHAR NOT NULL,
  location VARCHAR NOT NULL,
  publication_year INT,
  
  goals VARCHAR NOT NULL,
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
  comment VARCHAR,
  intern VARCHAR,

  main_code_of_codeclass1 CHAR(2) NULL,

  version INT DEFAULT 1,
  timestamp TIMESTAMP DEFAULT current_timestamp(),
);



DROP TABLE IF EXISTS code_class;

CREATE TABLE code_class (
  id INT NOT NULL PRIMARY KEY,

  version INT DEFAULT 1,
  timestamp TIMESTAMP DEFAULT current_timestamp(),
);

DROP TABLE IF EXISTS code_class_tr;

CREATE TABLE code_class_tr (
  id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  code_class_id INT NOT NULL,
  lang_code VARCHAR NOT NULL,
  name VARCHAR NOT NULL,
  description VARCHAR NOT NULL,

  version INT DEFAULT 1,
  timestamp TIMESTAMP DEFAULT current_timestamp(),
);

ALTER TABLE code_class_tr ADD FOREIGN KEY (code_class_id) REFERENCES code_class(id) on delete cascade on update cascade;

DROP INDEX IF EXISTS idx_code_class_tr_unique;
CREATE UNIQUE INDEX idx_code_class_tr_unique ON code_class_tr (code_class_id, lang_code);



DROP TABLE IF EXISTS code;

CREATE TABLE code (
  code CHAR(2) NOT NULL PRIMARY KEY,
  code_class_id INT NOT NULL,
  sort INT NOT NULL,

  version INT DEFAULT 1,
  timestamp TIMESTAMP DEFAULT current_timestamp(),
);

DROP INDEX IF EXISTS idx_code_unique;
CREATE UNIQUE INDEX idx_code_unique ON code (code_class_id, sort);

DROP TABLE IF EXISTS code_tr;

CREATE TABLE code_tr (
  id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  code CHAR(2) NOT NULL,
  lang_code VARCHAR NOT NULL,

  name VARCHAR NOT NULL,

  version INT DEFAULT 1,
  timestamp TIMESTAMP DEFAULT current_timestamp(),
);

ALTER TABLE code_tr ADD FOREIGN KEY (code) REFERENCES code(code) on delete cascade on update cascade;

DROP INDEX IF EXISTS idx_code_tr_unique;
CREATE UNIQUE INDEX idx_code_tr_unique ON code_tr (code, lang_code);



DROP TABLE IF EXISTS paper_code;

CREATE TABLE paper_code (
  paper_id BIGINT NOT NULL,
  code CHAR(2) NOT NULL,

  version INT DEFAULT 1,
  timestamp TIMESTAMP DEFAULT current_timestamp(),
);

ALTER TABLE paper_code ADD PRIMARY KEY (paper_id, code);
ALTER TABLE paper_code ADD FOREIGN KEY (paper_id) REFERENCES paper(id) on delete cascade on update cascade;
ALTER TABLE paper_code ADD FOREIGN KEY (code) REFERENCES code(code) on update cascade;

DROP INDEX IF EXISTS idx_paper_code;
CREATE UNIQUE INDEX idx_paper_code ON paper_code (paper_id, code);
