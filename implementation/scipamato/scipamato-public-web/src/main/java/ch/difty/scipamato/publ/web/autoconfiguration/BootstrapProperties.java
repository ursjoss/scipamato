package ch.difty.scipamato.publ.web.autoconfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = BootstrapProperties.PROPERTY_PREFIX)
@Getter
@Setter
public class BootstrapProperties extends BootstrapSettings {

    public static final String PROPERTY_PREFIX = "wicket.external.agilcoders.bootstrap";

    private boolean enabled = true;

    private BootswatchTheme theme = BootswatchTheme.Sandstone;

}
