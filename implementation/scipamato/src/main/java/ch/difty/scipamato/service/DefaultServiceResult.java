package ch.difty.scipamato.service;

import java.util.ArrayList;
import java.util.List;

public class DefaultServiceResult implements ServiceResult {

    private final List<String> infoMessages = new ArrayList<>();
    private final List<String> warnMessages = new ArrayList<>();
    private final List<String> errorMessages = new ArrayList<>();

    @Override
    public void addInfoMessage(String msg) {
        if (msg != null)
            infoMessages.add(msg);
    }

    @Override
    public void addWarnMessage(String msg) {
        if (msg != null)
            warnMessages.add(msg);
    }

    @Override
    public void addErrorMessage(String msg) {
        if (msg != null)
            errorMessages.add(msg);
    }

    @Override
    public List<String> getInfoMessages() {
        return infoMessages;
    }

    @Override
    public List<String> getWarnMessages() {
        return warnMessages;
    }

    @Override
    public List<String> getErrorMessages() {
        return errorMessages;
    }

}
