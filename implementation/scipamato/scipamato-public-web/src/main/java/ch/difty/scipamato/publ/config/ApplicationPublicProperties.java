package ch.difty.scipamato.publ.config;

import ch.difty.scipamato.common.config.ApplicationProperties;

public interface ApplicationPublicProperties extends ApplicationProperties {

    String COMMERCIAL_FONT_PRESENT = "scipamato.commercial-font-present";

    /**
     * @return whether the commercial font MetaOT can and shall be used or not
     */
    boolean isCommercialFontPresent();
}
