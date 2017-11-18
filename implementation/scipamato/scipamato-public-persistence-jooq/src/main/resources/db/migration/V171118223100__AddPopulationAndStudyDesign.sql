/*
 * Add two smallint arrays:
 * - Population (1: Children (Codes 3A+3B), 2: Adults (Codes 3C)
 * - Study Design (1: Experimental Studies (5A+5B+5C), 2: Epidemiolog. Studies (5E+5F+5G+5H+5I)), 3. Overview/Methodology (5U+5M)
 */

ALTER TABLE paper ADD COLUMN IF NOT EXISTS codes_population smallint[] NOT NULL DEFAULT '{}';
ALTER TABLE paper ADD COLUMN IF NOT EXISTS codes_study_design smallint[] NOT NULL DEFAULT '{}';

CREATE INDEX gin_idx_codes_population   ON paper USING GIN (codes_population);
CREATE INDEX gin_idx_codes_study_design ON paper USING GIN (codes_study_design);

UPDATE paper SET codes_population = '{2}', codes_study_design = '{2}' WHERE id = 1;
UPDATE paper SET codes_population = '{2}', codes_study_design = '{2}' WHERE id = 2;

/* Add a third record with different values for the two new fields */
INSERT INTO paper(ID, NUMBER, PM_ID, AUTHORS, TITLE, LOCATION, PUBLICATION_YEAR, GOALS, POPULATION, METHODS, RESULT, COMMENT, VERSION, CREATED, LAST_MODIFIED, codes_population, codes_study_design) VALUES
  (3, 3, null, 'Varonier HS.', 'Allergie respiratoire chez l''enfant et pollution atmospheric', 'Vortrag Gründungsversammlung AefU, 21.6.87', 1987, null, null, 'Allergie, Prävalenz von Heuschnupfen, Asthma bei Kindern von 4-5 und 15 Jahren im J
ahr 1967 und 1981 in Genf. Vortrag.', null, null, 1, TIMESTAMP '1993-12-31 01:00:00.000', TIMESTAMP '2015-01-22 01:00:00.000', '{1}', '{3}');
