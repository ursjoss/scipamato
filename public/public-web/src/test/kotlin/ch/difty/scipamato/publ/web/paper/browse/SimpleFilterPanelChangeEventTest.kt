package ch.difty.scipamato.publ.web.paper.browse

import ch.difty.scipamato.common.AjaxRequestTargetSpy
import io.mockk.every
import io.mockk.mockk
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.apache.wicket.markup.html.form.TextArea
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val ID = "id"
private const val MARKUP_ID = "mId"

@Suppress("SpellCheckingInspection")
internal class SimpleFilterPanelChangeEventTest {

    private val targetSpy = AjaxRequestTargetSpy()
    private val e = SimpleFilterPanelChangeEvent(targetSpy)

    private lateinit var mockComponent: TextArea<String>

    @BeforeEach
    fun setUp() {
        mockComponent = mockk {
            every { id } returns ID
            every { markupId } returns MARKUP_ID
        }
    }

    @AfterEach
    fun tearDown() {
        targetSpy.reset()
    }

    @Test
    fun canRetrieveTarget() {
        e.target shouldBeEqualTo targetSpy
    }

    @Test
    fun usingMinimalConstructor_doesNotSetAnySpecialStuff() {
        e.id.shouldBeNull()
        e.markupId.shouldBeNull()
    }

    @Test
    fun usingWithId_doesAddId() {
        val f = SimpleFilterPanelChangeEvent(targetSpy).withId("foo")
        f.id shouldBeEqualTo "foo"
        f.markupId.shouldBeNull()
    }

    @Test
    fun usingWithMarkupId_doesAddMarkupId() {
        val f = SimpleFilterPanelChangeEvent(targetSpy).withMarkupId("bar")
        f.id.shouldBeNull()
        f.markupId shouldBeEqualTo "bar"
    }

    @Test
    fun usingWithIdAndMarkupId_doesAddBoth() {
        val f = SimpleFilterPanelChangeEvent(targetSpy)
            .withId("hups")
            .withMarkupId("goo")
        f.id shouldBeEqualTo "hups"
        f.markupId shouldBeEqualTo "goo"
    }

    @Test
    fun canOverrideTarget() {
        e.target shouldBeEqualTo targetSpy

        val targetDummy2 = AjaxRequestTargetSpy()
        e.target = targetDummy2
        e.target shouldBeEqualTo targetDummy2
    }

    @Test
    fun consideringAddingToTarget_withIdLessEvent_addsTarget() {
        e.id.shouldBeNull()
        e.considerAddingToTarget(mockComponent)
        targetSpy.components.size shouldBeEqualTo 1
    }

    @Test
    fun consideringAddingToTarget_withDifferingId_doesNotAddTarget() {
        val f = SimpleFilterPanelChangeEvent(targetSpy)
            .withId("otherId")
            .withMarkupId(MARKUP_ID)
        f.considerAddingToTarget(mockComponent)
        targetSpy.components.size shouldBeEqualTo 0
    }

    @Test
    fun consideringAddingToTarget_withSameIdButNullMarkupId_addsTarget() {
        every { mockComponent.id } returns ID
        every { mockComponent.markupId } returns MARKUP_ID
        val f = SimpleFilterPanelChangeEvent(targetSpy).withId(ID)
        f.markupId.shouldBeNull()
        f.considerAddingToTarget(mockComponent)
        targetSpy.components.size shouldBeEqualTo 1
    }

    @Test
    fun consideringAddingToTarget_withSameIdAndDifferingMarkupId_addsTarget() {
        every { mockComponent.id } returns ID
        every { mockComponent.markupId } returns MARKUP_ID
        val f = SimpleFilterPanelChangeEvent(targetSpy)
            .withId(ID)
            .withMarkupId("otherMarkupId")
        f.considerAddingToTarget(mockComponent)
        targetSpy.components.size shouldBeEqualTo 1
    }

    @Test
    fun consideringAddingToTarget_withSameIdButSameMarkupId_doesNotAddTarget() {
        val f = SimpleFilterPanelChangeEvent(targetSpy)
            .withId(ID)
            .withMarkupId(MARKUP_ID)
        f.considerAddingToTarget(mockComponent)
        targetSpy.components.size shouldBeEqualTo 0
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(SimpleFilterPanelChangeEvent::class.java)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }
}
