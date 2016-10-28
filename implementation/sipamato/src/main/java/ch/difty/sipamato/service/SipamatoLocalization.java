package ch.difty.sipamato.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.config.ApplicationProperties;
import ch.difty.sipamato.lib.AssertAs;

@Component
public class SipamatoLocalization implements Localization {

    private final String defaultLocalization;
    private String localization;

    @Autowired
    public SipamatoLocalization(final ApplicationProperties appProperties) {
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
