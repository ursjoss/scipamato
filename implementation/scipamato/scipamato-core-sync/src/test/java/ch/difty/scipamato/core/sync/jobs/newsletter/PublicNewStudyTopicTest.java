package ch.difty.scipamato.core.sync.jobs.newsletter;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;

import org.junit.Test;

import ch.difty.scipamato.core.sync.jobs.PublicEntityTest;

public class PublicNewStudyTopicTest extends PublicEntityTest {

    private static final Date ISSUE_DATE = Date.valueOf("2018-06-14");

    @Test
    public void canSetGet() {
        PublicNewStudyTopic pnst = PublicNewStudyTopic
            .builder()
            .newsletterId(2)
            .newsletterTopicId(3)
            .sort(4)
            .version(5)
            .created(CREATED)
            .lastModified(MODIFIED)
            .lastSynched(SYNCHED)
            .build();

        assertThat(pnst.getNewsletterId()).isEqualTo(2);
        assertThat(pnst.getNewsletterTopicId()).isEqualTo(3);
        assertThat(pnst.getSort()).isEqualTo(4);
        assertThat(pnst.getVersion()).isEqualTo(5);
        assertThat(pnst.getCreated()).isEqualTo(CREATED);
        assertThat(pnst.getLastModified()).isEqualTo(MODIFIED);
        assertThat(pnst.getLastSynched()).isEqualTo(SYNCHED);
    }
}