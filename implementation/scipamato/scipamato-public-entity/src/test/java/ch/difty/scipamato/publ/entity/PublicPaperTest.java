package ch.difty.scipamato.publ.entity;

import static org.assertj.core.api.Assertions.assertThat;

import ch.difty.scipamato.publ.entity.PublicPaper;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class PublicPaperTest extends PublicEntityTest<PublicPaper> {

    @Override
    protected PublicPaper newEntity() {
        return PublicPaper.builder()
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
    }

    protected String getToString() {
        return "PublicPaper(id=1, number=2, pmId=1000, authors=authors, title=title, location=location, publicationYear=2016, goals=goals, methods=methods, population=population, result=result, comment=comment)";
    }

    @Override
    protected void assertSpecificGetters() {
        assertThat(getEntity().getId()).isEqualTo(1l);
        assertThat(getEntity().getNumber()).isEqualTo(2l);
        assertThat(getEntity().getPmId()).isEqualTo(1000);
        assertThat(getEntity().getAuthors()).isEqualTo("authors");
        assertThat(getEntity().getTitle()).isEqualTo("title");
        assertThat(getEntity().getLocation()).isEqualTo("location");
        assertThat(getEntity().getPublicationYear()).isEqualTo(2016);
        assertThat(getEntity().getGoals()).isEqualTo("goals");
        assertThat(getEntity().getMethods()).isEqualTo("methods");
        assertThat(getEntity().getPopulation()).isEqualTo("population");
        assertThat(getEntity().getResult()).isEqualTo("result");
        assertThat(getEntity().getComment()).isEqualTo("comment");
    }

    @Override
    public void verifyEquals() {
        EqualsVerifier.forClass(PublicPaper.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(PublicPaper.CREATED, PublicPaper.MODIFIED)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

}
