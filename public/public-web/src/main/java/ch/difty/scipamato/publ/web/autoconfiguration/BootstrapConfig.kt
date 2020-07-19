package ch.difty.scipamato.publ.web.autoconfiguration

import ch.difty.scipamato.publ.config.ApplicationPublicProperties
import ch.difty.scipamato.publ.web.themes.markup.html.publ.ScipamatoPublicTheme
import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration
import de.agilecoders.wicket.core.Bootstrap
import de.agilecoders.wicket.core.settings.SingleThemeProvider
import de.agilecoders.wicket.less.BootstrapLess
import de.agilecoders.wicket.webjars.WicketWebjars
import org.apache.wicket.protocol.http.WebApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties

@ApplicationInitExtension
@ConditionalOnProperty(prefix = BootstrapProperties.PROPERTY_PREFIX, value = ["enabled"], matchIfMissing = true)
@ConditionalOnClass(BootstrapConfig::class)
@EnableConfigurationProperties(BootstrapProperties::class)
open class BootstrapConfig(
    private val prop: BootstrapProperties,
    private val applicationProperties: ApplicationPublicProperties
) : WicketApplicationInitConfiguration {

    override fun init(webApplication: WebApplication) {
        prop.themeProvider = SingleThemeProvider(ScipamatoPublicTheme(applicationProperties.isLessUsedOverCss))

        WicketWebjars.install(webApplication)

        Bootstrap.install(webApplication, prop)

        BootstrapLess.install(webApplication)
    }

}
