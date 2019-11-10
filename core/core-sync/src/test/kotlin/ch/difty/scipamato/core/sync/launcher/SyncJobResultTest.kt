package ch.difty.scipamato.core.sync.launcher

import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

internal class SyncJobResultTest {

    private val result = SyncJobResult()

    @Test
    fun newSyncJobResult_hasNotSuccessful() {
        assertThat(result.isSuccessful).isFalse()
    }

    @Test
    fun newSyncJobResult_hasNotFailed() {
        assertThat(result.isFailed).isFalse()
    }

    @Test
    fun newSyncJobResult_hasNoMessages() {
        assertThat(result.messages).isEmpty()
    }

    @Test
    fun settingSuccess_hasMessageWithLevelInfo() {
        result.setSuccess("foo")
        val lm = result.messages.first()
        assertThat(lm.message).isEqualTo("foo")
        assertThat(lm.messageLevel).isEqualTo(SyncJobResult.MessageLevel.INFO)
    }

    @Test
    fun settingFailure_hasMessageWithLevelError() {
        result.setFailure("bar")
        val lm = result.messages.first()
        assertThat(lm.message).isEqualTo("bar")
        assertThat(lm.messageLevel).isEqualTo(SyncJobResult.MessageLevel.ERROR)
    }

    @Test
    fun settingWarning_hasMessageWithLevelWarning() {
        result.setWarning("baz")
        val lm = result.messages.first()
        assertThat(lm.message).isEqualTo("baz")
        assertThat(lm.messageLevel).isEqualTo(SyncJobResult.MessageLevel.WARNING)
    }

    @Test
    fun settingWarning_doesNotAlterSuccess() {
        result.setWarning("baz")
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.isFailed).isFalse()
    }

    @Test
    fun addingSuccessMessage_resultsInSuccessfulJobWithMessage() {
        result.setSuccess("foo")
        assertThat(result.isSuccessful).isTrue()
        assertThat(result.isFailed).isFalse()
        assertThat(result.messages).hasSize(1)
        assertThat(result.messages.first().message).isEqualTo("foo")
    }

    @Test
    fun addingFailureMessage_resultsInFailedJobWithMessage() {
        result.setFailure("bar")
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.isFailed).isTrue()
        assertThat(result.messages).hasSize(1)
        assertThat(result.messages.first().message).isEqualTo("bar")
    }

    @Test
    fun withMultipleSteps_ifAllSucceed_success() {
        result.setSuccess("success1")
        result.setSuccess("success2")
        assertThat(result.isSuccessful).isTrue()
        assertThat(result.messages.map { it.message }).containsExactly("success1", "success2")
    }

    @Test
    fun withMultipleSteps_ifOneFails_failure() {
        result.setSuccess("success1")
        result.setFailure("some issue2")
        assertThat(result.isFailed).isTrue()
        assertThat(result.messages.map { it.message }).containsExactly("success1", "some issue2")
    }

    @Test
    fun withMultipleSteps_failureWins() {
        result.setSuccess("success1")
        result.setFailure("some issue2")
        result.setSuccess("success3")
        assertThat(result.isFailed).isTrue()
        assertThat(result.messages.map { it.message }).containsExactly("success1", "some issue2", "success3")
    }
}
