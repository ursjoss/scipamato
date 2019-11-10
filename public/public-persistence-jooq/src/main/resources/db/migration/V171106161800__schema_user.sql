/*
 * Creates the user 'scipamatopub' and grants it the read privileges
 * while revoking public privileges from PUBLIC.
 */

DO
$body$
BEGIN
   IF NOT EXISTS (
      SELECT 1
      FROM   pg_catalog.pg_user
      WHERE  usename = 'scipamatopub') THEN

      CREATE USER scipamatopub WITH PASSWORD 'scipamatopub';
   END IF;
END
$body$;

/* grant connect to scipamatopub but not to PUBLIC */
REVOKE CONNECT ON DATABASE scipamato_public FROM PUBLIC;
GRANT CONNECT  ON DATABASE scipamato_public TO scipamatopub;

/* grant read privileges (for all existing tables as well as sequences) to scipamatopub but not PUBLIC */
REVOKE ALL ON ALL TABLES IN SCHEMA public FROM PUBLIC;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO scipamatopub;

/* grant read privileges (for all future tables as well as sequences) to scipamatopub but not PUBLIC */
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO scipamatopub;
