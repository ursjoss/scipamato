package ch.difty.scipamato.core.sync.jobs.newsletter;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;

import org.junit.Test;

import ch.difty.scipamato.core.sync.jobs.PublicEntityTest;

public class PublicNewStudyTest extends PublicEntityTest {

    private static final Date ISSUE_DATE = Date.valueOf("2018-06-14");

    @Test
    public void canSetGet() {
        PublicNewStudy pns = PublicNewStudy
            .builder()
            .newsletterId(1)
            .newsletterTopicId(2)
            .sort(3)
            .paperNumber(4l)
            .year(2018)
            .authors("a")
            .headline("hl")
            .description("d")
            .version(5)
            .created(CREATED)
            .lastModified(MODIFIED)
            .lastSynched(SYNCHED)
            .build();

        assertThat(pns.getNewsletterId()).isEqualTo(1);
        assertThat(pns.getNewsletterTopicId()).isEqualTo(2);
        assertThat(pns.getSort()).isEqualTo(3);
        assertThat(pns.getPaperNumber()).isEqualTo(4);
        assertThat(pns.getYear()).isEqualTo(2018);
        assertThat(pns.getAuthors()).isEqualTo("a");
        assertThat(pns.getHeadline()).isEqualTo("hl");
        assertThat(pns.getDescription()).isEqualTo("d");
        assertThat(pns.getVersion()).isEqualTo(5);
        assertThat(pns.getCreated()).isEqualTo(CREATED);
        assertThat(pns.getLastModified()).isEqualTo(MODIFIED);
        assertThat(pns.getLastSynched()).isEqualTo(SYNCHED);
    }
}