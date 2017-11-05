package ch.difty.scipamato.persistence;

import java.util.List;

public interface ServiceResult {

    void addInfoMessage(String msg);

    void addWarnMessage(String msg);

    void addErrorMessage(String msg);

    List<String> getInfoMessages();

    List<String> getWarnMessages();

    List<String> getErrorMessages();

}
