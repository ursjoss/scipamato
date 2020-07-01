package ch.difty.scipamato.core.web.paper.common

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.form.TextArea
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection", "UnusedEquals")
internal class NewFieldChangeEventTest {

    private lateinit var e: NewFieldChangeEvent
    private lateinit var targetMock: AjaxRequestTarget
    private lateinit var mockComponent: TextArea<*>

    @BeforeEach
    fun setUp() {
        mockComponent = mockk {
            every { id } returns "id"
            every { markupId } returns "mid"
        }
        targetMock = mockk {
            every { add(mockComponent) } returns Unit
        }
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(targetMock)
        unmockkAll()
    }

    @Test
    fun canRetrieveTarget() {
        e = NewFieldChangeEvent(targetMock)
        e.target shouldBeEqualTo targetMock
        verify { targetMock == targetMock }
    }

    @Test
    fun usingMinimalConstructor_doesNotSetAnySpecialStuff() {
        e = NewFieldChangeEvent(targetMock)
        e.id.shouldBeNull()
        e.markupId.shouldBeNull()
    }

    @Test
    fun usingWithId_doesAddId() {
        e = NewFieldChangeEvent(targetMock).withId("foo")
        e.id shouldBeEqualTo "foo"
        e.markupId.shouldBeNull()
    }

    @Test
    fun usingWithMarkupId_doesAddMarkupId() {
        e = NewFieldChangeEvent(targetMock).withMarkupId("bar")
        e.id.shouldBeNull()
        e.markupId shouldBeEqualTo "bar"
    }

    @Test
    fun usingWithIdAndMarkupId_doesAddBoth() {
        e = NewFieldChangeEvent(targetMock)
            .withId("hups")
            .withMarkupId("goo")
        e.id shouldBeEqualTo "hups"
        e.markupId shouldBeEqualTo "goo"
    }

    @Test
    fun canOverrideTarget() {
        e = NewFieldChangeEvent(targetMock)
        e.target shouldBeEqualTo targetMock

        val targetMock2 = mockk<AjaxRequestTarget>()
        e.target = targetMock2
        e.target shouldBeEqualTo targetMock2
        verify { targetMock == targetMock }
        verify { targetMock2 == targetMock2 }
    }

    @Test
    fun consideringAddingToTarget_withIdLessEvent_addsTarget() {
        e = NewFieldChangeEvent(targetMock)
        e.id.shouldBeNull()
        e.considerAddingToTarget(mockComponent)
        verify { targetMock.add(mockComponent) }
    }

    @Test
    fun consideringAddingToTarget_withDifferingId_doesNotAddTarget() {
        e = NewFieldChangeEvent(targetMock)
            .withId("otherId")
            .withMarkupId("mId")
        e.considerAddingToTarget(mockComponent)
        verify(exactly = 0) { targetMock.add(mockComponent) }
    }

    @Test
    fun consideringAddingToTarget_withSameIdButNullMarkupId_addsTarget() {
        every { mockComponent.id } returns "id"
        every { mockComponent.markupId } returns "mId"
        e = NewFieldChangeEvent(targetMock).withId("id")
        e.markupId.shouldBeNull()
        e.considerAddingToTarget(mockComponent)
        verify { targetMock.add(mockComponent) }
    }

    @Test
    fun consideringAddingToTarget_withSameIdAndDifferingMarkupId_addsTarget() {
        every { mockComponent.id } returns "id"
        every { mockComponent.markupId } returns "mId"
        e = NewFieldChangeEvent(targetMock)
            .withId("id")
            .withMarkupId("otherMarkupId")
        e.considerAddingToTarget(mockComponent)
        verify { targetMock.add(mockComponent) }
    }

    @Test
    fun consideringAddingToTarget_withSameIdButSameMarkupId_doesNotAddTarget() {
        every { mockComponent.id } returns "id"
        every { mockComponent.markupId } returns "mId"
        e = NewFieldChangeEvent(targetMock)
            .withId("id")
            .withMarkupId("mId")
        e.considerAddingToTarget(mockComponent)
        verify(exactly = 0) { targetMock.add(mockComponent) }
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(NewFieldChangeEvent::class.java)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }
}
