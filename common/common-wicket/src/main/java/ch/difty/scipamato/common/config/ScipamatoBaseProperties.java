package ch.difty.scipamato.common.config;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ScipamatoBaseProperties extends Serializable {

    @NotNull
    String getBrand();

    @Nullable
    String getPageTitle();

    @NotNull
    String getDefaultLocalization();

    @NotNull
    String getPubmedBaseUrl();

    @Nullable
    String getCmsUrlSearchPage();

    @Nullable
    Integer getRedirectFromPort();

    int getMultiSelectBoxActionBoxWithMoreEntriesThan();

}
