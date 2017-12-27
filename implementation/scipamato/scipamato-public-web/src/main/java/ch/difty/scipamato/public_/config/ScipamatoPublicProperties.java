package ch.difty.scipamato.public_.config;

import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.config.ApplicationProperties;
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
public class ScipamatoPublicProperties implements ApplicationProperties {

    private final ScipamatoProperties scipamatoProperties;
    private final MavenProperties     mavenProperties;

    public ScipamatoPublicProperties(ScipamatoProperties scipamatoProperties, MavenProperties mavenProperties) {
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
    public String getBrand() {
        return scipamatoProperties.getBrand();
    }

    @Override
    public String getPubmedBaseUrl() {
        return scipamatoProperties.getPubmedBaseUrl();
    }

}
