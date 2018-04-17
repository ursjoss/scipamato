package ch.difty.scipamato.core.entity.projection;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID;
import static ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class PaperSlimTest {

    private PaperSlim ps = new PaperSlim();

    @Before
    public void setUp() {
        ps.setId(1L);
        ps.setNumber(10L);
        ps.setFirstAuthor("firstAuthor");
        ps.setTitle("title");
        ps.setPublicationYear(2016);
    }

    @Test
    public void getting() {
        assertThat(ps.getId()).isEqualTo(1L);
        assertThat(ps.getNumber()).isEqualTo(10L);
        assertThat(ps.getPublicationYear()).isEqualTo(2016);
        assertThat(ps.getTitle()).isEqualTo("title");
        assertThat(ps.getFirstAuthor()).isEqualTo("firstAuthor");
    }

    @Test
    public void displayValue() {
        assertThat(ps.getDisplayValue()).isEqualTo("firstAuthor (2016): title.");
    }

    @Test
    public void testingToString() {
        assertThat(ps.toString())
            .isEqualTo("PaperSlim(number=10, firstAuthor=firstAuthor, publicationYear=2016, title=title)");
    }

    @Test
    public void alternativeConstructor() {
        ps = new PaperSlim(1L, 10L, "firstAuthor", 2016, "title");
        getting();
    }

    @Test
    public void equals() {
        EqualsVerifier.forClass(PaperSlim.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.getName(), CREATOR_ID.getName(), MODIFIED.getName(), MODIFIER_ID.getName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }
}
