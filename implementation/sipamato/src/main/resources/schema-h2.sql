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
  
  version INT,
  timestamp TIMESTAMP,
);