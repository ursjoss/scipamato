package ch.difty.scipamato.entity;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class PublicPaperTest {

    private static final LocalDateTime CREATED_DATE = LocalDateTime.parse("2017-01-01T22:15:13.111");
    private static final LocalDateTime LASTMOD_DATE = LocalDateTime.parse("2017-01-10T22:15:13.111");

    PublicPaper p;

    @Before
    public void setUp() {
        p = PublicPaper
            .builder()
            .id(1l)
            .number(2l)
            .pmId(1000)
            .authors("authors")
            .title("title")
            .location("location")
            .publicationYear(2016)
            .goals("goals")
            .methods("methods")
            .population("population")
            .result("result")
            .comment("comment")
            .build();
        p.setCreated(CREATED_DATE);
        p.setLastModified(LASTMOD_DATE);
        p.setVersion(10);
    }

    @Test
    public void setGet() {
        assertThat(p.getId()).isEqualTo(1l);
        assertThat(p.getNumber()).isEqualTo(2l);
        assertThat(p.getPmId()).isEqualTo(1000);
        assertThat(p.getAuthors()).isEqualTo("authors");
        assertThat(p.getTitle()).isEqualTo("title");
        assertThat(p.getLocation()).isEqualTo("location");
        assertThat(p.getPublicationYear()).isEqualTo(2016);
        assertThat(p.getGoals()).isEqualTo("goals");
        assertThat(p.getMethods()).isEqualTo("methods");
        assertThat(p.getPopulation()).isEqualTo("population");
        assertThat(p.getResult()).isEqualTo("result");
        assertThat(p.getComment()).isEqualTo("comment");

        assertThat(p.getCreated()).isEqualTo(CREATED_DATE);
        assertThat(p.getLastModified()).isEqualTo(LASTMOD_DATE);
        assertThat(p.getVersion()).isEqualTo(10);
    }

    @Test
    public void testingToString() {
        // @formatter:off
        assertThat(p.toString()).isEqualTo(
                "PublicPaper(id=1, number=2, pmId=1000, authors=authors, title=title, location=location, publicationYear=2016, goals=goals, methods=methods, population=population, result=result, comment=comment)");
        // @formatter:on
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(PublicPaper.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(PublicPaper.CREATED, PublicPaper.MODIFIED)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }
}
