package ch.difty.scipamato.publ.web.autoconfiguration;

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension;
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;
import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.SingleThemeProvider;
import de.agilecoders.wicket.less.BootstrapLess;
import de.agilecoders.wicket.webjars.WicketWebjars;
import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import ch.difty.scipamato.publ.config.ApplicationPublicProperties;
import ch.difty.scipamato.publ.web.themes.markup.html.publ.ScipamatoPublicTheme;

@ApplicationInitExtension
@ConditionalOnProperty(prefix = BootstrapProperties.PROPERTY_PREFIX, value = "enabled", matchIfMissing = true)
@ConditionalOnClass(BootstrapConfig.class)
@EnableConfigurationProperties({ BootstrapProperties.class })
public class BootstrapConfig implements WicketApplicationInitConfiguration {

    private final BootstrapProperties prop;

    private final ApplicationPublicProperties applicationProperties;

    public BootstrapConfig(BootstrapProperties prop, ApplicationPublicProperties applicationProperties) {
        this.prop = prop;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void init(WebApplication webApplication) {
        prop.setThemeProvider(
            new SingleThemeProvider(new ScipamatoPublicTheme(applicationProperties.isLessUsedOverCss())));

        WicketWebjars.install(webApplication);

        Bootstrap.install(webApplication, prop);

        BootstrapLess.install(webApplication);
    }

}
