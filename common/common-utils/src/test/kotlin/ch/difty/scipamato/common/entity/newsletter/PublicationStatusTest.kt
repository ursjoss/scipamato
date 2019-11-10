package ch.difty.scipamato.common.entity.newsletter

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus.CANCELLED
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus.PUBLISHED
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus.WIP
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus.values
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test

internal class PublicationStatusTest {

    @Test
    fun testValues() {
        assertThat(values()).containsExactly(WIP, PUBLISHED, CANCELLED)
    }

    @Test
    fun testId() {
        assertThat(WIP.id).isEqualTo(0)
        assertThat(PUBLISHED.id).isEqualTo(1)
        assertThat(CANCELLED.id).isEqualTo(-1)
    }

    @Test
    fun testById_withValidIds() {
        assertThat(PublicationStatus.byId(0)).isEqualTo(WIP)
        assertThat(PublicationStatus.byId(1)).isEqualTo(PUBLISHED)
        assertThat(PublicationStatus.byId(-1)).isEqualTo(CANCELLED)
    }

    @Test
    fun assertNames() {
        assertThat(values().map { it.description })
            .containsExactly("in progress", "published", "cancelled")
    }

    @Test
    fun testById_withInvalidIds() {
        try {
            PublicationStatus.byId(-2)
            fail<Any>("should have thrown")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("id -2 is not supported")
        }

        try {
            PublicationStatus.byId(2)
            fail<Any>("should have thrown")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("id 2 is not supported")
        }
    }

    @Test
    fun assertIfIsInProgress() {
        assertThat(WIP.isInProgress).isTrue()
        assertThat(PUBLISHED.isInProgress).isFalse()
        assertThat(CANCELLED.isInProgress).isFalse()
    }
}
