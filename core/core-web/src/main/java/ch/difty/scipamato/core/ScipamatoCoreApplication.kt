package ch.difty.scipamato.core

import ch.difty.scipamato.common.logger
import ch.difty.scipamato.common.web.AbstractPage
import ch.difty.scipamato.core.config.ApplicationCoreProperties
import ch.difty.scipamato.core.db.tables.Paper
import ch.difty.scipamato.core.db.tables.ScipamatoUser
import ch.difty.scipamato.core.pubmed.PubmedArticleService
import ch.difty.scipamato.core.web.sync.ISyncTask
import ch.difty.scipamato.core.web.sync.TasksRunnable
import com.giffing.wicket.spring.boot.starter.app.WicketBootSecuredWebApplication
import org.apache.wicket.Session
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse
import org.apache.wicket.markup.html.SecurePackageResourceGuard
import org.apache.wicket.request.Request
import org.apache.wicket.request.Response
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private val log = logger()

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
@EnableFeignClients
@EnableCaching
@ComponentScan(basePackages = ["ch.difty.scipamato"])
open class ScipamatoCoreApplication : WicketBootSecuredWebApplication() {

    private val executorService = Executors.newSingleThreadExecutor()

    @Autowired
    private lateinit var dsl: DSLContext

    @Autowired
    protected lateinit var properties: ApplicationCoreProperties

    @Autowired
    private lateinit var pubmedArticleService: PubmedArticleService

    override fun init() {
        // TODO consider making it CSP compliant
        cspSettings.blocking().disabled()

        super.init()

        headerResponseDecorators.add { JavaScriptFilteredIntoFooterHeaderResponse(it, AbstractPage.FOOTER_CONTAINER) }
        registerJasperJrxmlFilesWithPackageResourceGuard()

        triggerStartupMigration(dsl) { pmId: Long ->
            val id = pmId.toInt()
            val result = properties.pubmedApiKey?.let {
                pubmedArticleService.getPubmedArticleWithPmidAndApiKey(id, it)
            } ?: pubmedArticleService.getPubmedArticleWithPmid(id)
            result.pubmedArticleFacade?.originalAbstract
        }
    }

    // Allow access to jrxml jasper report definition files
    private fun registerJasperJrxmlFilesWithPackageResourceGuard() {
        resourceSettings.packageResourceGuard?.run {
            if (this is SecurePackageResourceGuard) addPattern("+*.jrxml")
        }
    }

    @Suppress("MagicNumber")
    private fun triggerStartupMigration(dsl: DSLContext, getAbstract: (Long) -> String?) {
        val adminUserId = (dsl
            .select(ScipamatoUser.SCIPAMATO_USER.ID)
            .from(ScipamatoUser.SCIPAMATO_USER)
            .where(ScipamatoUser.SCIPAMATO_USER.USER_NAME.eq("admin"))
            .fetchOne()?.get(0) as? Int?) ?: 1
        log.info("Starting Migration (user $adminUserId) <----------------------")
        val papersToEnrich: List<PaperIds> =
            dsl.select(Paper.PAPER.ID, Paper.PAPER.PM_ID)
                .from(Paper.PAPER)
                .where(Paper.PAPER.PM_ID.isNotNull.and(Paper.PAPER.ORIGINAL_ABSTRACT.isNull))
                .fetchInto(PaperIds::class.java).toList()
        log.info("affected records with no abstract but PM_ID: ${papersToEnrich.size}")
        papersToEnrich.forEach { paper ->
            getAbstract(paper.pmId)?.let { abstract ->
                log.info("Migrating paper $paper")
                dsl.update(Paper.PAPER)
                    .set(Paper.PAPER.ORIGINAL_ABSTRACT, abstract)
                    .set(Paper.PAPER.LAST_MODIFIED, Timestamp.valueOf(LocalDateTime.now()))
                    .set(Paper.PAPER.LAST_MODIFIED_BY, adminUserId)
                    .set(Paper.PAPER.VERSION, Paper.PAPER.VERSION + 1)
                    .where(Paper.PAPER.ID.eq(paper.id).and(Paper.PAPER.ORIGINAL_ABSTRACT.isNull())).execute()
                Thread.sleep(1000L)
            }
        }
    }

    private data class PaperIds(val id: Long, val pmId: Long)

    override fun newSession(request: Request, response: Response): Session = ScipamatoSession(request)

    @Suppress("MagicNumber")
    override fun onDestroy() {
        executorService.shutdown()
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS))
                executorService.shutdownNow()
        } catch (e: InterruptedException) {
            log.error("Unable to shutdown executor service", e)
        }
        super.onDestroy()
    }

    @Synchronized
    open fun launchSyncTask(task: ISyncTask) {
        val sjr = ScipamatoSession.get().syncJobResult
        sjr.clear()
        executorService.execute(TasksRunnable(task, sjr::setSuccess, sjr::setFailure, sjr::setWarning, sjr::setDone))
    }

    companion object {
        fun getApplication() = get() as ScipamatoCoreApplication
    }
}


@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<ScipamatoCoreApplication>(*args)
}
