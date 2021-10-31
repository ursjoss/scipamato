package ch.difty.scipamato.publ.web.paper.browse

import ch.difty.scipamato.common.AjaxRequestTargetSpy
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.apache.wicket.markup.html.form.TextArea
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val ID = "id"
private const val MARKUP_ID = "mId"

@Suppress("SpellCheckingInspection")
class SimpleFilterPanelChangeEventTest {

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
        SimpleFilterPanelChangeEvent(target = targetSpy, id = "foo").apply {
            id shouldBeEqualTo "foo"
            markupId.shouldBeNull()
        }
    }

    @Test
    fun usingWithMarkupId_doesAddMarkupId() {
        SimpleFilterPanelChangeEvent(target = targetSpy, markupId = "bar").apply {
            id.shouldBeNull()
            markupId shouldBeEqualTo "bar"
        }
    }

    @Test
    fun usingWithIdAndMarkupId_doesAddBoth() {
        SimpleFilterPanelChangeEvent(target = targetSpy, id = "hups", markupId = "goo").apply {
            id shouldBeEqualTo "hups"
            markupId shouldBeEqualTo "goo"
        }
    }

    @Test
    fun consideringAddingToTarget_withIdLessEvent_addsTarget() {
        e.id.shouldBeNull()
        e.considerAddingToTarget(mockComponent)
        targetSpy.components.size shouldBeEqualTo 1
    }

    @Test
    fun consideringAddingToTarget_withDifferingId_doesNotAddTarget() {
        SimpleFilterPanelChangeEvent(target = targetSpy, id = "otherId", markupId = MARKUP_ID).apply {
            considerAddingToTarget(mockComponent)
        }
        targetSpy.components.size shouldBeEqualTo 0
    }

    @Test
    fun consideringAddingToTarget_withSameIdButNullMarkupId_addsTarget() {
        every { mockComponent.id } returns ID
        every { mockComponent.markupId } returns MARKUP_ID
        SimpleFilterPanelChangeEvent(targetSpy, id = ID).apply {
            markupId.shouldBeNull()
            considerAddingToTarget(mockComponent)
        }
        targetSpy.components.size shouldBeEqualTo 1
    }

    @Test
    fun consideringAddingToTarget_withSameIdAndDifferingMarkupId_addsTarget() {
        every { mockComponent.id } returns ID
        every { mockComponent.markupId } returns MARKUP_ID
        SimpleFilterPanelChangeEvent(target = targetSpy, id = ID, markupId = "otherMarkupId").apply {
            considerAddingToTarget(mockComponent)
        }
        targetSpy.components.size shouldBeEqualTo 1
    }

    @Test
    fun consideringAddingToTarget_withSameIdButSameMarkupId_doesNotAddTarget() {
        SimpleFilterPanelChangeEvent(target = targetSpy, id = ID, markupId = MARKUP_ID).apply {
            considerAddingToTarget(mockComponent)
        }
        targetSpy.components.size shouldBeEqualTo 0
    }
}
