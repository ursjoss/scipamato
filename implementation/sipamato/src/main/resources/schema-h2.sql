DROP TABLE IF EXISTS paper;

DROP SEQUENCE IF EXISTS s_paper_id;
CREATE SEQUENCE s_paper_id START WITH 1;

CREATE TABLE paper (
  id INT NOT NULL,
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
  exposure VARCHAR,
  exposure_pollutant VARCHAR,
  exposure_assessment VARCHAR,
  methods VARCHAR,
  method_outcome VARCHAR,
  method_statistics VARCHAR,
  method_confounders VARCHAR,
  
  result VARCHAR,
  result_exposure_range VARCHAR,
  result_effect_estimate VARCHAR,
  comment VARCHAR,
  intern VARCHAR,
  
  version INT,
  timestamp TIMESTAMP,

  CONSTRAINT pk_paper PRIMARY KEY (ID)
);