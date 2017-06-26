package ch.difty.scipamato.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.config.ApplicationProperties;
import ch.difty.scipamato.lib.AssertAs;

@Component
public class ScipamatoLocalization implements Localization {

    private static final long serialVersionUID = 1L;

    private final String defaultLocalization;
    private String localization;

    @Autowired
    public ScipamatoLocalization(final ApplicationProperties appProperties) {
        AssertAs.notNull(appProperties, "appProperties");
        this.defaultLocalization = appProperties.getDefaultLocalization();
        this.localization = this.defaultLocalization;
    }

    /** {@inheritDoc} */
    @Override
    public String getDefaultLocalization() {
        return defaultLocalization;
    }

    /** {@inheritDoc} */
    @Override
    public String getLocalization() {
        return localization;
    }

    /** {@inheritDoc} */
    @Override
    public void setLocalization(String localization) {
        this.localization = localization;
    }

}
