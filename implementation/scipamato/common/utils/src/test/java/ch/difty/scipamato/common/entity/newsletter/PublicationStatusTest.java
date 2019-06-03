package ch.difty.scipamato.common.entity.newsletter;

import static ch.difty.scipamato.common.entity.newsletter.PublicationStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class PublicationStatusTest {

    @Test
    void testValues() {
        assertThat(values()).containsExactly(WIP, PUBLISHED, CANCELLED);
    }

    @Test
    void testId() {
        assertThat(WIP.getId()).isEqualTo(0);
        assertThat(PUBLISHED.getId()).isEqualTo(1);
        assertThat(CANCELLED.getId()).isEqualTo(-1);
    }

    @Test
    void testById_withValidIds() {
        assertThat(PublicationStatus.byId(0)).isEqualTo(WIP);
        assertThat(PublicationStatus.byId(1)).isEqualTo(PUBLISHED);
        assertThat(PublicationStatus.byId(-1)).isEqualTo(CANCELLED);
    }

    @Test
    void assertNames() {
        assertThat(PublicationStatus.values())
            .extracting("name")
            .containsExactly("in progress", "published", "cancelled");
    }

    @Test
    void testById_withInvalidIds() {
        try {
            PublicationStatus.byId(-2);
            fail("should have thrown");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id -2 is not supported");
        }
        try {
            PublicationStatus.byId(2);
            fail("should have thrown");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id 2 is not supported");
        }
    }

    @Test
    void assertIfIsInProgress() {
        assertThat(PublicationStatus.WIP.isInProgress()).isTrue();
        assertThat(PublicationStatus.PUBLISHED.isInProgress()).isFalse();
        assertThat(PublicationStatus.CANCELLED.isInProgress()).isFalse();
    }
}
