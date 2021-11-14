ALTER TABLE search_condition
    ADD COLUMN IF NOT EXISTS codes_excluded text NULL;
