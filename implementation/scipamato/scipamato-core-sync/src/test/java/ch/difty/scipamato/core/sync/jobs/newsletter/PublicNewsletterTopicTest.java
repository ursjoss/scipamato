package ch.difty.scipamato.core.sync.jobs.newsletter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.sync.jobs.PublicEntityTest;

class PublicNewsletterTopicTest extends PublicEntityTest {

    @Test
    void canSetGet() {
        PublicNewsletterTopic pnt = PublicNewsletterTopic
            .builder()
            .id(1)
            .langCode("lc")
            .title("t")
            .version(3)
            .created(CREATED)
            .lastModified(MODIFIED)
            .lastSynched(SYNCHED)
            .build();

        assertThat(pnt.getId()).isEqualTo(1);
        assertThat(pnt.getLangCode()).isEqualTo("lc");
        assertThat(pnt.getTitle()).isEqualTo("t");
        assertThat(pnt.getVersion()).isEqualTo(3);
        assertThat(pnt.getCreated()).isEqualTo(CREATED);
        assertThat(pnt.getLastModified()).isEqualTo(MODIFIED);
        assertThat(pnt.getLastSynched()).isEqualTo(SYNCHED);
    }
}