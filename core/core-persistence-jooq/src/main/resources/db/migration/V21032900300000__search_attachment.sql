ALTER TABLE search_condition
  ADD COLUMN IF NOT EXISTS attachment_name_mask text NULL,
  ADD COLUMN IF NOT EXISTS has_attachments boolean NULL;
