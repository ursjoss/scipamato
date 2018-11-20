package ch.difty.scipamato.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@SuppressWarnings("WeakerAccess")
@Component
@ConfigurationProperties(prefix = "sync")
@Getter
@Setter
public class SyncProperties {

    /**
     * Configuration for the synchronization source datasource (core database).
     */
    private Source source = new Source();

    /**
     * Configuration for the synchronization target datasource (public database).
     */
    private Target target = new Target();

    /**
     * Configuration for the database providing the spring batch meta tables.
     */
    private Batch batch = new Batch();

    @Getter
    @Setter
    public static class Source {
        private Datasource datasource = new Datasource();
    }

    @Getter
    @Setter
    public static class Target {
        private Datasource datasource = new Datasource();
    }

    @Getter
    @Setter
    public static class Batch {
        private Datasource datasource = new Datasource();
    }

    @Getter
    @Setter
    public static class Datasource {
        /**
         * The database connection string to connect to the database.
         */
        private String jdbcUrl;

        /**
         * The driver class name for the RDBMS.
         */
        private String driverClassName = "org.postgresql.Driver";

        /**
         * The database username for the application to connect to the database.
         */
        private String username = "scipamato";

        /**
         * The password for the application user to connect to the database.
         */
        private String password = "scipamato";

        /**
         * Maximum number of milliseconds the application will wait for a connection
         * from the pool.
         */
        private long connectionTimeout = 1000;

        /**
         * Maximum number of milliseconds a connection is allowed to sit idle in the
         * pool.
         */
        private int idleTimeout = 600000;

        /**
         * Maximum lifetime in milliseconds of a connection in the pool.
         */
        private int maxLifetime = 5;

        /**
         * Maximum size that the pool is allowed to reach.
         */
        private int maximumPoolSize = 5;

        /**
         * User-defined name for the connection pool.
         */
        private String poolName;

    }
}
