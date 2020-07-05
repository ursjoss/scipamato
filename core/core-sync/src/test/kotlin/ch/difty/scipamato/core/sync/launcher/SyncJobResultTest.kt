package ch.difty.scipamato.core.sync.launcher

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test

internal class SyncJobResultTest {

    private val result = SyncJobResult()

    @Test
    fun newSyncJobResult_hasNotSuccessful() {
        result.isSuccessful.shouldBeFalse()
    }

    @Test
    fun newSyncJobResult_hasNotFailed() {
        result.isFailed.shouldBeFalse()
    }

    @Test
    fun newSyncJobResult_hasNoMessages() {
        result.messages.shouldBeEmpty()
    }

    @Test
    fun settingSuccess_hasMessageWithLevelInfo() {
        result.setSuccess("foo")
        val lm = result.messages.first()
        lm.message shouldBeEqualTo "foo"
        lm.messageLevel shouldBeEqualTo SyncJobResult.MessageLevel.INFO
    }

    @Test
    fun settingFailure_hasMessageWithLevelError() {
        result.setFailure("bar")
        val lm = result.messages.first()
        lm.message shouldBeEqualTo "bar"
        lm.messageLevel shouldBeEqualTo SyncJobResult.MessageLevel.ERROR
    }

    @Test
    fun settingWarning_hasMessageWithLevelWarning() {
        result.setWarning("baz")
        val lm = result.messages.first()
        lm.message shouldBeEqualTo "baz"
        lm.messageLevel shouldBeEqualTo SyncJobResult.MessageLevel.WARNING
    }

    @Test
    fun settingWarning_doesNotAlterSuccess() {
        result.setWarning("baz")
        result.isSuccessful.shouldBeFalse()
        result.isFailed.shouldBeFalse()
    }

    @Test
    fun addingSuccessMessage_resultsInSuccessfulJobWithMessage() {
        result.setSuccess("foo")
        result.isSuccessful.shouldBeTrue()
        result.isFailed.shouldBeFalse()
        result.messages shouldHaveSize 1
        result.messages.first().message shouldBeEqualTo "foo"
    }

    @Test
    fun addingFailureMessage_resultsInFailedJobWithMessage() {
        result.setFailure("bar")
        result.isSuccessful.shouldBeFalse()
        result.isFailed.shouldBeTrue()
        result.messages shouldHaveSize 1
        result.messages.first().message shouldBeEqualTo "bar"
    }

    @Test
    fun withMultipleSteps_ifAllSucceed_success() {
        result.setSuccess("success1")
        result.setSuccess("success2")
        result.isSuccessful.shouldBeTrue()
        result.messages.map { it.message } shouldContainAll listOf("success1", "success2")
    }

    @Test
    fun withMultipleSteps_ifOneFails_failure() {
        result.setSuccess("success1")
        result.setFailure("some issue2")
        result.isFailed.shouldBeTrue()
        result.messages.map { it.message } shouldContainAll listOf("success1", "some issue2")
    }

    @Test
    fun withMultipleSteps_failureWins() {
        result.setSuccess("success1")
        result.setFailure("some issue2")
        result.setSuccess("success3")
        result.isFailed.shouldBeTrue()
        result.messages.map { it.message } shouldContainAll listOf("success1", "some issue2", "success3")
    }
}
