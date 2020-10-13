package ch.difty.scipamato.common.web

import ch.difty.scipamato.common.config.ApplicationProperties
import ch.difty.scipamato.common.navigator.LongNavigator
import ch.difty.scipamato.common.web.pages.AbstractMenuBuilder
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar
import org.apache.wicket.Page
import org.apache.wicket.model.StringResourceModel
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.io.Serializable

@SpringBootApplication
open class TestApplication {
    fun main(args: Array<String>) {
        SpringApplicationBuilder().sources(TestApplication::class.java).run(*args)
    }
}

@Component
class TestApplicationProperties : ApplicationProperties {
    override val buildVersion get() = "0.0.0"
    override val defaultLocalization get() = "en"
    override val brand get() = "SciPaMaTo"
    override val titleOrBrand get() = "SciPaMaTo"
    override val pubmedBaseUrl get() = "https://pubmed/"
    override val cmsUrlSearchPage get() = "http://localhost:8081/"
    override val redirectFromPort get() = 8080
    override val multiSelectBoxActionBoxWithMoreEntriesThan get() = 4
}

@Primary
@Component
class TestWebSessionFacade : ScipamatoWebSessionFacade {
    override val languageCode get() = "de"
    override val paperIdManager get() = LongNavigator()
    override fun hasAtLeastOneRoleOutOf(vararg roles: String) = true
}

@Primary
@Component
class TestMenuBuilder(
    applicationProperties: ApplicationProperties,
    webSessionFacade: ScipamatoWebSessionFacade
) : AbstractMenuBuilder(applicationProperties, webSessionFacade) {
    override fun addMenuLinksTo(navbar: Navbar, page: Page) {
        addPageLink(
            navbar, page, TestHomePage::class.java, "menu.home",
            GlyphIconType.home, Navbar.ComponentPosition.LEFT
        )
        addExternalLink(
            navbar, "https://github.com/ursjoss/scipamato/wiki",
            StringResourceModel("menu.help", page, null).string,
            GlyphIconType.questionsign, Navbar.ComponentPosition.RIGHT
        )
    }
}

data class TestRecord(val id: Int, val name: String) : Serializable
