package ch.difty.scipamato.public_.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.config.MavenProperties;
import ch.difty.scipamato.common.config.core.ApplicationProperties;
import ch.difty.scipamato.common.config.core.AuthorParserStrategy;

/**
 * This bean is used to evaluate all environment properties used in the
 * application in one place and serve those as bean to wherever they are used.
 *
 * @see <a href=
 *      "https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-injecting-property-values-into-configuration-beans/">https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-injecting-property-values-into-configuration-beans/</a>
 *
 * @author u.joss
 */
@Component
public class ScipamatoProperties implements ApplicationProperties {

    @Autowired
    private ScipamatoPublicProperties scipamatoPublicProperties;
    @Autowired
    private MavenProperties           mavenProperties;

    @Override
    public String getBuildVersion() {
        return mavenProperties.getVersion();
    }

    @Override
    public String getDefaultLocalization() {
        return scipamatoPublicProperties.getDefaultLocalization();
    }

    @Override
    public AuthorParserStrategy getAuthorParserStrategy() {
        return null;
    }

    @Override
    public String getBrand() {
        return scipamatoPublicProperties.getBrand();
    }

    @Override
    public long getMinimumPaperNumberToBeRecycled() {
        return 0;
    }

    @Override
    public String getPubmedBaseUrl() {
        return scipamatoPublicProperties.getPubmedBaseUrl();
    }

}
