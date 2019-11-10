package ch.difty.scipamato.common.web

import com.nhaarman.mockitokotlin2.*
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX
import org.apache.wicket.bean.validation.PropertyValidator
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito

internal class AbstractPanelTest : WicketBaseTest() {

    @Test
    fun testViewMode_withOneArgConstructor() {
        assertViewMode(object : AbstractPanel<TestRecord>("panel") {})
    }

    @Test
    fun testViewMode_withTwoArgConstructor() {
        assertViewMode(object : AbstractPanel<TestRecord>("panel", Model.of(TestRecord(1, "foo"))) {})
    }

    @Test
    fun testViewMode_withThreeArgConstructor() {
        assertViewMode(object : AbstractPanel<TestRecord>("id", Model.of(TestRecord(1, "foo")), Mode.VIEW) {})
    }

    private fun assertViewMode(p: AbstractPanel<TestRecord>) {
        assertThat(p.mode).isEqualTo(Mode.VIEW)
        assertThat(p.submitLinkResourceLabel).isEqualTo("button.disabled.label")
        assertThat(p.isViewMode).isTrue()
        assertThat(p.isEditMode).isFalse()
        assertThat(p.isSearchMode).isFalse()
    }

    @Test
    fun testEditMode() {
        assertEditMode(object : AbstractPanel<TestRecord>("panel", Model.of(TestRecord(1, "foo")), Mode.EDIT) {})
    }

    private fun assertEditMode(p: AbstractPanel<TestRecord>) {
        assertThat(p.mode).isEqualTo(Mode.EDIT)
        assertThat(p.submitLinkResourceLabel).isEqualTo("button.save.label")
        assertThat(p.isViewMode).isFalse()
        assertThat(p.isEditMode).isTrue()
        assertThat(p.isSearchMode).isFalse()
    }

    @Test
    fun testSearchMode() {
        assertSearchMode(object : AbstractPanel<TestRecord>("panel", Model.of(TestRecord(1, "foo")), Mode.SEARCH) {})
    }

    private fun assertSearchMode(p: AbstractPanel<TestRecord>) {
        assertThat(p.mode).isEqualTo(Mode.SEARCH)
        assertThat(p.submitLinkResourceLabel).isEqualTo("button.search.label")
        assertThat(p.isViewMode).isFalse()
        assertThat(p.isEditMode).isFalse()
        assertThat(p.isSearchMode).isTrue()
    }

    @Test
    fun assertTestAbstractPanel_inViewMode() {
        tester.startComponentInPage(TestAbstractPanel("panel", Mode.VIEW))
        assertComponents()
    }

    private fun assertComponents() {
        tester.assertLabel("panel:fooLabel", "Foo")
        tester.assertComponent("panel:foo", TextField::class.java)
        tester.assertLabel("panel:barLabel", "Bar")
        tester.assertComponent("panel:bar", TextField::class.java)
        tester.assertLabel("panel:bazLabel", "Baz")
        tester.assertComponent("panel:baz", CheckBoxX::class.java)
    }

    @Test
    fun queuingFieldAndLabel_withPropertyValidatorInEditMode_addsLatterToComponent() {
        val fc = Mockito.mock(FormComponent::class.java)
        whenever(fc.id).thenReturn("fcId")
        val pv = Mockito.mock(PropertyValidator::class.java)

        val p = TestAbstractPanel("panel", Mode.EDIT)
        p.queueFieldAndLabel(fc, pv)

        verify(fc, times(3)).id
        verify(fc).label = any()
        verify(fc).add(pv)
        verifyNoMoreInteractions(fc, pv)
    }

    @Test
    fun queuingFieldAndLabel_withPropertyValidatorInViewMode_addsNothingToComponent() {
        val fc = Mockito.mock(FormComponent::class.java)
        whenever(fc.id).thenReturn("fcId")
        val pv = Mockito.mock(PropertyValidator::class.java)

        val p = TestAbstractPanel("panel", Mode.VIEW)
        p.queueFieldAndLabel(fc, pv)

        verify(fc, times(3)).id
        verify(fc).label = any()
        verify(fc, never()).add(pv)
        verifyNoMoreInteractions(fc, pv)
    }

    @Test
    fun queuingFieldAndLabel_withNullPropertyValidator_addsNothingToComponent() {
        val fc = Mockito.mock(FormComponent::class.java)
        whenever(fc.id).thenReturn("fcId")

        val p = TestAbstractPanel("panel", Mode.EDIT)
        p.queueFieldAndLabel(fc, null)

        verify(fc, times(3)).id
        verify(fc).label = any()
        verifyNoMoreInteractions(fc)
    }
}
