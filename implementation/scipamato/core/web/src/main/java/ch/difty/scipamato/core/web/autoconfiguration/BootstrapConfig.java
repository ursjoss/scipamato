package ch.difty.scipamato.core.web.autoconfiguration;

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension;
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;
import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.ThemeProvider;
import de.agilecoders.wicket.less.BootstrapLess;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchThemeProvider;
import org.apache.wicket.protocol.http.WebApplication;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ApplicationInitExtension
@ConditionalOnProperty(prefix = BootstrapProperties.PROPERTY_PREFIX, value = "enabled", matchIfMissing = true)
@ConditionalOnClass(BootstrapConfig.class)
@EnableConfigurationProperties({ BootstrapProperties.class })
public class BootstrapConfig implements WicketApplicationInitConfiguration {

    private final BootstrapProperties prop;

    public BootstrapConfig(@NotNull BootstrapProperties prop) {
        this.prop = prop;
    }

    @Override
    public void init(WebApplication webApplication) {
        final ThemeProvider themeProvider = new BootswatchThemeProvider(prop.getTheme());
        prop.setThemeProvider(themeProvider);

        Bootstrap.install(webApplication, prop);

        BootstrapLess.install(webApplication);
    }
}
