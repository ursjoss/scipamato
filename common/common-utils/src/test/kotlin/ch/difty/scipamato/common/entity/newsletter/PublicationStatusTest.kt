package ch.difty.scipamato.common.entity.newsletter

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus.CANCELLED
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus.PUBLISHED
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus.WIP
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus.values
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.junit.jupiter.api.Test

internal class PublicationStatusTest {

    @Test
    fun testValues() {
        values() shouldContainAll listOf(WIP, PUBLISHED, CANCELLED)
    }

    @Test
    fun testId() {
        WIP.id shouldBeEqualTo 0
        PUBLISHED.id shouldBeEqualTo 1
        CANCELLED.id shouldBeEqualTo -1
    }

    @Test
    fun testById_withValidIds() {
        PublicationStatus.byId(0) shouldBeEqualTo WIP
        PublicationStatus.byId(1) shouldBeEqualTo PUBLISHED
        PublicationStatus.byId(-1) shouldBeEqualTo CANCELLED
    }

    @Test
    fun assertNames() {
        values().map { it.description } shouldContainAll listOf("in progress", "published", "cancelled")
    }

    @Test
    fun testById_withInvalidIds() {
        invoking { PublicationStatus.byId(-2) } shouldThrow IllegalArgumentException::class withMessage "id -2 is not supported"
        invoking { PublicationStatus.byId(2) } shouldThrow IllegalArgumentException::class withMessage "id 2 is not supported"
    }

    @Test
    fun assertIfIsInProgress() {
        WIP.isInProgress.shouldBeTrue()
        PUBLISHED.isInProgress.shouldBeFalse()
        CANCELLED.isInProgress.shouldBeFalse()
    }
}
