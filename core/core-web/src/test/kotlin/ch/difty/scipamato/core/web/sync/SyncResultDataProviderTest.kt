package ch.difty.scipamato.core.web.sync

import ch.difty.scipamato.core.ScipamatoSession
import ch.difty.scipamato.core.sync.launcher.SyncJobResult
import ch.difty.scipamato.core.web.AbstractWicketTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.apache.wicket.util.tester.WicketTester
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SyncResultDataProviderTest : AbstractWicketTest() {

    private lateinit var provider: SyncResultDataProvider

    @BeforeEach
    fun setUp() {
        WicketTester(application)
        provider = SyncResultDataProvider()
    }

    @AfterEach
    fun tearDown() {
        ScipamatoSession.get().syncJobResult.clear()
    }

    @Test
    fun gettingModel_wrapsLogMessage() {
        val logMessage = SyncJobResult.LogMessage("msg", SyncJobResult.MessageLevel.WARNING)
        val model = provider.model(logMessage)
        model.getObject() shouldBeEqualTo logMessage
    }

    @Test
    fun iniatesWithSize0() {
        provider.size() shouldBeEqualTo 0
    }

    @Test
    fun withSyncJobResultsAdded_reflectsSize() {
        ScipamatoSession.get().syncJobResult.setSuccess("Foo")
        ScipamatoSession.get().syncJobResult.setFailure("Bar")
        provider.size() shouldBeEqualTo 2
    }

    @Test
    fun x() {
        ScipamatoSession.get().syncJobResult.setSuccess("Foo")
        ScipamatoSession.get().syncJobResult.setFailure("Bar")
        provider.iterator(0, 2).asSequence().map { it.message }.toList().shouldContainSame(listOf("Foo", "Bar"))
    }

}
