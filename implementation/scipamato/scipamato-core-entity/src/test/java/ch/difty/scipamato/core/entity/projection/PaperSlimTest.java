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

public class PaperSlimTest {

    private PaperSlim ps;

    @Before
    public void setUp() {
        ps = new PaperSlim();
        ps.setId(1L);
        ps.setNumber(10L);
        ps.setFirstAuthor("firstAuthor");
        ps.setTitle("title");
        ps.setPublicationYear(2016);
        ps.setNewsletterAssociation(new NewsletterAssociation(20, "nl", 1, "hl"));
    }

    @Test
    public void getting_hasAllFields() {
        getting(20, "nl", 1, "hl");
    }

    private void getting() {
        getting(null, null, null, null);
    }

    private void getting(Integer nlId, String nlIssue, Integer nlStatus, String headLine) {
        assertThat(ps.getId()).isEqualTo(1L);
        assertThat(ps.getNumber()).isEqualTo(10L);
        assertThat(ps.getPublicationYear()).isEqualTo(2016);
        assertThat(ps.getTitle()).isEqualTo("title");
        assertThat(ps.getFirstAuthor()).isEqualTo("firstAuthor");
        if (nlId != null) {
            assertThat(ps
                .getNewsletterAssociation()
                .getId()).isEqualTo(nlId);
            assertThat(ps
                .getNewsletterAssociation()
                .getIssue()).isEqualTo(nlIssue);
            assertThat(ps
                .getNewsletterAssociation()
                .getPublicationStatusId()).isEqualTo(nlStatus);
        } else {
            assertThat(ps.getNewsletterAssociation()).isNull();
        }
    }

    @Test
    public void displayValue() {
        assertThat(ps.getDisplayValue()).isEqualTo("firstAuthor (2016): title.");
    }

    @Test
    public void testingToString() {
        assertThat(ps.toString()).isEqualTo(
            "PaperSlim(number=10, firstAuthor=firstAuthor, publicationYear=2016, title=title, newsletter=nl, headLine=hl)");
    }

    @Test
    public void testingToString_withNoNewsletter() {
        ps.setNewsletterAssociation(null);
        assertThat(ps.toString()).isEqualTo(
            "PaperSlim(number=10, firstAuthor=firstAuthor, publicationYear=2016, title=title)");
    }

    @Test
    public void testingToString_withNoHeadLine() {
        ps
            .getNewsletterAssociation()
            .setHeadLine(null);
        assertThat(ps.toString()).isEqualTo(
            "PaperSlim(number=10, firstAuthor=firstAuthor, publicationYear=2016, title=title, newsletter=nl)");
    }

    @Test
    public void alternativeConstructorwithoutNewsletter_hasAllFields_exceptNewsletter() {
        ps = new PaperSlim(1L, 10L, "firstAuthor", 2016, "title");
        getting(null, null, null, null);
    }

    @Test
    public void alternativeConstructor_withNewsletterFields() {
        ps = new PaperSlim(1L, 10L, "firstAuthor", 2016, "title", 20, "nlTitle", 1, "hl");
        getting(20, "nlTitle", 1, "hl");
    }

    @Test
    public void alternativeConstructor_withNewsletter() {
        ps = new PaperSlim(1L, 10L, "firstAuthor", 2016, "title", new NewsletterAssociation(30, "t", 3, "headLine"));
        getting(30, "t", 3, "headLine");
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(PaperSlim.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.getName(), CREATOR_ID.getName(), MODIFIED.getName(), MODIFIER_ID.getName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }
}
