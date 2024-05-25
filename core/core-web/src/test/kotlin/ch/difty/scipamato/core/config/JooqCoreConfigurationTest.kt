package ch.difty.scipamato.core.config

import ch.difty.scipamato.core.persistence.UserRepository
import ch.difty.scipamato.core.persistence.code.CodeRepository
import ch.difty.scipamato.core.persistence.codeclass.CodeClassRepository
import ch.difty.scipamato.core.persistence.keyword.KeywordRepository
import ch.difty.scipamato.core.persistence.newsletter.NewsletterRepository
import ch.difty.scipamato.core.persistence.newsletter.NewsletterTopicRepository
import ch.difty.scipamato.core.persistence.paper.PaperRepository
import ch.difty.scipamato.core.persistence.paper.searchorder.BySearchOrderRepository
import ch.difty.scipamato.core.persistence.paper.searchorder.PaperSlimBackedSearchOrderRepository
import ch.difty.scipamato.core.persistence.paper.slim.PaperSlimRepository
import ch.difty.scipamato.core.persistence.search.SearchOrderRepository
import ch.difty.scipamato.core.persistence.user.UserRoleRepository
import com.ninjasquad.springmockk.MockkBean
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
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import java.sql.SQLException
import javax.sql.DataSource

@SpringBootTest
@Disabled("might have to move it into integration-test TODO")
@Suppress("UnusedPrivateMember")
internal class JooqCoreConfigurationTest {

    @MockkBean
    private lateinit var userRepoMock: UserRepository

    @MockkBean
    private lateinit var userRoleRepoMock: UserRoleRepository

    @MockkBean
    private lateinit var codeRepoMock: CodeRepository

    @MockkBean
    private lateinit var codeClassRepoMock: CodeClassRepository

    @MockkBean
    private lateinit var keywordRepoMock: KeywordRepository

    @MockkBean
    private lateinit var newsletterRepoMock: NewsletterRepository

    @MockkBean
    private lateinit var newsletterTopicRepoMock: NewsletterTopicRepository

    @MockkBean
    @Qualifier("jooqPaperBySearchOrderRepo")
    private lateinit var bySearchOrderRepoMock: BySearchOrderRepository<*>

    @MockkBean
    private lateinit var paperRepoMock: PaperRepository

    @MockkBean
    private lateinit var paperSlimBackedSearchOrderRepositoryMock: PaperSlimBackedSearchOrderRepository

    @MockkBean
    private lateinit var paperSlimRepositoryMock: PaperSlimRepository

    @MockkBean
    private lateinit var searchOrderRepositoryMock: SearchOrderRepository

    @MockkBean
    private lateinit var dataSourceMock: DataSource

    @Autowired
    @Qualifier("coreConfiguration")
    private lateinit var jooqCoreConfig: Configuration

    @Autowired
    @Qualifier("publicConfiguration")
    private lateinit var jooqPublicConfig: Configuration

    @Autowired
    @Qualifier("batchConfiguration")
    private lateinit var jooqBatchConfig: Configuration

    @Test
    @Throws(SQLException::class)
    fun assertJooqPublicConfigIsProperlyWired() {
        assertConfiguration(jooqPublicConfig)
    }

    @Test
    @Throws(SQLException::class)
    fun assertJooqCoreConfigIsProperlyWired() {
        assertConfiguration(jooqCoreConfig)
    }

    @Test
    @Throws(SQLException::class)
    fun assertJooqBatchConfigIsProperlyWired() {
        assertConfiguration(jooqBatchConfig)
    }

    @Throws(SQLException::class)
    private fun assertConfiguration(config: Configuration?) {
        config.shouldNotBeNull()
        config.dialect() shouldBeEqualTo SQLDialect.POSTGRES
        config.settings().isExecuteWithOptimisticLocking.shouldBeTrue()

        // assert Datasource Connection Provider
        config.connectionProvider().shouldNotBeNull()
        config.connectionProvider() shouldBeInstanceOf DataSourceConnectionProvider::class
        val dscp = config.connectionProvider() as DataSourceConnectionProvider
        dscp.dataSource() shouldBeInstanceOf TransactionAwareDataSourceProxy::class
        dscp.dataSource().isWrapperFor(HikariDataSource::class.java).shouldBeTrue()

        // assert executeListenerProviders
        config.executeListenerProviders() shouldHaveSize 1
        val elp = config.executeListenerProviders()[0] as DefaultExecuteListenerProvider
        elp.provide().javaClass.name shouldBeEqualTo
            "org.springframework.boot.autoconfigure.jooq.ExceptionTranslatorExecuteListener"

        // assert TransactionProvider
        config.transactionProvider().shouldNotBeNull()
        config.transactionProvider().javaClass.name shouldBeEqualTo
            "org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider"
    }
}
