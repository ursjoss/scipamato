package ch.difty.scipamato.core.web.common

import ch.difty.scipamato.core.config.ApplicationCoreProperties
import ch.difty.scipamato.core.entity.CodeClass
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.amshove.kluent.shouldBeTrue
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.Model
import org.junit.jupiter.api.Test

abstract class SelfUpdatingPageTest<T : BasePage<*>> : BasePageTest<T>() {

    @MockkBean(relaxed = true)
    protected lateinit var appProps: ApplicationCoreProperties

    override fun setUpHook() {
        every { appProps.authorParserStrategy } returns AuthorParserStrategy.PUBMED
        every { appProps.defaultLocalization } returns "de"
        every { appProps.brand } returns "SciPaMaTo"
        every { appProps.minimumPaperNumberToBeRecycled } returns 7L
    }

    @Test
    fun renderedPage_setsOutputMarkupIdToComponents() {
        tester.startPage(makePage())
        tester.getComponentFromLastRenderedPage("contentPanel:form:title").outputMarkupId.shouldBeTrue()
    }

    @Test
    fun test() {
        val p: SelfUpdatingPage<*> = object : SelfUpdatingPage<CodeClass?>(Model.of(CodeClass(1, "CC1", ""))) {
            override fun getForm(): Form<CodeClass?> {
                return Form<CodeClass?>("id")
            }
        }
        p.implSpecificOnInitialize()
    }
}
