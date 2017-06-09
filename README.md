# sipamato

SiPaMaTo (Scientific Paper Management Tool)

# Elevator Pitch

TODO

# Technology Stack

* Java 1.8
* [Spring Boot](https://projects.spring.io/spring-boot/) with [Spring Security](https://projects.spring.io/spring-security/)
* Presentation Layer
  * [Apache Wicket](https://wicket.apache.org/) with [Bootstrap](http://getbootstrap.com/)
    * [wicket-spring-boot-starter](https://github.com/MarcGiffing/wicket-spring-boot)
    * [wicket-bootstrap](https://github.com/l0rdn1kk0n/wicket-bootstrap)
  * [JasperReport](http://community.jaspersoft.com/) for PDF generation
  * [Feign](https://github.com/OpenFeign/feign) as HHTP client to PubMed
  * jaxb for XML parsing
* Persistence Layer
  * [jOOQ](https://www.jooq.org/)
  * [HikariCP](https://github.com/brettwooldridge/HikariCP)
  * [PostgreSQL](https://www.postgresql.org/)
  * [Flyway](https://flywaydb.org/) for database migrations
  
# User Documentation

* [Entering Papers](https://github.com/ursjoss/sipamato/wiki/Entering-Papers)
* [Filtering Papers](https://github.com/ursjoss/sipamato/wiki/Filtering-Papers)
* [Complex Searches on Papers](https://github.com/ursjoss/sipamato/wiki/Searches)


# Information for Developers

See [Developer Wiki](https://github.com/ursjoss/sipamato/wiki/Developer-Information)

# Information for Operations

See [Operations Wiki](https://github.com/ursjoss/sipamato/wiki/Operations)

