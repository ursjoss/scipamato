package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import ch.difty.scipamato.core.persistence.UserRepository;
import ch.difty.scipamato.core.persistence.code.CodeRepository;
import ch.difty.scipamato.core.persistence.codeclass.CodeClassRepository;
import ch.difty.scipamato.core.persistence.keyword.KeywordRepository;
import ch.difty.scipamato.core.persistence.newsletter.NewsletterRepository;
import ch.difty.scipamato.core.persistence.newsletter.NewsletterTopicRepository;
import ch.difty.scipamato.core.persistence.paper.PaperRepository;
import ch.difty.scipamato.core.persistence.paper.searchorder.BySearchOrderRepository;
import ch.difty.scipamato.core.persistence.paper.searchorder.PaperSlimBackedSearchOrderRepository;
import ch.difty.scipamato.core.persistence.paper.slim.PaperSlimRepository;
import ch.difty.scipamato.core.persistence.search.SearchOrderRepository;
import ch.difty.scipamato.core.persistence.user.UserRoleRepository;
import ch.difty.scipamato.core.pubmed.PubmedImportService;

@SpringBootTest@Disabled("might have to move it into intTest TODO")
class JooqCoreConfigurationTest {

    @MockBean
    private PubmedImportService                  pubmedImportServiceMock;
    @MockBean
    private UserRepository                       userRepoMock;
    @MockBean
    private UserRoleRepository                   userRoleRepoMock;
    @MockBean
    private CodeRepository                       codeRepoMock;
    @MockBean
    private CodeClassRepository                  codeClassRepoMock;
    @MockBean
    private KeywordRepository                    keywordRepoMock;
    @MockBean
    private NewsletterRepository                 newsletterRepoMock;
    @MockBean
    private NewsletterTopicRepository            newsletterTopicRepoMock;
    @MockBean
    @Qualifier("jooqPaperBySearchOrderRepo")
    private BySearchOrderRepository              bySearchOrderRepoMock;
    @MockBean
    private PaperRepository                      paperRepoMock;
    @MockBean
    private PaperSlimBackedSearchOrderRepository paperSlimBackedSearchOrderRepositoryMock;
    @MockBean
    private PaperSlimRepository                  paperSlimRepositoryMock;
    @MockBean
    private SearchOrderRepository                searchOrderRepositoryMock;
    @MockBean
    private DataSource                           dataSourceMock;

    @Autowired
    @Qualifier("coreConfiguration")
    private Configuration jooqCoreConfig;

    @Autowired
    @Qualifier("publicConfiguration")
    private Configuration jooqPublicConfig;

    @Autowired
    @Qualifier("batchConfiguration")
    private Configuration jooqBatchConfig;

    @Test
    void assertJooqPublicConfigIsProperlyWired() throws SQLException {
        assertConfiguration(jooqPublicConfig);
    }

    @Test
    void assertJooqCoreConfigIsProperlyWired() throws SQLException {
        assertConfiguration(jooqCoreConfig);
    }

    @Test
    void assertJooqBatchConfigIsProperlyWired() throws SQLException {
        assertConfiguration(jooqBatchConfig);
    }

    private void assertConfiguration(Configuration config) throws SQLException {
        assertThat(config).isNotNull();

        assertThat(config.dialect()).isEqualTo(SQLDialect.POSTGRES);
        assertThat(config
            .settings()
            .isExecuteWithOptimisticLocking()).isTrue();

        // assert Datasource Connection Provider
        assertThat(config.connectionProvider()).isNotNull();
        assertThat(config.connectionProvider()).isInstanceOf(DataSourceConnectionProvider.class);
        DataSourceConnectionProvider dscp = (DataSourceConnectionProvider) config.connectionProvider();
        assertThat(dscp.dataSource()).isInstanceOf(TransactionAwareDataSourceProxy.class);
        assertThat(dscp
            .dataSource()
            .isWrapperFor(HikariDataSource.class)).isTrue();

        // assert executeListenerProviders
        assertThat(config.executeListenerProviders()).hasSize(1);
        DefaultExecuteListenerProvider elp = (DefaultExecuteListenerProvider) config.executeListenerProviders()[0];
        assertThat(elp
            .provide()
            .getClass()
            .getName()).isEqualTo("org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator");

        // assert TransactionProvider
        assertThat(config.transactionProvider()).isNotNull();
        assertThat(config
            .transactionProvider()
            .getClass()
            .getName()).isEqualTo("org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider");
    }
}
