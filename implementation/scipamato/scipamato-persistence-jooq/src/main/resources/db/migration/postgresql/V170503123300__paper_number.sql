/*
 * creates the new paper field number which should be not null and unique
 */
ALTER TABLE paper ADD COLUMN IF NOT EXISTS number bigint;

UPDATE paper SET number = id WHERE number IS NULL;

ALTER TABLE paper ALTER COLUMN number SET NOT NULL;

ALTER TABLE paper ADD CONSTRAINT idx_number UNIQUE (number);