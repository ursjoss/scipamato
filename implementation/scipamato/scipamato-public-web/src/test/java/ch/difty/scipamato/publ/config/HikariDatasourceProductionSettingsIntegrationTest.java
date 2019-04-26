package ch.difty.scipamato.publ.config;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class HikariDatasourceProductionSettingsIntegrationTest {

    @Autowired
    private DataSource datasource;

    @Test
    void validateSettingsInProduction() {
        assertThat(datasource).isInstanceOf(HikariDataSource.class);
        HikariDataSource ds = (HikariDataSource) datasource;

        assertThat(ds.getJdbcUrl()).isEqualTo("jdbc:postgresql://localhost:5432/scipamato_public");
        assertThat(ds.getUsername()).isEqualTo("scipamatopub");
        assertThat(ds.getPassword()).isEqualTo("scipamatopub");
        assertThat(ds.getConnectionTimeout()).isEqualTo(15000);
        assertThat(ds.getMaxLifetime()).isEqualTo(180000);
        assertThat(ds.getMaximumPoolSize()).isEqualTo(15);
        assertThat(ds.getPoolName()).isEqualTo("SciPaMaTo-Public-HikariCP");
        assertThat(ds.isReadOnly()).isTrue();
    }
}
