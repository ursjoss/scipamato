package ch.difty.scipamato.common.web

import ch.difty.scipamato.common.config.ApplicationProperties
import ch.difty.scipamato.common.navigator.LongNavigator
import ch.difty.scipamato.common.web.pages.AbstractMenuBuilder
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome7IconType
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

    companion object {
        private const val serialVersionUID: Long = 1572649704495652217L
    }
}

@Primary
@Component
class TestWebSessionFacade : ScipamatoWebSessionFacade {
    override val languageCode get() = "de"
    override val paperIdManager get() = LongNavigator()
    override fun hasAtLeastOneRoleOutOf(vararg roles: String) = true

    companion object {
        private const val serialVersionUID: Long = 4928608410325981664L
    }
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
            FontAwesome7IconType.house_s, Navbar.ComponentPosition.LEFT
        )
        addExternalLink(
            navbar, "https://github.com/ursjoss/scipamato/wiki",
            StringResourceModel("menu.help", page, null).string,
            FontAwesome7IconType.circle_question_s, Navbar.ComponentPosition.RIGHT
        )
    }

    companion object {
        private const val serialVersionUID: Long = -3636310190556316753L
    }
}

data class TestRecord(@JvmField val id: Int, @JvmField val name: String) : Serializable {
    companion object {
        private const val serialVersionUID: Long = -6684112836747553253L
    }
}
