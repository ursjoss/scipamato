package ch.difty.scipamato.publ.config

import com.zaxxer.hikari.HikariDataSource
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeNull
import org.jooq.Configuration
import org.jooq.SQLDialect
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultExecuteListenerProvider
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import java.sql.SQLException

@SpringBootTest
internal class JooqConfigurationTest {

    @Autowired
    private lateinit var jooqConfig: Configuration

    @Test
    @Throws(SQLException::class)
    fun assertJooqConfigIsProperlyWired() {
        jooqConfig.shouldNotBeNull()
        jooqConfig.dialect() shouldBeEqualTo SQLDialect.POSTGRES

        // assert Datasource Connection Provider
        jooqConfig.connectionProvider() shouldBeInstanceOf DataSourceConnectionProvider::class

        val dscp = jooqConfig.connectionProvider() as DataSourceConnectionProvider
        dscp.dataSource() shouldBeInstanceOf TransactionAwareDataSourceProxy::class
        dscp.dataSource().isWrapperFor(HikariDataSource::class.java).shouldBeTrue()

        // assert executeListenerProviders
        jooqConfig.executeListenerProviders() shouldHaveSize 1
        val elp = jooqConfig.executeListenerProviders()[0] as DefaultExecuteListenerProvider
        elp.provide().javaClass.name shouldBeEqualTo "org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator"

        // assert TransactionProvider
        jooqConfig.transactionProvider().javaClass.name shouldBeEqualTo
            "org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider"
    }
}
