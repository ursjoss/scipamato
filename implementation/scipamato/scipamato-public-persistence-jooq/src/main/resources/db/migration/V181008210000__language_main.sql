ALTER TABLE language
  ADD COLUMN IF NOT EXISTS main_language boolean NOT NULL DEFAULT FALSE;

DO
$body$
BEGIN
  IF NOT EXISTS(
      SELECT 1 FROM language WHERE main_language = true
  )
  THEN
    UPDATE language SET main_language = TRUE where code = 'de';
  END IF;
END
$body$;