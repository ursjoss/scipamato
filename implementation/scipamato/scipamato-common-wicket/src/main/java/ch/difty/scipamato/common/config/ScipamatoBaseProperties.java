package ch.difty.scipamato.common.config;

import java.io.Serializable;

public interface ScipamatoBaseProperties extends Serializable {

    String getBrand();

    String getPageTitle();

    String getDefaultLocalization();

    String getPubmedBaseUrl();

    Integer getRedirectFromPort();

}
