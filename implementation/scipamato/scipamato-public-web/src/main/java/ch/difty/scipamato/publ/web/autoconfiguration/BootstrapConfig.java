package ch.difty.scipamato.publ.web.autoconfiguration;

import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension;
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;

import ch.difty.scipamato.publ.web.themes.markup.html.publ.ScipamatoPublicTheme;
import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.SingleThemeProvider;
import de.agilecoders.wicket.less.BootstrapLess;
import de.agilecoders.wicket.webjars.WicketWebjars;

@ApplicationInitExtension
@ConditionalOnProperty(prefix = BootstrapProperties.PROPERTY_PREFIX, value = "enabled", matchIfMissing = true)
@ConditionalOnClass(BootstrapConfig.class)
@EnableConfigurationProperties({ BootstrapProperties.class })
public class BootstrapConfig implements WicketApplicationInitConfiguration {

    private final BootstrapProperties prop;

    public BootstrapConfig(BootstrapProperties prop) {
        this.prop = prop;
    }

    @Override
    public void init(WebApplication webApplication) {
        // TODO configurable solution to switch between bootswatch themes and google and
        // own etc.
        prop.setThemeProvider(new SingleThemeProvider(new ScipamatoPublicTheme()));

        WicketWebjars.install(webApplication);

        Bootstrap.install(webApplication, prop);

        BootstrapLess.install(webApplication);
    }

}
