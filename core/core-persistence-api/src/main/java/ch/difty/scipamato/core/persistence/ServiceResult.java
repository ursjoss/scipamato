package ch.difty.scipamato.core.persistence;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ServiceResult {

    void addInfoMessage(@Nullable String msg);

    void addWarnMessage(@Nullable String msg);

    void addErrorMessage(@Nullable String msg);

    @NotNull
    List<String> getInfoMessages();

    @NotNull
    List<String> getWarnMessages();

    @NotNull
    List<String> getErrorMessages();
}
