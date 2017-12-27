package ch.difty.scipamato.core.sync.jobs.paper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.core.sync.jobs.PublicEntityTest;

public class PublicPaperTest extends PublicEntityTest {

    @Test
    public void canSetGet() {
        PublicPaper pp = PublicPaper.builder()
            .id(1l)
            .number(2l)
            .pmId(10000)
            .authors("authors")
            .title("title")
            .location("location")
            .publicationYear(2017)
            .goals("goals")
            .methods("methods")
            .population("population")
            .result("result")
            .comment("comment")
            .codesPopulation(new Short[] { (short) 1, (short) 2 })
            .codesStudyDesign(new Short[] { (short) 3, (short) 4 })
            .codes(new String[] { "1A", "2B", "3C" })
            .version(3)
            .created(CREATED)
            .lastModified(MODIFIED)
            .lastSynched(SYNCHED)
            .build();

        assertThat(pp.getId()).isEqualTo(1l);
        assertThat(pp.getNumber()).isEqualTo(2l);
        assertThat(pp.getPmId()).isEqualTo(10000);
        assertThat(pp.getAuthors()).isEqualTo("authors");
        assertThat(pp.getTitle()).isEqualTo("title");
        assertThat(pp.getLocation()).isEqualTo("location");
        assertThat(pp.getPublicationYear()).isEqualTo(2017);
        assertThat(pp.getGoals()).isEqualTo("goals");
        assertThat(pp.getMethods()).isEqualTo("methods");
        assertThat(pp.getPopulation()).isEqualTo("population");
        assertThat(pp.getResult()).isEqualTo("result");
        assertThat(pp.getComment()).isEqualTo("comment");
        assertThat(pp.getVersion()).isEqualTo(3);
        assertThat(pp.getCreated()).isEqualTo(CREATED);
        assertThat(pp.getLastModified()).isEqualTo(MODIFIED);
        assertThat(pp.getCodesPopulation()).containsExactly((short) 1, (short) 2);
        assertThat(pp.getCodesStudyDesign()).containsExactly((short) 3, (short) 4);
        assertThat(pp.getCodes()).containsExactly("1A", "2B", "3C");
        assertThat(pp.getLastSynched()).isEqualTo(SYNCHED);
    }
}
