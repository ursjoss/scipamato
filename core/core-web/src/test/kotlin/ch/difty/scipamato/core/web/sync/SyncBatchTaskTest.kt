package ch.difty.scipamato.core.web.sync

import ch.difty.scipamato.core.sync.launcher.SyncJobLauncher
import ch.difty.scipamato.core.web.AbstractWicketTest
import org.amshove.kluent.shouldBe
import org.apache.wicket.util.tester.WicketTester
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SyncBatchTaskTest : AbstractWicketTest() {

    private lateinit var setSuccess: (String) -> Unit
    private lateinit var setFailure: (String) -> Unit
    private lateinit var setWarning: (String) -> Unit
    private lateinit var setDone: () -> Unit

    private val jobLauncher = SyncJobLauncher { ss, sf, sw, sd ->
        setSuccess = ss
        setFailure = sf
        setWarning = sw
        setDone = sd
    }

    @BeforeEach
    fun setUp() {
        WicketTester(application)
    }

    @Test
    @Suppress("Unused_Expression")
    fun syncBatchTask_shouldPassLambdasToLauncher() {
        val ss: (String) -> Unit = { s -> s + s }
        val sf: (String) -> Unit = { s -> s + s + s }
        val sw: (String) -> Unit = { s -> s }
        val d: () -> Unit = { true }

        val sbt = SyncBatchTask(jobLauncher)
        sbt.launchJob(ss, sf, sw, d)

        setSuccess shouldBe ss
        setFailure shouldBe sf
        setWarning shouldBe sw
        setDone shouldBe d
    }
}
