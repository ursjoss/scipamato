package ch.difty.scipamato.core.persistence;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultServiceResult implements ServiceResult {

    private final List<String> infoMessages  = new ArrayList<>();
    private final List<String> warnMessages  = new ArrayList<>();
    private final List<String> errorMessages = new ArrayList<>();

    @Override
    public void addInfoMessage(@Nullable final String msg) {
        if (msg != null)
            infoMessages.add(msg);
    }

    @Override
    public void addWarnMessage(@Nullable final String msg) {
        if (msg != null)
            warnMessages.add(msg);
    }

    @Override
    public void addErrorMessage(@Nullable final String msg) {
        if (msg != null)
            errorMessages.add(msg);
    }

    @NotNull
    @Override
    public List<String> getInfoMessages() {
        return infoMessages;
    }

    @NotNull
    @Override
    public List<String> getWarnMessages() {
        return warnMessages;
    }

    @NotNull
    @Override
    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
