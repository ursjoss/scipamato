/*
 * Add a text array for the codes of all code classes
 */

ALTER TABLE paper ADD COLUMN IF NOT EXISTS codes text[] NOT NULL DEFAULT '{}';

CREATE INDEX gin_idx_codes ON paper USING GIN (codes);

UPDATE paper SET codes = '{1F,2N,3C,4G,5H,5S,6M,7L,8O}' WHERE id = 1;
UPDATE paper SET codes = '{1F,1V,2N,3C,4D,4H,5H,6M,7L,8O}' WHERE id = 2;
UPDATE paper SET codes = '{2R,3A,3B,4C,5U,6M,7L}' WHERE id = 3;


/*
 * Add table code_class and fill the data
 *
 * Note: The corresponding table in the scipamato-core part is code_class_tr
 */
CREATE TABLE code_class (
  code_class_id integer NOT NULL,
  lang_code text NOT NULL,
  name text NOT NULL,
  description text NOT NULL,
  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  last_modified TIMESTAMP DEFAULT current_timestamp,

  PRIMARY KEY(code_class_id, lang_code)
);

INSERT INTO code_class VALUES (1, 'de', 'Schadstoffe', 'Schadstoffe, Einwirkung, Exposition', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (2, 'de', 'Region', 'Geographie (Europa, andere)', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (3, 'de', 'Kollektiv', 'Stichprobe, Kollektiv', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (4, 'de', 'Zielgrössen', 'Wirkungen, Zielgrössen', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (5, 'de', 'Studientyp', 'Art der Studie, der Publikation', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (6, 'de', 'Spezies', 'Spezies (Mensch, Tier, Pflanze etc.)', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (7, 'de', 'Zeitdauer', 'Zeit der Einwirkung (kurzfristig – langfristig)', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (8, 'de', 'Umgebung', 'Umgebung (berufliche Exposition, Aussenluft…)', 1, DEFAULT, DEFAULT);

INSERT INTO code_class VALUES (1, 'en', 'Exposure Agent', '', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (2, 'en', 'Region', '', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (3, 'en', 'Study Population', '', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (4, 'en', 'Health Outcome', '', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (5, 'en', 'Study Design', '', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (6, 'en', 'Species', '', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (7, 'en', 'Duration of Exposure', '', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (8, 'en', 'Setting', '', 1, DEFAULT, DEFAULT);

INSERT INTO code_class VALUES (1, 'fr', 'Polluant nocif', '', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (2, 'fr', 'Région', '', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (3, 'fr', 'Echantillons des personnes', '', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (4, 'fr', 'Effets physiologigue ou nocifs', '', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (5, 'fr', 'Typ d''étude', '', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (6, 'fr', 'Espèces investigées', '', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (7, 'fr', 'Durée de l''exposition', '', 1, DEFAULT, DEFAULT);
INSERT INTO code_class VALUES (8, 'fr', 'Site d''exposition', '', 1, DEFAULT, DEFAULT);


/*
 * Add table code and fill the data
 *
 * Note: The corresponding table in the scipamato-core part is code_tr.
 *       Important:
 *       - Internal codes in scipamato-core will not be synchronized and are left out here too.
 *       - Some internal codes are aggregated here (5A, 5B, 5C -> 5abc (lower case!))
 */
CREATE TABLE code (
  code text NOT NULL,
  lang_code text NOT NULL,
  code_class_id integer NOT NULL,
  name text NOT NULL,
  comment text NULL,
  sort integer NOT NULL,
  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  last_modified TIMESTAMP DEFAULT current_timestamp,

  PRIMARY KEY(code, lang_code)
);

ALTER TABLE code ADD CONSTRAINT idx_code UNIQUE (code_class_id, lang_code, code);

INSERT INTO code VALUES('1F', 'de', 1, 'Feinstaub, Partikel', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1A', 'de', 1, 'Ozon, Oxidantien', null, 2, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1B', 'de', 1, 'Stickoxide, Ammoniak', null, 3, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1C', 'de', 1, 'Kohlenwasserstoffe, nicht chloriert, VOC', null, 4, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1D', 'de', 1, 'Kohlenmonoxid', null, 5, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1E', 'de', 1, 'SO2, Schwefelverbindungen,', null, 6, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1G', 'de', 1, 'Metalle', null, 7, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1H', 'de', 1, 'Halogene, halogenierte Stoffe', null, 8, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1I', 'de', 1, 'Saure Aerosole', null, 9, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1K', 'de', 1, 'Radioaktive Stoffe', null, 10, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1L', 'de', 1, 'Asbest, Fasern', null, 11, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1M', 'de', 1, 'Tabakrauch, Passivrauchen', null, 12, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1P', 'de', 1, 'Pollen', null, 14, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1R', 'de', 1, 'Feuchtigkeit in der Raumluft', null, 15, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1S', 'de', 1, 'Milben, Allergene in den Raumluft', null, 16, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1T', 'de', 1, 'Temperatur', null, 17, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1V', 'de', 1, 'Verkehr', null, 19, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1Y', 'de', 1, 'Gerüche', null, 20, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('2R', 'de', 2, 'Europa', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('2N', 'de', 2, 'Übrige Länder', null, 2, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('3A', 'de', 3, 'Säuglinge, Vorschulkinder', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3B', 'de', 3, 'Schulkinder und Jugendliche', null, 2, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3C', 'de', 3, 'Erwachsene (alle)', null, 3, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3H', 'de', 3, 'Betagte Personen (65+ Jahre)', null, 4, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3D', 'de', 3, 'Personen mit Asthma oder Atemwegsallergie', null, 5, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3E', 'de', 3, 'Pers. mit anderer chronischer Lungenkrankheit, COPD', null, 6, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3F', 'de', 3, 'Registerdaten, Patienten', null, 7, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3G', 'de', 3, 'Schwangere Frauen', null, 8, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3I', 'de', 3, 'Sportler/innen', null, 9, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3O', 'de', 3, 'Beruflich Exponierte', null, 10, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3K', 'de', 3, 'Gewebe', null, 11, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3L', 'de', 3, 'Zellen', null, 12, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('4A', 'de', 4, 'Sterblichkeit', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4B', 'de', 4, 'Lungenfunktion', null, 2, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4C', 'de', 4, 'Symptome', null, 3, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4D', 'de', 4, 'Biochemische oder zelluläre Veränderungen', null, 4, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4E', 'de', 4, 'Atemwegskrankheiten akut', null, 5, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4F', 'de', 4, 'Atemwegskrankheiten chronisch', 'wie COPD/Asthma', 6, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4G', 'de', 4, 'Krebs', null, 7, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4H', 'de', 4, 'Herz, Kreislauf, Blut', null, 8, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4I', 'de', 4, 'Gehirn, Nerven, Augen, Ohren', null, 9, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4K', 'de', 4, 'Belästigung, psychische Folgen, Lebensqualität', null, 10, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4M', 'de', 4, 'Medikamentenkonsum, Konsultation, Spitaleintritt', null, 11, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4N', 'de', 4, 'Fertilität, Schwangerschaftsproblem, Abort', null, 12, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4O', 'de', 4, 'Frühgeburt, Säuglingsgewicht, pränatale Entwicklung', null, 13, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4L', 'de', 4, 'Missbildungen', null, 14, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('5abc', 'de', 5, 'Experimentelle Studiem', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5E', 'de', 5, 'Einmalige Exposition, Unfall, Brand', null, 4, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5F', 'de', 5, 'Zeitreihen, Panel, kurzfristige Längsstudie', null, 5, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5G', 'de', 5, 'Querschnitt, Fall-Kontrollstudie, deskriptiv', null, 6, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5H', 'de', 5, 'Kohortenstudie', null, 7, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5I', 'de', 5, 'Interventionsstudie', null, 8, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5M', 'de', 5, 'Messstudie, Studienmethodik', null, 9, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5S', 'de', 5, 'Statistik', null, 10, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5U', 'de', 5, 'Übersichten, Metaanalysen, HIA', null, 13, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('6M', 'de', 6, 'Mensch', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('6T', 'de', 6, 'Tier', null, 2, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('6O', 'de', 6, 'Viren, Bakterien, Einzeller', null, 3, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('7K', 'de', 7, 'Kurzfristig', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('7L', 'de', 7, 'Langfristig', null, 2, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('8I', 'de', 8, 'Raumluft', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('8O', 'de', 8, 'Aussenluft', null, 2, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('8P', 'de', 8, 'Individuelle Messung', null, 3, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('8W', 'de', 8, 'Berufliche Exposition', null, 4, 1, DEFAULT, DEFAULT);


INSERT INTO code VALUES('1F', 'en', 1, 'Particles, Particulate Matter', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1A', 'en', 1, 'Ozone, Oxidants', null, 2, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1B', 'en', 1, 'Nitrogen Oxides, Ammonia', null, 3, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1C', 'en', 1, 'Hydrocarbons, non-chlorinated, VOC', null, 4, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1D', 'en', 1, 'Carbonmonoxide', null, 5, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1E', 'en', 1, 'SO2, sulfur compounds', null, 6, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1G', 'en', 1, 'Metals', null, 7, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1H', 'en', 1, 'Halogens, halogenated substances', null, 8, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1I', 'en', 1, 'Acidic Aerosols', null, 9, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1K', 'en', 1, 'Radioactive substances', null, 10, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1L', 'en', 1, 'Asbestos, fibres', null, 11, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1M', 'en', 1, 'Tobacco smoke, passive smoking', null, 12, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1P', 'en', 1, 'Pollen', null, 14, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1R', 'en', 1, 'Indoor air humidity', null, 15, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1S', 'en', 1, 'Mites, indoor allergens', null, 16, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1T', 'en', 1, 'Temperature', null, 17, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1V', 'en', 1, 'Traffic', null, 19, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1Y', 'en', 1, 'Odours', null, 20, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('2R', 'en', 2, 'Europe', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('2N', 'en', 2, 'Other countries', null, 2, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('3A', 'en', 3, 'Newborns, infants, pre-school children', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3B', 'en', 3, 'School children, adolescents', null, 2, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3C', 'en', 3, 'Adults (19+)', null, 3, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3H', 'en', 3, 'Agend persons (65+)', null, 4, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3D', 'en', 3, 'People with asthma or respiratory allergies', null, 5, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3E', 'en', 3, 'People with chronic respiratory disease, COPD', null, 6, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3F', 'en', 3, 'Registry data, patient records', null, 7, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3G', 'en', 3, 'Pregnant women', null, 8, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3I', 'en', 3, 'Athletes', null, 9, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3O', 'en', 3, 'Ocupationally exposed', null, 10, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3K', 'en', 3, 'Tissue', null, 11, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3L', 'en', 3, 'Cells', null, 12, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('4A', 'en', 4, 'Mortality', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4B', 'en', 4, 'Lung function, pulmonary function', null, 2, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4C', 'en', 4, 'Symptoms', null, 3, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4D', 'en', 4, 'Biochemical or cellular responses', null, 4, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4E', 'en', 4, 'Respiratory diseases acute', null, 5, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4F', 'en', 4, 'Respiratory diseases chronic', 'like COPD/Asthma', 6, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4G', 'en', 4, 'Cancer', null, 7, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4H', 'en', 4, 'Heart, cardovascular system, blood', null, 8, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4I', 'en', 4, 'Brain, nervous system, eyes, ears', null, 9, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4K', 'en', 4, 'Annoyance, well-being, quality of life', null, 10, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4M', 'en', 4, 'Drug intake, consultation, hospital admission', null, 11, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4N', 'en', 4, 'Fertility, pregnancy complications, miscarriage', null, 12, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4O', 'en', 4, 'Preterm birth, birth weight, prenatal development', null, 13, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4L', 'en', 4, 'Congenital malformation', null, 14, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('5abc', 'en', 5, 'Experimental studies', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5E', 'en', 5, 'Accidental release, fire', null, 4, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5F', 'en', 5, 'Panel study,  time series', null, 5, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5G', 'en', 5, 'Cross-sectional or case-control study, descriptive', null, 6, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5H', 'en', 5, 'Cohort Study', null, 7, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5I', 'en', 5, 'Intervention', null, 8, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5M', 'en', 5, 'Methodological study, Exposure assessment, modelling', null, 9, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5S', 'en', 5, 'Statistics', null, 10, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5U', 'en', 5, 'Review, meta-analysis', null, 13, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('6M', 'en', 6, 'Humans', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('6T', 'en', 6, 'Animals', null, 2, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('6O', 'en', 6, 'Viruses, Bacteria, Protozoa', null, 3, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('7K', 'en', 7, 'Short-term', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('7L', 'en', 7, 'Long-term', null, 2, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('8I', 'en', 8, 'Indoor Air', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('8O', 'en', 8, 'Ambient air', null, 2, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('8P', 'en', 8, 'Personal measurement', null, 3, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('8W', 'en', 8, 'Occupational exposure', null, 4, 1, DEFAULT, DEFAULT);


INSERT INTO code VALUES('1F', 'fr', 1, 'Poussière fines, particules', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1A', 'fr', 1, 'Ozone, Oxydants', null, 2, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1B', 'fr', 1, 'Oxydes d''azote, ammoniac', null, 3, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1C', 'fr', 1, 'Hydrocarbures non-chlorurés', null, 4, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1D', 'fr', 1, 'Monoxyde de carbone', null, 5, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1E', 'fr', 1, 'SO2, composé de soufre', null, 6, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1G', 'fr', 1, 'Métals', null, 7, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1H', 'fr', 1, 'Halogènes, substances halogénées', null, 8, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1I', 'fr', 1, 'Aérosols acides', null, 9, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1K', 'fr', 1, 'Substances radioactives', null, 10, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1L', 'fr', 1, 'Amiante, fibres', null, 11, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1M', 'fr', 1, 'Tabagisme passif, fumée de cigarette', null, 12, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1P', 'fr', 1, 'Pollen', null, 14, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1R', 'fr', 1, 'Humidité (air intérieur)', null, 15, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1S', 'fr', 1, 'Allergènes dans l''air intérieur', null, 16, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1T', 'fr', 1, 'Température', null, 17, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1V', 'fr', 1, 'Trafic / circulation', null, 19, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('1Y', 'fr', 1, 'Odeur', null, 20, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('2R', 'fr', 2, 'Europe', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('2N', 'fr', 2, 'Autres pays', null, 2, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('3A', 'fr', 3, 'Nourisson, infants, enfants préscolaires', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3B', 'fr', 3, 'Enfants scolaires, adolescents', null, 2, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3C', 'fr', 3, 'Adultes (19+)', null, 3, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3H', 'fr', 3, 'Personnes agées (65+)', null, 4, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3D', 'fr', 3, 'Personnes asthmatiques ou allérgiques', null, 5, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3E', 'fr', 3, 'Pers. souffrantes des maladies pulmonaire chronique', null, 6, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3F', 'fr', 3, 'Données de registre ou des patients', null, 7, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3G', 'fr', 3, 'Femmes enceintes', null, 8, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3I', 'fr', 3, 'Sportifs', null, 9, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3O', 'fr', 3, 'Personnes exposées au travail', null, 10, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3K', 'fr', 3, 'Tissus', null, 11, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('3L', 'fr', 3, 'Cellules', null, 12, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('4A', 'fr', 4, 'Mortalité', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4B', 'fr', 4, 'Fonction pulmonaire', null, 2, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4C', 'fr', 4, 'Symptômes, Signes', null, 3, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4D', 'fr', 4, 'Changements biochimiques ou cellulaire', null, 4, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4E', 'fr', 4, 'Maladie respiratoire, imminante', null, 5, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4F', 'fr', 4, 'Maladie respiratoire, chronique', 'comme COPD/Asthma', 6, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4G', 'fr', 4, 'Cancer', null, 7, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4H', 'fr', 4, 'Coeur, circulation, sang', null, 8, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4I', 'fr', 4, 'Système nerveux, yeux, oreilles', null, 9, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4K', 'fr', 4, 'Nuisance, bien-être, qualité de vie', null, 10, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4M', 'fr', 4, 'Médicaments, visite médicale, hospitalisation', null, 11, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4N', 'fr', 4, 'Fertilité, complication de grossesse, fausse couche', null, 12, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4O', 'fr', 4, 'Naissance prématurée, poids de naissance ', null, 13, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('4L', 'fr', 4, 'Malformation', null, 14, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('5abc', 'fr', 5, 'Etudes expérimentales', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5E', 'fr', 5, 'Accident, incident chimique, feu', null, 4, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5F', 'fr', 5, 'Etude panel, étude longitudinale à court terme, séries chronologiques', null, 5, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5G', 'fr', 5, 'Etude transversal ou cas-témoin, descriptive', null, 6, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5H', 'fr', 5, 'Etude de cohorte', null, 7, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5I', 'fr', 5, 'Intervention', null, 8, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5M', 'fr', 5, 'Méthodologie', null, 9, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5S', 'fr', 5, 'Statistique ', null, 10, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('5U', 'fr', 5, 'Review, révue, méta-analyse', null, 13, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('6M', 'fr', 6, 'Hommes', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('6T', 'fr', 6, 'Animaux', null, 2, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('6O', 'fr', 6, 'Virus, Bactéries, Protozoaires', null, 3, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('7K', 'fr', 7, 'à court terme', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('7L', 'fr', 7, 'à long terme', null, 2, 1, DEFAULT, DEFAULT);

INSERT INTO code VALUES('8I', 'fr', 8, 'Air intérieur', null, 1, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('8O', 'fr', 8, 'Air extérieur', null, 2, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('8P', 'fr', 8, 'Mesures personalisées', null, 3, 1, DEFAULT, DEFAULT);
INSERT INTO code VALUES('8W', 'fr', 8, 'Exposition lié au travail', null, 4, 1, DEFAULT, DEFAULT);
