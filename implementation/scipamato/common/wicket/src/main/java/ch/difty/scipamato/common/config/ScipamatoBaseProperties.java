package ch.difty.scipamato.common.config;

import java.io.Serializable;

public interface ScipamatoBaseProperties extends Serializable {

    String getBrand();

    String getPageTitle();

    String getDefaultLocalization();

    String getPubmedBaseUrl();

    String getCmsUrlSearchPage();

    Integer getRedirectFromPort();

    int getMultiSelectBoxActionBoxWithMoreEntriesThan();

}
