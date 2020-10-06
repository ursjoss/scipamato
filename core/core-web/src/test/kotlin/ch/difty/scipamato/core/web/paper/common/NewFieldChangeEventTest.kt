package ch.difty.scipamato.core.web.paper.common

import ch.difty.scipamato.common.AjaxRequestTargetSpy
import io.mockk.every
import io.mockk.mockk
import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeEmpty
import org.apache.wicket.markup.html.form.TextArea
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class NewFieldChangeEventTest {

    private val targetSpy = AjaxRequestTargetSpy()

    private lateinit var e: NewFieldChangeEvent
    private lateinit var mockComponent: TextArea<*>

    @BeforeEach
    fun setUp() {
        mockComponent = mockk {
            every { id } returns "id"
            every { markupId } returns "mid"
        }
    }

    @AfterEach
    fun tearDown() {
        targetSpy.reset()
    }

    @Test
    fun canRetrieveTarget() {
        e = NewFieldChangeEvent(targetSpy)
        e.target shouldBeEqualTo targetSpy
    }

    @Test
    fun usingMinimalConstructor_doesNotSetAnySpecialStuff() {
        e = NewFieldChangeEvent(targetSpy)
        e.id.shouldBeNull()
        e.markupId.shouldBeNull()
    }

    @Test
    fun usingWithId_doesAddId() {
        e = NewFieldChangeEvent(targetSpy, id = "foo")
        e.id shouldBeEqualTo "foo"
        e.markupId.shouldBeNull()
    }

    @Test
    fun usingWithMarkupId_doesAddMarkupId() {
        e = NewFieldChangeEvent(targetSpy, markupId = "bar")
        e.id.shouldBeNull()
        e.markupId shouldBeEqualTo "bar"
    }

    @Test
    fun usingWithIdAndMarkupId_doesAddBoth() {
        e = NewFieldChangeEvent(targetSpy, id = "hups", markupId = "goo")
        e.id shouldBeEqualTo "hups"
        e.markupId shouldBeEqualTo "goo"
    }

    @Test
    fun consideringAddingToTarget_withIdLessEvent_addsTarget() {
        e = NewFieldChangeEvent(targetSpy)
        e.id.shouldBeNull()
        e.considerAddingToTarget(mockComponent)
        targetSpy.components.shouldNotBeEmpty()
    }

    @Test
    fun consideringAddingToTarget_withDifferingId_doesNotAddTarget() {
        e = NewFieldChangeEvent(targetSpy, id = "otherId", markupId = "mId")
        e.considerAddingToTarget(mockComponent)
        targetSpy.components.shouldBeEmpty()
    }

    @Test
    fun consideringAddingToTarget_withSameIdButNullMarkupId_addsTarget() {
        every { mockComponent.id } returns "id"
        every { mockComponent.markupId } returns "mId"
        e = NewFieldChangeEvent(targetSpy, id = "id")
        e.markupId.shouldBeNull()
        e.considerAddingToTarget(mockComponent)
        targetSpy.components.shouldNotBeEmpty()
    }

    @Test
    fun consideringAddingToTarget_withSameIdAndDifferingMarkupId_addsTarget() {
        every { mockComponent.id } returns "id"
        every { mockComponent.markupId } returns "mId"
        e = NewFieldChangeEvent(targetSpy, id = "id", markupId = "otherMarkupId")
        e.considerAddingToTarget(mockComponent)
        targetSpy.components.shouldNotBeEmpty()
    }

    @Test
    fun consideringAddingToTarget_withSameIdButSameMarkupId_doesNotAddTarget() {
        every { mockComponent.id } returns "id"
        every { mockComponent.markupId } returns "mId"
        e = NewFieldChangeEvent(targetSpy, id = "id", markupId = "mId")
        e.considerAddingToTarget(mockComponent)
        targetSpy.components.shouldBeEmpty()
    }

    @Test
    fun equals() {
        EqualsVerifier.simple()
            .forClass(NewFieldChangeEvent::class.java)
            .withRedefinedSuperclass()
            .verify()
    }
}
