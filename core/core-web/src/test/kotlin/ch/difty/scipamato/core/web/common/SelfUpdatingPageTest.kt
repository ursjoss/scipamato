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
            override fun getForm(): Form<CodeClass?> = Form("id")
        }
        p.implSpecificOnInitialize()
    }
}

class TestApplicationCoreProperties : ApplicationCoreProperties {
    override val brand get() = "SciPaMaTo"
    override fun getMinimumPaperNumberToBeRecycled() = 7L
    override val defaultLocalization get() = "de"
    override fun getAuthorParserStrategy() = AuthorParserStrategy.PUBMED

    override val cmsUrlSearchPage get() = null
    override val redirectFromPort get() = null
    override val multiSelectBoxActionBoxWithMoreEntriesThan get() = 0
    override val buildVersion get() = null
    override val pubmedBaseUrl get() = ""
    override val titleOrBrand get() = ""
    override fun getRisExporterStrategy() = RisExporterStrategy.DEFAULT
    override fun getPubmedApiKey() = null
}
