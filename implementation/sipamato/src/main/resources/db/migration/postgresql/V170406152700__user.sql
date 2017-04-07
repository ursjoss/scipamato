/*
 * Creates the user 'sipamato' and grants it the relevant privileges
 * while revoking public preveleges from PUBLIC.
 */

DO
$body$
BEGIN
   IF NOT EXISTS (
      SELECT 1
      FROM   pg_catalog.pg_user
      WHERE  usename = 'sipamato') THEN

      CREATE USER sipamato WITH PASSWORD 'sipamato';
   END IF;
END
$body$;

/* grant connect to sipamato but not to PUBLIC */
REVOKE CONNECT ON DATABASE sipamato FROM PUBLIC;
GRANT CONNECT  ON DATABASE sipamato TO sipamato;

/* grant CRUD privileges (for all existing tables as well as sequences) to sipamato but not PUBLIC */
REVOKE ALL ON ALL TABLES IN SCHEMA public FROM PUBLIC;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO sipamato;
GRANT SELECT, USAGE ON ALL SEQUENCES IN SCHEMA public TO sipamato;

/* grant CRUD privileges (for all future tables as well as sequences) to sipamato but not PUBLIC */
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO sipamato;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, USAGE ON SEQUENCES TO sipamato;
