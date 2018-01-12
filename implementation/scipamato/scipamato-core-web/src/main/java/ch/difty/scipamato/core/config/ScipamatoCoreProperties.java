package ch.difty.scipamato.core.config;

import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.config.MavenProperties;

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
public class ScipamatoCoreProperties implements ApplicationCoreProperties {

    private final ScipamatoProperties scipamatoProperties;
    private final MavenProperties     mavenProperties;

    public ScipamatoCoreProperties(ScipamatoProperties scipamatoProperties, MavenProperties mavenProperties) {
        this.scipamatoProperties = scipamatoProperties;
        this.mavenProperties = mavenProperties;
    }

    @Override
    public String getBuildVersion() {
        return mavenProperties.getVersion();
    }

    @Override
    public String getDefaultLocalization() {
        return scipamatoProperties.getDefaultLocalization();
    }

    @Override
    public AuthorParserStrategy getAuthorParserStrategy() {
        return scipamatoProperties.getAuthorParserStrategy();
    }

    @Override
    public String getBrand() {
        return scipamatoProperties.getBrand();
    }

    @Override
    public long getMinimumPaperNumberToBeRecycled() {
        return scipamatoProperties.getPaperNumberMinimumToBeRecycled();
    }

    @Override
    public String getPubmedBaseUrl() {
        return scipamatoProperties.getPubmedBaseUrl();
    }

    @Override
    public Integer getRedirectFromPort() {
        return scipamatoProperties.getRedirectFromPort();
    }

}
