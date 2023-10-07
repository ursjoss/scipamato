package ch.difty.scipamato.publ.config

import ch.difty.scipamato.publ.AbstractIntegrationTest
import com.zaxxer.hikari.HikariDataSource
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.sql.DataSource

internal class HikariDatasourceProductionSettingsIntegrationTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var datasource: DataSource

    @Test
    fun jdbcUrl() {
        datasource shouldBeInstanceOf HikariDataSource::class
        val ds = datasource as HikariDataSource
        ds.jdbcUrl shouldBeEqualTo "jdbc:tc:postgresql:15.4://localhost:5432/scipamato_public"
    }

    @Test
    fun username_password() {
        val ds = datasource as HikariDataSource
        ds.username shouldBeEqualTo "scipamatopub"
        ds.password shouldBeEqualTo "scipamatopub"
    }

    @Test
    fun connectionTimeout() {
        val ds = datasource as HikariDataSource
        ds.connectionTimeout shouldBeEqualTo 60000
    }

    @Test
    fun maxLifetime() {
        val ds = datasource as HikariDataSource
        ds.maxLifetime shouldBeEqualTo 100000
    }

    @Test
    fun pool() {
        val ds = datasource as HikariDataSource
        ds.poolName shouldBeEqualTo "SciPaMaTo-Public-HikariCP"
        ds.maximumPoolSize shouldBeEqualTo 5
    }

    @Test
    fun readOnly() {
        val ds = datasource as HikariDataSource
        ds.isReadOnly.shouldBeFalse()
    }
}
