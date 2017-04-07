# sipamato

SiPaMaTo (Scientific Paper Management Tool)

# Elevator Pitch

TODO

# Build and run

## Supported Databases

Currently, the project set up supports two databases:

* H2
* PostgreSQL

The advantage of the former during the early development phase: The project is self-contained with the jar file produced by maven. There's no need to install a relational database management system on the test users machines.

Eventually, the production server will most probably run PostgreSQL - most probably I'll discontinue dragging H2 along to make the project setup simpler. But that's the future...

## Consequence of using [jOOQ](https://www.jooq.org/)

The jOOQ code generator plugin needs to run in the maven goal _generate_. Even more, it needs to have any Flyway database migrations already completed before the code generator starts. Both need to know database specific properties (url, driver, dialect, Flyway migration scripts etc.). This DB specific setup partially takes place in the `pom.xml` itself and partially in db-specific application properties files. The mechanism to specify database type and therefore the relevant property file is through spring and maven profiles. As the tests that usually run with maven also need to know the spring profile, you need to specify the spring profile on the maven command line. The maven profile is activated based on the spring profile. The relevant properties files are:

* application.properties (for database independent settings)
* application-h2.properties (sourced if the spring profile `h2` is active)
* application-postgres.properties (sourced if the spring profile `postgres` is active)

## Using H2 as database

### Building the jar

`mvn clean install -Dspring.profiles.active=h2`

This creates an H2 database in the project root (`sipamato.mv.db`), runs all H2-Flyway scripts to create the tables in the schema `PUBLIC` and inserts the test data. It then runs the jOOQ code generator to generate the type-safe java classes from the H2 schema.

### Running the application

To run the application from maven:

`mvn spring-boot:run -Drun.profiles=h2`

This starts the application with the spring profile `h2` (datasource etc.)

## Using PostgreSQL as database

### Preparing the database (required once)

* Install PostgreSQL and configure (I use 9.6)
* Create the database `sipamato` and the administrative user `sipamadmin` for the Flyway migrations.
  * As user `postgres`:
    `$ createdb sipamato`
  * `psql`
    * `CREATE USER sipamadmin WITH CREATEROLE PASSWORD 'sipamadmin';`
    * `GRANT ALL PRIVILEGES ON DATABASE sipamato to sipamadmin WITH GRANT OPTION;`
    
  If you use a different username or password, you'll need to override the property `flyway.user` and/or `flyway.password` defined in `application-postgres.properties` file. You could e.g. create your own `application.properties` in the same directory as the jar.
    
### Building the jar

`mvn clean install -Dspring.profiles.active=postgres`

This runs all PostgreSQL-Flyway scripts to create the tables in the schema `public` and inserts the test data. It then runs the jOOQ code generator to generate the type-safe java classes from the database.

### Running the application

To run the application from maven:

`mvn spring-boot:run -Drun.profiles=postgres`

This starts the application with the spring profile `postgres` (datasource etc.)


## Running the project in Eclipse:

I use STS (with the Spring Boot Dashboard). Configured with the following parameters:

* Main Type: `ch.difty.sipamato.SipamatoApplication`
* Profiles: `DB_JOOQ,h2` or `DB_JOOQ,postgres` (depending on how the project was built with maven in the first place)

You might want to add profile `development` as well.


## Running all tests

The surefire plugin is configured to run the normal tests, but not page/panel tests or integration tests without explicitly activating those tests via profiles. Assuming to run the project with H2 in the following examples:

Run the fast tests - excluding wicket page/panel tests and integration tests:

`mvn clean install -Dspring.profiles.active=h2`

Run the wicket tests only - ignoring normal tests or integrationTests:

`mvn clean install -PpageTests -Dspring.profiles.active=h2`

Run all tests (including wicket page/panel tests and integrationTests):

`mvn clean install -PallTests -Dspring.profiles.active=h2`
