package ch.difty.scipamato.core.sync.launcher;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

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
    public void addingSuccessMessage_resultsInSuccessfulJobWithMessage() {
        result.setSuccess("foo");
        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.isFailed()).isFalse();
        assertThat(result.getMessages()).hasSize(1);
        assertThat(result.getMessages()
            .get(0)).isEqualTo("foo");
    }

    @Test
    public void addingFailureMessage_resultsInFailedJobWithMessage() {
        result.setFailure("bar");
        assertThat(result.isSuccessful()).isFalse();
        assertThat(result.isFailed()).isTrue();
        assertThat(result.getMessages()).hasSize(1);
        assertThat(result.getMessages()
            .get(0)).isEqualTo("bar");
    }
}
