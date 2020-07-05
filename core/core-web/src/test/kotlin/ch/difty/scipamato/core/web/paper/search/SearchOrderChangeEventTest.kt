package ch.difty.scipamato.core.web.paper.search

import ch.difty.scipamato.common.AjaxRequestTargetSpy
import ch.difty.scipamato.core.web.paper.SearchOrderChangeEvent
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class SearchOrderChangeEventTest {

    private lateinit var e: SearchOrderChangeEvent

    private val targetDummy = AjaxRequestTargetSpy()

    @AfterEach
    fun tearDown() {
        targetDummy.reset()
    }

    @Test
    fun canRetrieveTarget() {
        e = SearchOrderChangeEvent(targetDummy)
        e.target shouldBeEqualTo targetDummy
    }

    @Test
    fun usingMinimalConstructor_doesNotSetAnySpecialStuff() {
        e = SearchOrderChangeEvent(targetDummy)
        e.excludedId.shouldBeNull()
        e.droppedConditionId.shouldBeNull()
        e.isNewSearchOrderRequested.shouldBeFalse()
    }

    @Test
    fun usingWithExcludedPaperId_doesAddExclusionId() {
        e = SearchOrderChangeEvent(targetDummy).withExcludedPaperId(5L)
        e.excludedId shouldBeEqualTo 5L
    }

    @Test
    fun usingWithDroppedConditionId_doesAddConditionId() {
        e = SearchOrderChangeEvent(targetDummy).withDroppedConditionId(5L)
        e.droppedConditionId shouldBeEqualTo 5L
    }

    @Test
    fun requestingNewSearchOrder_setsFlagAccordingly() {
        e = SearchOrderChangeEvent(targetDummy).requestingNewSearchOrder()
        e.isNewSearchOrderRequested.shouldBeTrue()
    }

    @Test
    fun requestingNewSearchOrder_withExcludedPaperIdAndNewSearchOrderRequest_newSearchOrderRequestWins() {
        e = SearchOrderChangeEvent(targetDummy).withExcludedPaperId(5L).requestingNewSearchOrder()
        e.isNewSearchOrderRequested.shouldBeTrue()
        e.excludedId.shouldBeNull()
    }

    @Test
    fun requestingNewSearchOrder_withNewSearchOrderRequestAndThenExcludedPaperId_exclusionWins() {
        e = SearchOrderChangeEvent(targetDummy).requestingNewSearchOrder().withExcludedPaperId(5L)
        e.excludedId shouldBeEqualTo 5L
        e.isNewSearchOrderRequested.shouldBeFalse()
    }

    @Test
    fun canOverrideTarget() {
        e = SearchOrderChangeEvent(targetDummy)
        e.target shouldBeEqualTo targetDummy

        val targetDummy2 = AjaxRequestTargetSpy()
        e.target = targetDummy2
        e.target shouldBeEqualTo targetDummy2
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
