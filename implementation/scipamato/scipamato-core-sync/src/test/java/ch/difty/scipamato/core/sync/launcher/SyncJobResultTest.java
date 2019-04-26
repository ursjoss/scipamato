package ch.difty.scipamato.core.sync.launcher;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SyncJobResultTest {

    private final SyncJobResult result = new SyncJobResult();

    @Test
    void newSyncJobResult_hasNotSuccessful() {
        assertThat(result.isSuccessful()).isFalse();
    }

    @Test
    void newSyncJobResult_hasNotFailed() {
        assertThat(result.isFailed()).isFalse();
    }

    @Test
    void newSyncJobResult_hasNoMessages() {
        assertThat(result.getMessages()).isEmpty();
    }

    @Test
    void settingSuccess_hasMessageWithLevelInfo() {
        result.setSuccess("foo");
        SyncJobResult.LogMessage lm = result
            .getMessages()
            .get(0);
        assertThat(lm.getMessage()).isEqualTo("foo");
        assertThat(lm.getMessageLevel()).isEqualTo(SyncJobResult.MessageLevel.INFO);
    }

    @Test
    void settingFailure_hasMessageWithLevelError() {
        result.setFailure("bar");
        SyncJobResult.LogMessage lm = result
            .getMessages()
            .get(0);
        assertThat(lm.getMessage()).isEqualTo("bar");
        assertThat(lm.getMessageLevel()).isEqualTo(SyncJobResult.MessageLevel.ERROR);
    }

    @Test
    void settingWarning_hasMessageWithLevelWarning() {
        result.setWarning("baz");
        SyncJobResult.LogMessage lm = result
            .getMessages()
            .get(0);
        assertThat(lm.getMessage()).isEqualTo("baz");
        assertThat(lm.getMessageLevel()).isEqualTo(SyncJobResult.MessageLevel.WARNING);
    }

    @Test
    void settingWarning_doesNotAlterSuccess() {
        result.setWarning("baz");
        assertThat(result.isSuccessful()).isFalse();
        assertThat(result.isFailed()).isFalse();
    }

    @Test
    void addingSuccessMessage_resultsInSuccessfulJobWithMessage() {
        result.setSuccess("foo");
        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.isFailed()).isFalse();
        assertThat(result.getMessages()).hasSize(1);
        assertThat(result
            .getMessages()
            .get(0)
            .getMessage()).isEqualTo("foo");
    }

    @Test
    void addingFailureMessage_resultsInFailedJobWithMessage() {
        result.setFailure("bar");
        assertThat(result.isSuccessful()).isFalse();
        assertThat(result.isFailed()).isTrue();
        assertThat(result.getMessages()).hasSize(1);
        assertThat(result
            .getMessages()
            .get(0)
            .getMessage()).isEqualTo("bar");
    }

    @Test
    void withMultipleSteps_ifAllSucceed_success() {
        result.setSuccess("success1");
        result.setSuccess("success2");
        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.getMessages())
            .extracting("message")
            .containsExactly("success1", "success2");
    }

    @Test
    void withMultipleSteps_ifOneFails_failure() {
        result.setSuccess("success1");
        result.setFailure("some issue2");
        assertThat(result.isFailed()).isTrue();
        assertThat(result.getMessages())
            .extracting("message")
            .containsExactly("success1", "some issue2");
    }

    @Test
    void withMultipleSteps_failureWins() {
        result.setSuccess("success1");
        result.setFailure("some issue2");
        result.setSuccess("success3");
        assertThat(result.isFailed()).isTrue();
        assertThat(result.getMessages())
            .extracting("message")
            .containsExactly("success1", "some issue2", "success3");
    }
}
