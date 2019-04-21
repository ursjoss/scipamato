package ch.difty.scipamato.core.sync.jobs.newsletter;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.sync.jobs.PublicEntityTest;

public class PublicNewsletterTest extends PublicEntityTest {

    private static final Date ISSUE_DATE = Date.valueOf("2018-06-14");

    @Test
    public void canSetGet() {
        PublicNewsletter pn = PublicNewsletter
            .builder()
            .id(1)
            .issue("i")
            .issueDate(ISSUE_DATE)
            .version(3)
            .created(CREATED)
            .lastModified(MODIFIED)
            .lastSynched(SYNCHED)
            .build();

        assertThat(pn.getId()).isEqualTo(1);
        assertThat(pn.getIssue()).isEqualTo("i");
        assertThat(pn.getIssueDate()).isEqualTo(ISSUE_DATE);
        assertThat(pn.getVersion()).isEqualTo(3);
        assertThat(pn.getCreated()).isEqualTo(CREATED);
        assertThat(pn.getLastModified()).isEqualTo(MODIFIED);
        assertThat(pn.getLastSynched()).isEqualTo(SYNCHED);
    }
}