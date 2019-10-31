-- Fix issues introduced in V180814143000__ref_integrity.sql
--
--  remove accidentially duplicated foreign key constraint on new_study_topic
ALTER TABLE new_study_topic
  DROP CONSTRAINT IF EXISTS new_study_topic_newsletter_id_fkey1;

-- replace non cascading foreign key on new_study_topic with cascading one
ALTER TABLE new_study
  DROP CONSTRAINT new_study_newsletter_id_fkey,
  ADD CONSTRAINT new_study_new_study_topic_newsletter_id_newsletter_topic_id_fkey
FOREIGN KEY (newsletter_id, newsletter_topic_id)
REFERENCES new_study_topic (newsletter_id, newsletter_topic_id)
ON DELETE CASCADE ON UPDATE CASCADE;