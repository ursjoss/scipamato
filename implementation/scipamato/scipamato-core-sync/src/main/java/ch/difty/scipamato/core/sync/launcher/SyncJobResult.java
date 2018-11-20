package ch.difty.scipamato.core.sync.launcher;

import java.util.ArrayList;
import java.util.List;

public class SyncJobResult {

    private final List<String> logMessages = new ArrayList<>();
    private       JobResult    result      = JobResult.UNKNOWN;

    public boolean isSuccessful() {
        return result == JobResult.SUCCESS;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isFailed() {
        return result == JobResult.FAILURE;
    }

    public List<String> getMessages() {
        return new ArrayList<>(logMessages);
    }

    public void setSuccess(final String msg) {
        result = JobResult.SUCCESS;
        logMessages.add(msg);
    }

    public void setFailure(final String msg) {
        result = JobResult.FAILURE;
        logMessages.add(msg);
    }

    private enum JobResult {
        UNKNOWN,
        SUCCESS,
        FAILURE
    }
}
