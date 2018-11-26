package ch.difty.scipamato.core.entity.projection;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID;
import static ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID;
import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

public class NewsletterAssociationTest {

    private NewsletterAssociation na = new NewsletterAssociation();

    @Before
    public void setUp() {
        na.setId(1);
        na.setIssue("issue");
        na.setPublicationStatusId(2);
        na.setHeadline("hl");
    }

    @Test
    public void getting() {
        assertThat(na.getId()).isEqualTo(1);
        assertThat(na.getIssue()).isEqualTo("issue");
        assertThat(na.getPublicationStatusId()).isEqualTo(2);
        assertThat(na.getHeadline()).isEqualTo("hl");
    }

    @Test
    public void displayValue() {
        assertThat(na.getDisplayValue()).isEqualTo("issue");
    }

    @Test
    public void testingToString() {
        assertThat(na.toString()).isEqualTo("NewsletterAssociation(issue=issue, publicationStatusId=2, headline=hl)");
    }

    @Test
    public void alternativeConstructor() {
        na = new NewsletterAssociation(1, "issue", 2, "hl");
        getting();
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(NewsletterAssociation.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.getName(), CREATOR_ID.getName(), MODIFIED.getName(), MODIFIER_ID.getName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    public void assertEnumFields() {
        assertThat(NewsletterAssociation.NewsletterAssociationFields.values())
            .extracting("name")
            .containsExactly("id", "issue", "publicationStatusId", "headline");
    }
}