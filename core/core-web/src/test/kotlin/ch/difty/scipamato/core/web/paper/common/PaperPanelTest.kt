package ch.difty.scipamato.core.web.paper.common

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.core.NewsletterAware
import ch.difty.scipamato.core.entity.Code
import ch.difty.scipamato.core.entity.CodeBoxAware
import ch.difty.scipamato.core.entity.CodeClass
import ch.difty.scipamato.core.web.common.PanelTest
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.unmockkAll
import io.mockk.verify
import org.apache.wicket.Component
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextArea
import org.apache.wicket.markup.html.form.TextField
import org.junit.jupiter.api.AfterEach
import java.util.ArrayList

abstract class PaperPanelTest<T, P : PaperPanel<T>> : PanelTest<P>() where T : CodeBoxAware?, T : NewsletterAware? {

    private val codeClasses: MutableList<CodeClass> = ArrayList()
    private val codesOfClass1: MutableList<Code> = ArrayList()
    private val codesOfClass2: MutableList<Code> = ArrayList()
    private val codesOfClass3: MutableList<Code> = ArrayList()
    private val codesOfClass4: MutableList<Code> = ArrayList()
    private val codesOfClass5: MutableList<Code> = ArrayList()
    private val codesOfClass6: MutableList<Code> = ArrayList()
    private val codesOfClass7: MutableList<Code> = ArrayList()
    private val codesOfClass8: MutableList<Code> = ArrayList()

    // See https://issues.apache.org/jira/browse/WICKET-2790
    protected fun applyTestHackWithNestedMultiPartForms() {
        val servletRequest = tester.request
        servletRequest.setUseMultiPartContentType(true)
    }

    override fun setUpHook() {
        codeClasses.addAll(
            listOf(newCC(1), newCC(2), newCC(3), newCC(4), newCC(5), newCC(6), newCC(7), newCC(8))
        )
        every { codeClassServiceMock.find(LOCALE) } returns codeClasses
        var ccId = 0
        codesOfClass1.addAll(listOf(newC(++ccId, "F"), newC(ccId, "G"), newC(ccId, "A")))
        codesOfClass2.addAll(listOf(newC(++ccId, "A"), newC(ccId, "B")))
        codesOfClass3.addAll(listOf(newC(++ccId, "A"), newC(ccId, "B")))
        codesOfClass4.addAll(listOf(newC(++ccId, "A"), newC(ccId, "B")))
        codesOfClass5.addAll(listOf(newC(++ccId, "A"), newC(ccId, "B")))
        codesOfClass6.addAll(listOf(newC(++ccId, "A"), newC(ccId, "B")))
        codesOfClass7.addAll(listOf(newC(++ccId, "A"), newC(ccId, "B")))
        codesOfClass8.addAll(listOf(newC(++ccId, "A"), newC(ccId, "B")))

        with(codeServiceMock) {
            every { findCodesOfClass(CodeClassId.CC1, LOCALE) } returns codesOfClass1
            every { findCodesOfClass(CodeClassId.CC2, LOCALE) } returns codesOfClass2
            every { findCodesOfClass(CodeClassId.CC3, LOCALE) } returns codesOfClass3
            every { findCodesOfClass(CodeClassId.CC4, LOCALE) } returns codesOfClass4
            every { findCodesOfClass(CodeClassId.CC5, LOCALE) } returns codesOfClass5
            every { findCodesOfClass(CodeClassId.CC6, LOCALE) } returns codesOfClass6
            every { findCodesOfClass(CodeClassId.CC7, LOCALE) } returns codesOfClass7
            every { findCodesOfClass(CodeClassId.CC8, LOCALE) } returns codesOfClass8
        }
        setUpLocalHook()
    }

    protected open fun setUpLocalHook() {}

    @AfterEach
    fun tearDown() {
        confirmVerified(codeClassServiceMock, codeServiceMock)
        tearDownLocalHook()
        tester.destroy()
        unmockkAll()
    }

    protected open fun tearDownLocalHook() {}

    private fun newCC(id: Int): CodeClass = CodeClass(id, "cc$id", "")

    protected fun newC(ccId: Int, c: String): Code =
        Code(ccId.toString() + c, "Code $ccId$c", "", false, ccId, "cc$ccId", "", 0)

    protected fun verifyCodeAndCodeClassCalls(times: Int, timesCC1: Int = times) {
        verify { codeClassServiceMock.find(LOCALE) }
        verify(exactly = timesCC1) { codeServiceMock.findCodesOfClass(CodeClassId.CC1, LOCALE) }
        verify(exactly = times) { codeServiceMock.findCodesOfClass(CodeClassId.CC2, LOCALE) }
        verify(exactly = times) { codeServiceMock.findCodesOfClass(CodeClassId.CC3, LOCALE) }
        verify(exactly = times) { codeServiceMock.findCodesOfClass(CodeClassId.CC4, LOCALE) }
        verify(exactly = times) { codeServiceMock.findCodesOfClass(CodeClassId.CC5, LOCALE) }
        verify(exactly = times) { codeServiceMock.findCodesOfClass(CodeClassId.CC6, LOCALE) }
        verify(exactly = times) { codeServiceMock.findCodesOfClass(CodeClassId.CC7, LOCALE) }
        verify(exactly = times) { codeServiceMock.findCodesOfClass(CodeClassId.CC8, LOCALE) }
    }

    private fun assertTextAreaWithLabel(path: String, modelValue: String, labelText: String) {
        assertComponentWithLabel(path, TextArea::class.java, modelValue, labelText)
    }

    protected fun assertTextFieldWithLabel(path: String, modelValue: Any, labelText: String) {
        assertComponentWithLabel(path, TextField::class.java, modelValue, labelText)
    }

    private fun assertMultiselectWithLabel(path: String, modelValue: Code, labelText: String) {
        tester.assertComponent(path, BootstrapMultiSelect::class.java)
        tester.assertModelValue(path, listOf(modelValue))
        tester.assertLabel(path + "Label", labelText)
    }

    private fun assertComponentWithLabel(
        path: String,
        componentClass: Class<out Component?>,
        modelValue: Any,
        labelText: String
    ) {
        tester.assertComponent(path, componentClass)
        tester.assertModelValue(path, modelValue)
        tester.assertLabel(path + "Label", labelText)
    }

    protected fun assertCommonComponents(id: String) {
        var b = "$id:form"
        tester.assertComponent(b, Form::class.java)
        assertTextAreaWithLabel("$b:authors", "a", "Authors")
        assertTextFieldWithLabel("$b:firstAuthor", "fa", "First Author")
        assertComponentWithLabel("$b:firstAuthorOverridden", CheckBoxX::class.java, false, "Override")
        assertTextAreaWithLabel("$b:title", "t", "Title")
        assertTextFieldWithLabel("$b:location", "l", "Location")
        assertTextFieldWithLabel("$b:doi", "doi", "DOI")
        b += ":tabs"
        tester.assertComponent(b, BootstrapTabbedPanel::class.java)
        var bb = "$b:panel"
        var bbb = "$bb:tab1Form"
        tester.assertComponent(bbb, Form::class.java)
        assertTextAreaWithLabel("$bbb:goals", "g", "Goals")
        assertTextAreaWithLabel("$bbb:population", "p", "Population")
        assertTextAreaWithLabel("$bbb:methods", "m", "Methods")
        assertTextAreaWithLabel("$bbb:populationPlace", "ppl", "Place/Country (study name)")
        assertTextAreaWithLabel("$bbb:populationParticipants", "ppa", "Participants")
        assertTextAreaWithLabel("$bbb:populationDuration", "pd", "Study Duration")
        assertTextAreaWithLabel("$bbb:exposurePollutant", "ep", "Pollutant")
        assertTextAreaWithLabel("$bbb:exposureAssessment", "ea", "Exposure Assessment")
        assertTextAreaWithLabel("$bbb:methodStudyDesign", "msd", "Study Design")
        assertTextAreaWithLabel("$bbb:methodOutcome", "mo", "Outcome")
        assertTextAreaWithLabel("$bbb:methodStatistics", "ms", "Statistical Method")
        assertTextAreaWithLabel("$bbb:methodConfounders", "mc", "Confounders")
        tester.clickLink("panel:form:tabs:tabs-container:tabs:1:link")
        bbb = "$bb:tab2Form"
        tester.assertComponent(bbb, Form::class.java)
        assertTextAreaWithLabel("$bbb:result", "r", "Results")
        assertTextAreaWithLabel("$bbb:comment", "c", "Comment")
        assertTextAreaWithLabel("$bbb:intern", "i", "Internal")
        assertTextAreaWithLabel("$bbb:resultMeasuredOutcome", "rmo", "Measured Outcome")
        assertTextAreaWithLabel("$bbb:resultExposureRange", "rer", "Exposure (Range)")
        assertTextAreaWithLabel("$bbb:resultEffectEstimate", "ree", "Effect Estimate/Results")
        assertTextAreaWithLabel("$bbb:conclusion", "cc", "Conclusion")
        tester.clickLink("panel:form:tabs:tabs-container:tabs:2:link")
        bbb = "$bb:tab3Form"
        tester.assertComponent(bbb, Form::class.java)
        assertMultiselectWithLabel("$bbb:codesClass1", newC(1, "F"), "cc1")
        assertTextFieldWithLabel("$bbb:mainCodeOfCodeclass1", "mcocc1", "Main Exposure Agent")
        assertMultiselectWithLabel("$bbb:codesClass2", newC(2, "A"), "cc2")
        assertMultiselectWithLabel("$bbb:codesClass3", newC(3, "A"), "cc3")
        assertMultiselectWithLabel("$bbb:codesClass4", newC(4, "A"), "cc4")
        assertMultiselectWithLabel("$bbb:codesClass5", newC(5, "A"), "cc5")
        assertMultiselectWithLabel("$bbb:codesClass6", newC(6, "A"), "cc6")
        assertMultiselectWithLabel("$bbb:codesClass7", newC(7, "A"), "cc7")
        assertMultiselectWithLabel("$bbb:codesClass8", newC(8, "A"), "cc8")
        tester.clickLink("panel:form:tabs:tabs-container:tabs:3:link")
        bbb = "$bb:tab4Form"
        tester.assertComponent(bbb, Form::class.java)
        assertTextAreaWithLabel("$bbb:populationPlace", "ppl", "Place/Country (study name)")
        assertTextAreaWithLabel("$bbb:populationParticipants", "ppa", "Participants")
        assertTextAreaWithLabel("$bbb:populationDuration", "pd", "Study Duration")
        assertTextAreaWithLabel("$bbb:exposurePollutant", "ep", "Pollutant")
        assertTextAreaWithLabel("$bbb:exposureAssessment", "ea", "Exposure Assessment")
        assertTextAreaWithLabel("$bbb:methodStudyDesign", "msd", "Study Design")
        assertTextAreaWithLabel("$bbb:methodOutcome", "mo", "Outcome")
        assertTextAreaWithLabel("$bbb:methodStatistics", "ms", "Statistical Method")
        assertTextAreaWithLabel("$bbb:methodConfounders", "mc", "Confounders")
        assertTextAreaWithLabel("$bbb:resultMeasuredOutcome", "rmo", "Measured Outcome")
        assertTextAreaWithLabel("$bbb:resultExposureRange", "rer", "Exposure (Range)")
        assertTextAreaWithLabel("$bbb:conclusion", "cc", "Conclusion")
        assertTextAreaWithLabel("$bbb:resultEffectEstimate", "ree", "Effect Estimate/Results")
        tester.clickLink("panel:form:tabs:tabs-container:tabs:4:link")
        bbb = "$bb:tab5Form"
        assertTextAreaWithLabel("$bbb:originalAbstract", "oa", "Original Abstract")
        tester.assertComponent(bbb, Form::class.java)
        tester.clickLink("panel:form:tabs:tabs-container:tabs:5:link")
        bbb = "$bb:tab6Form"
        tester.assertComponent(bbb, Form::class.java)
        tester.clickLink("panel:form:tabs:tabs-container:tabs:6:link")
        bbb = "$bb:tab7Form"
        tester.assertComponent(bbb, Form::class.java)
        bb = "$b:tabs-container:tabs:"
        tester.assertLabel(bb + "0:link:title", "Population, Goals, and Methods")
        tester.assertLabel(bb + "1:link:title", "Results and Comments")
        tester.assertLabel(bb + "2:link:title", "Codes and new Studies")
        tester.assertLabel(bb + "3:link:title", "New Field Entry")
        tester.assertLabel(bb + "4:link:title", "Original Abstract")
        tester.assertLabel(bb + "5:link:title", "Attachments")
        tester.assertLabel(bb + "6:link:title", "Newsletter")
    }

    companion object {
        private const val LOCALE = "en_us"
    }
}
