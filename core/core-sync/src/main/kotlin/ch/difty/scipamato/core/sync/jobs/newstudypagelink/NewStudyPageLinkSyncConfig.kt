package ch.difty.scipamato.core.sync.jobs.newstudypagelink

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.NewStudyPageLink
import ch.difty.scipamato.core.sync.PublicNewStudyPageLink
import ch.difty.scipamato.core.sync.PublicNewStudyPageLinkTable
import ch.difty.scipamato.core.sync.jobs.SyncConfig
import ch.difty.scipamato.core.sync.newPublicNewStudyPageLink
import ch.difty.scipamato.publ.db.tables.records.NewStudyPageLinkRecord
import org.jooq.DSLContext
import org.jooq.TableField
import org.springframework.batch.core.Job
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.transaction.PlatformTransactionManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp
import javax.sql.DataSource

/**
 * Defines the newStudyPageLink synchronization job.
 */
@Configuration
@Profile("!wickettest")
open class NewStudyPageLinkSyncConfig(
    @Qualifier("dslContext") jooqCore: DSLContext,
    @Qualifier("publicDslContext") jooqPublic: DSLContext,
    @Qualifier("dataSource") coreDataSource: DataSource,
    jobRepository: JobRepository,
    transactionManager: PlatformTransactionManager,
    dateTimeService: DateTimeService,
) : SyncConfig<PublicNewStudyPageLink, NewStudyPageLinkRecord>(TOPIC,
    CHUNK_SIZE,
    jooqCore,
    jooqPublic,
    coreDataSource,
    jobRepository,
    transactionManager,
    dateTimeService
) {
    @Bean
    open fun syncNewStudyPageLinkJob(): Job = createJob()

    override val jobName: String
        get() = "syncNewStudyPageLinkJob"

    override fun publicWriter(): ItemWriter<PublicNewStudyPageLink> = NewStudyPageLinkItemWriter(jooqPublic)

    override fun selectSql(): String =
        jooqCore
            .select(C_LANG_CODE, C_SORT, C_TITLE, C_URL)
            .from(NewStudyPageLink.NEW_STUDY_PAGE_LINK)
            .sql

    @Throws(SQLException::class)
    override fun makeEntity(rs: ResultSet): PublicNewStudyPageLink = newPublicNewStudyPageLink(
        langCode = getString(C_LANG_CODE, rs),
        sort = getInteger(C_SORT, rs),
        title = getString(C_TITLE, rs),
        url = getString(C_URL, rs),
        version = null,
        created = null,
        lastModified = null,
        lastSynched = getNow(),
    )

    override fun lastSynchedField(): TableField<NewStudyPageLinkRecord, Timestamp> =
        PublicNewStudyPageLinkTable.NEW_STUDY_PAGE_LINK.LAST_SYNCHED

    companion object {
        private const val TOPIC = "newStudyPageLink"
        private const val CHUNK_SIZE = 50

        // relevant fields of the core Language record
        private val C_LANG_CODE = NewStudyPageLink.NEW_STUDY_PAGE_LINK.LANG_CODE
        private val C_SORT = NewStudyPageLink.NEW_STUDY_PAGE_LINK.SORT
        private val C_TITLE = NewStudyPageLink.NEW_STUDY_PAGE_LINK.TITLE
        private val C_URL = NewStudyPageLink.NEW_STUDY_PAGE_LINK.URL
    }
}
