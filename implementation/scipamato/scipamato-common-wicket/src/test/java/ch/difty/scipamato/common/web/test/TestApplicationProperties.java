package ch.difty.scipamato.common.web.test;

import ch.difty.scipamato.common.config.core.ApplicationProperties;
import ch.difty.scipamato.common.config.core.AuthorParserStrategy;

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

    @Override
    public String getPubmedBaseUrl() {
        return "http://pubmed/";
    }

}
