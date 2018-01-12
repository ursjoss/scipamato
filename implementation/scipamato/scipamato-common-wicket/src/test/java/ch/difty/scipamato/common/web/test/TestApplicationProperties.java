package ch.difty.scipamato.common.web.test;

import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.config.ApplicationProperties;

@Component
public class TestApplicationProperties implements ApplicationProperties {

    @Override
    public String getBuildVersion() {
        return "0.0.0";
    }

    @Override
    public String getDefaultLocalization() {
        return "en";
    }

    @Override
    public String getBrand() {
        return "SciPaMaTo";
    }

    @Override
    public String getPubmedBaseUrl() {
        return "http://pubmed/";
    }

    @Override
    public Integer getRedirectFromPort() {
        return 8080;
    }

}
