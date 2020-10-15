package ch.difty.scipamato.publ.web.autoconfiguration

import de.agilecoders.wicket.core.settings.BootstrapSettings
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = BootstrapProperties.PROPERTY_PREFIX)
@ConstructorBinding
data class BootstrapProperties(
    val isEnabled: Boolean = true,
    val theme: BootswatchTheme = BootswatchTheme.Sandstone,
) : BootstrapSettings() {

    companion object {
        const val PROPERTY_PREFIX = "wicket.external.agilecoders.bootstrap"
    }
}
