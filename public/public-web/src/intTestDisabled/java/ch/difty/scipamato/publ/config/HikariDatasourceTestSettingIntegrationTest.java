package ch.difty.scipamato.publ.config;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HikariDatasourceTestSettingIntegrationTest {

    @Autowired
    private DataSource datasource;

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    void validateSettingsForIntegrationTests() {
        assertThat(datasource).isInstanceOf(HikariDataSource.class);

        HikariDataSource ds = (HikariDataSource) datasource;
        assertThat(ds.getJdbcUrl()).isEqualTo("jdbc:postgresql://localhost:5432/scipamato_public_it");
        assertThat(ds.getUsername()).isEqualTo("scipamatopub");
        assertThat(ds.getPassword()).isEqualTo("scipamatopub");
        assertThat(ds.getConnectionTimeout()).isEqualTo(15000);
        assertThat(ds.getIdleTimeout()).isEqualTo(600000);
        assertThat(ds.getMaxLifetime()).isEqualTo(100000);
        assertThat(ds.getMinimumIdle()).isEqualTo(2);
        assertThat(ds.getMaximumPoolSize()).isEqualTo(5);
        assertThat(ds.getPoolName()).isEqualTo("SciPaMaTo-Public-HikariCP");
        assertThat(ds.isReadOnly()).isTrue();
    }
}
