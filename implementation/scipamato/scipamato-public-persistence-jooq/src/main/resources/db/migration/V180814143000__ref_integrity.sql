-- Table language - add foreign keys on code, clode_class and newsletter_topic
-------------------
CREATE TABLE language(
  code text PRIMARY KEY
);

INSERT INTO language (code)
VALUES ('de'), ('en'), ('fr');

ALTER TABLE             code ADD FOREIGN KEY (lang_code) REFERENCES language(code) on delete cascade on update cascade;
ALTER TABLE       code_class ADD FOREIGN KEY (lang_code) REFERENCES language(code) on delete cascade on update cascade;
ALTER TABLE newsletter_topic ADD FOREIGN KEY (lang_code) REFERENCES language(code) on delete cascade on update cascade;


-- Table new_study_topic with foreign key on newsletter - cleanse data in case there are violations
-------------------
DELETE FROM new_study_topic
WHERE newsletter_id NOT IN (SELECT id FROM newsletter);

ALTER TABLE new_study_topic
ADD FOREIGN KEY (newsletter_id)
REFERENCES newsletter(id) on delete cascade on update cascade;


-- Table new_study with foreign key on newsletter - cleanse data in case there are violations
-------------------
DELETE FROM new_study
WHERE paper_number NOT IN (SELECT number FROM paper);

ALTER TABLE new_study
ADD FOREIGN KEY (paper_number)
REFERENCES paper(number) on delete cascade on update cascade;