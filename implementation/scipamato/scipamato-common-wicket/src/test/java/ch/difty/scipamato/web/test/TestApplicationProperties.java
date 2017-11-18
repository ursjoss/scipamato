package ch.difty.scipamato.web.test;

import ch.difty.scipamato.config.core.ApplicationProperties;
import ch.difty.scipamato.config.core.AuthorParserStrategy;

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
    public AuthorParserStrategy getAuthorParserStrategy() {
        return null;
    }

    @Override
    public String getBrand() {
        return "SciPaMaTo";
    }

    @Override
    public long getMinimumPaperNumberToBeRecycled() {
        return 0;
    }

}
