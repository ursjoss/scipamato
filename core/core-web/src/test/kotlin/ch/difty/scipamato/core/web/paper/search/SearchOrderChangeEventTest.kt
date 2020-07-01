package ch.difty.scipamato.core.web.paper.search

import ch.difty.scipamato.core.web.paper.SearchOrderChangeEvent
import io.mockk.mockk
import io.mockk.unmockkAll
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.apache.wicket.ajax.AjaxRequestTarget
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SearchOrderChangeEventTest {

    private lateinit var e: SearchOrderChangeEvent

    private lateinit var targetMock: AjaxRequestTarget

    @BeforeEach
    fun setUp() {
        targetMock = mockk()
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun canRetrieveTarget() {
        e = SearchOrderChangeEvent(targetMock)
        e.target shouldBeEqualTo targetMock
    }

    @Test
    fun usingMinimalConstructor_doesNotSetAnySpecialStuff() {
        e = SearchOrderChangeEvent(targetMock)
        e.excludedId.shouldBeNull()
        e.droppedConditionId.shouldBeNull()
        e.isNewSearchOrderRequested.shouldBeFalse()
    }

    @Test
    fun usingWithExcludedPaperId_doesAddExclusionId() {
        e = SearchOrderChangeEvent(targetMock).withExcludedPaperId(5L)
        e.excludedId shouldBeEqualTo 5L
    }

    @Test
    fun usingWithDroppedConditionId_doesAddConditionId() {
        e = SearchOrderChangeEvent(targetMock).withDroppedConditionId(5L)
        e.droppedConditionId shouldBeEqualTo 5L
    }

    @Test
    fun requestingNewSearchOrder_setsFlagAccordingly() {
        e = SearchOrderChangeEvent(targetMock).requestingNewSearchOrder()
        e.isNewSearchOrderRequested.shouldBeTrue()
    }

    @Test
    fun requestingNewSearchOrder_withExcludedPaperIdAndNewSearchOrderRequest_newSearchOrderRequestWins() {
        e = SearchOrderChangeEvent(targetMock).withExcludedPaperId(5L).requestingNewSearchOrder()
        e.isNewSearchOrderRequested.shouldBeTrue()
        e.excludedId.shouldBeNull()
    }

    @Test
    fun requestingNewSearchOrder_withNewSearchOrderRequestAndThenExcludedPaperId_exclusionWins() {
        e = SearchOrderChangeEvent(targetMock).requestingNewSearchOrder().withExcludedPaperId(5L)
        e.excludedId shouldBeEqualTo 5L
        e.isNewSearchOrderRequested.shouldBeFalse()
    }

    @Test
    fun canOverrideTarget() {
        e = SearchOrderChangeEvent(targetMock)
        e.target shouldBeEqualTo targetMock

        val targetMock2 = mockk<AjaxRequestTarget>()
        e.target = targetMock2
        e.target shouldBeEqualTo targetMock2
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(SearchOrderChangeEvent::class.java)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }
}
