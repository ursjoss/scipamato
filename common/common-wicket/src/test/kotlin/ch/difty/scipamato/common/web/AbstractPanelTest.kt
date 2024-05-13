package ch.difty.scipamato.common.web

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.apache.wicket.bean.validation.PropertyValidator
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.Model
import org.junit.jupiter.api.Test

internal class AbstractPanelTest : WicketBaseTest() {

    @Test
    fun testViewMode_withOneArgConstructor() {
        assertViewMode(object : AbstractPanel<TestRecord>("panel") {
            private val serialVersionUID: Long = 1L
        })
    }

    @Test
    fun testViewMode_withTwoArgConstructor() {
        assertViewMode(object : AbstractPanel<TestRecord>("panel", Model.of(TestRecord(1, "foo"))) {
            private val serialVersionUID: Long = 1L
        })
    }

    @Test
    fun testViewMode_withThreeArgConstructor() {
        assertViewMode(object : AbstractPanel<TestRecord>("id", Model.of(TestRecord(1, "foo")), Mode.VIEW) {
            private val serialVersionUID: Long = 1L
        })
    }

    private fun assertViewMode(p: AbstractPanel<TestRecord>) {
        p.mode shouldBeEqualTo Mode.VIEW
        p.submitLinkResourceLabel shouldBeEqualTo "button.disabled.label"
        p.isViewMode.shouldBeTrue()
        p.isEditMode.shouldBeFalse()
        p.isSearchMode.shouldBeFalse()
    }

    @Test
    fun testEditMode() {
        assertEditMode(object : AbstractPanel<TestRecord>("panel", Model.of(TestRecord(1, "foo")), Mode.EDIT) {
            private val serialVersionUID: Long = 1L
        })
    }

    private fun assertEditMode(p: AbstractPanel<TestRecord>) {
        p.mode shouldBeEqualTo Mode.EDIT
        p.submitLinkResourceLabel shouldBeEqualTo "button.save.label"
        p.isViewMode.shouldBeFalse()
        p.isEditMode.shouldBeTrue()
        p.isSearchMode.shouldBeFalse()
    }

    @Test
    fun testSearchMode() {
        assertSearchMode(object : AbstractPanel<TestRecord>("panel", Model.of(TestRecord(1, "foo")), Mode.SEARCH) {
            private val serialVersionUID: Long = 1L
        })
    }

    private fun assertSearchMode(p: AbstractPanel<TestRecord>) {
        p.mode shouldBeEqualTo Mode.SEARCH
        p.submitLinkResourceLabel shouldBeEqualTo "button.search.label"
        p.isViewMode.shouldBeFalse()
        p.isEditMode.shouldBeFalse()
        p.isSearchMode.shouldBeTrue()
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
        val fc = mockk<FormComponent<String>>(relaxed = true) {
            every { id } returns "fcId"
        }
        val pv = mockk<PropertyValidator<FormComponent<String>>>()

        val p = TestAbstractPanel("panel", Mode.EDIT)
        p.queueFieldAndLabel(fc, pv)

        verify(exactly = 3) { fc.id }
        verify { fc.label = any() }
        verify { fc.setOutputMarkupId(true) }
        verify { fc.add(pv) }
        confirmVerified(fc, pv)
    }

    @Test
    fun queuingFieldAndLabel_withPropertyValidatorInViewMode_addsNothingToComponent() {
        val fc = mockk<FormComponent<String>>(relaxed = true) {
            every { id } returns "fcId"
        }
        val pv = mockk<PropertyValidator<FormComponent<String>>>()

        val p = TestAbstractPanel("panel", Mode.VIEW)
        p.queueFieldAndLabel(fc, pv)

        verify(exactly = 3) { fc.id }
        verify { fc.label = any() }
        verify { fc.setOutputMarkupId(true) }
        verify(exactly = 0) { fc.add(pv) }
        confirmVerified(fc, pv)
    }

    @Test
    fun queuingFieldAndLabel_withNullPropertyValidator_addsNothingToComponent() {
        val fc = mockk<FormComponent<String>>(relaxed = true) {
            every { id } returns "fcId"
        }

        val p = TestAbstractPanel("panel", Mode.EDIT)
        p.queueFieldAndLabel(fc, null)

        verify(exactly = 3) { fc.id }
        verify { fc.setOutputMarkupId(true) }
        verify { fc.label = any() }
        confirmVerified(fc)
    }
}
