package ch.difty.scipamato.publ.entity;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class PublicPaperTest extends PublicEntityTest<PublicPaper> {

    @Override
    protected PublicPaper newEntity() {
        return PublicPaper
            .builder()
            .id(1L)
            .number(2L)
            .pmId(1000)
            .authors("authors")
            .authorsAbbreviated("auths")
            .title("title")
            .location("location")
            .journal("journal")
            .publicationYear(2016)
            .goals("goals")
            .methods("methods")
            .population("population")
            .result("result")
            .comment("comment")
            .build();
    }

    protected String getToString() {
        return "PublicPaper(id=1, number=2, pmId=1000, authors=authors, authorsAbbreviated=auths, title=title, location=location, journal=journal, publicationYear=2016, goals=goals, methods=methods, population=population, result=result, comment=comment)";
    }

    @Override
    protected void assertSpecificGetters() {
        assertThat(getEntity().getId()).isEqualTo(1L);
        assertThat(getEntity().getNumber()).isEqualTo(2L);
        assertThat(getEntity().getPmId()).isEqualTo(1000);
        assertThat(getEntity().getAuthors()).isEqualTo("authors");
        assertThat(getEntity().getAuthorsAbbreviated()).isEqualTo("auths");
        assertThat(getEntity().getTitle()).isEqualTo("title");
        assertThat(getEntity().getLocation()).isEqualTo("location");
        assertThat(getEntity().getJournal()).isEqualTo("journal");
        assertThat(getEntity().getPublicationYear()).isEqualTo(2016);
        assertThat(getEntity().getGoals()).isEqualTo("goals");
        assertThat(getEntity().getMethods()).isEqualTo("methods");
        assertThat(getEntity().getPopulation()).isEqualTo("population");
        assertThat(getEntity().getResult()).isEqualTo("result");
        assertThat(getEntity().getComment()).isEqualTo("comment");
    }

    @Override
    public void verifyEquals() {
        EqualsVerifier
            .forClass(PublicPaper.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.getName(), MODIFIED.getName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    public void assertEnumFields() {
        assertThat(PublicPaper.PublicPaperFields.values())
            .extracting("name")
            .containsExactly("id", "number", "pmId", "authors", "authorsAbbreviated", "title", "location", "journal",
                "publicationYear", "goals", "methods", "population", "result", "comment");
    }
}
