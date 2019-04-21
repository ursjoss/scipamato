package ch.difty.scipamato.core.sync.launcher;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class SyncJobResultTest {

    private final SyncJobResult result = new SyncJobResult();

    @Test
    public void newSyncJobResult_hasNotSuccessful() {
        assertThat(result.isSuccessful()).isFalse();
    }

    @Test
    public void newSyncJobResult_hasNotFailed() {
        assertThat(result.isFailed()).isFalse();
    }

    @Test
    public void newSyncJobResult_hasNoMessages() {
        assertThat(result.getMessages()).isEmpty();
    }

    @Test
    public void settingSuccess_hasMessageWithLevelInfo() {
        result.setSuccess("foo");
        SyncJobResult.LogMessage lm = result
            .getMessages()
            .get(0);
        assertThat(lm.getMessage()).isEqualTo("foo");
        assertThat(lm.getMessageLevel()).isEqualTo(SyncJobResult.MessageLevel.INFO);
    }

    @Test
    public void settingFailure_hasMessageWithLevelError() {
        result.setFailure("bar");
        SyncJobResult.LogMessage lm = result
            .getMessages()
            .get(0);
        assertThat(lm.getMessage()).isEqualTo("bar");
        assertThat(lm.getMessageLevel()).isEqualTo(SyncJobResult.MessageLevel.ERROR);
    }

    @Test
    public void settingWarning_hasMessageWithLevelWarning() {
        result.setWarning("baz");
        SyncJobResult.LogMessage lm = result
            .getMessages()
            .get(0);
        assertThat(lm.getMessage()).isEqualTo("baz");
        assertThat(lm.getMessageLevel()).isEqualTo(SyncJobResult.MessageLevel.WARNING);
    }

    @Test
    public void settingWarning_doesNotAlterSuccess() {
        result.setWarning("baz");
        assertThat(result.isSuccessful()).isFalse();
        assertThat(result.isFailed()).isFalse();
    }

    @Test
    public void addingSuccessMessage_resultsInSuccessfulJobWithMessage() {
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
    public void addingFailureMessage_resultsInFailedJobWithMessage() {
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
    public void withMultipleSteps_ifAllSucceed_success() {
        result.setSuccess("success1");
        result.setSuccess("success2");
        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.getMessages())
            .extracting("message")
            .containsExactly("success1", "success2");
    }

    @Test
    public void withMultipleSteps_ifOneFails_failure() {
        result.setSuccess("success1");
        result.setFailure("some issue2");
        assertThat(result.isFailed()).isTrue();
        assertThat(result.getMessages())
            .extracting("message")
            .containsExactly("success1", "some issue2");
    }

    @Test
    public void withMultipleSteps_failureWins() {
        result.setSuccess("success1");
        result.setFailure("some issue2");
        result.setSuccess("success3");
        assertThat(result.isFailed()).isTrue();
        assertThat(result.getMessages())
            .extracting("message")
            .containsExactly("success1", "some issue2", "success3");
    }
}
