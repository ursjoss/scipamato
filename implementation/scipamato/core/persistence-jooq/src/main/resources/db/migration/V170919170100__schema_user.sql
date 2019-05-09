/*
 * Creates the user 'scipamato' and grants it the relevant privileges
 * while revoking public privileges from PUBLIC.
 */

DO
$body$
BEGIN
   IF NOT EXISTS (
      SELECT 1
      FROM   pg_catalog.pg_user
      WHERE  usename = 'scipamato') THEN

      CREATE USER scipamato WITH PASSWORD 'scipamato';
   END IF;
END
$body$;

/* grant connect to scipamato but not to PUBLIC */
REVOKE CONNECT ON DATABASE scipamato FROM PUBLIC;
GRANT CONNECT  ON DATABASE scipamato TO scipamato;

/* grant CRUD privileges (for all existing tables as well as sequences) to scipamato but not PUBLIC */
REVOKE ALL ON ALL TABLES IN SCHEMA public FROM PUBLIC;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO scipamato;
GRANT SELECT, USAGE ON ALL SEQUENCES IN SCHEMA public TO scipamato;

/* grant CRUD privileges (for all future tables as well as sequences) to scipamato but not PUBLIC */
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO scipamato;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, USAGE ON SEQUENCES TO scipamato;
