package ch.difty.scipamato.core.web.common

import ch.difty.scipamato.core.config.ApplicationCoreProperties
import ch.difty.scipamato.core.entity.CodeClass
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy
import org.amshove.kluent.shouldBeTrue
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.Model
import org.junit.jupiter.api.Test

abstract class SelfUpdatingPageTest<T : BasePage<*>> : BasePageTest<T>() {

    protected val appProps = TestApplicationCoreProperties()

    @Test
    fun renderedPage_setsOutputMarkupIdToComponents() {
        tester.startPage(makePage())
        tester.getComponentFromLastRenderedPage("contentPanel:form:title").outputMarkupId.shouldBeTrue()
    }

    @Test
    fun test() {
        val p: SelfUpdatingPage<*> = object : SelfUpdatingPage<CodeClass?>(
            Model.of(CodeClass(1, "CC1", ""))
        ) {
            private val serialVersionUID: Long = 1L
            override fun getForm(): Form<CodeClass?> = Form("id")
        }
        p.implSpecificOnInitialize()
    }
}

class TestApplicationCoreProperties : ApplicationCoreProperties {
    override val brand get() = "SciPaMaTo"
    override val minimumPaperNumberToBeRecycled get() = 7L
    override val defaultLocalization get() = "de"
    override val authorParserStrategy get() = AuthorParserStrategy.PUBMED

    override val cmsUrlSearchPage: String? get() = null
    override val redirectFromPort: Int? get() = null
    override val multiSelectBoxActionBoxWithMoreEntriesThan get() = 0
    override val buildVersion: String? get() = null
    override val pubmedBaseUrl get() = ""
    override val titleOrBrand get() = ""
    override val risExporterStrategy get() = RisExporterStrategy.DEFAULT
    override val pubmedApiKey: String? get() = null

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
