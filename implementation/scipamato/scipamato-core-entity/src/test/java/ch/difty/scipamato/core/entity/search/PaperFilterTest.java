package ch.difty.scipamato.core.entity.search;

import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class PaperFilterTest {

    private final PaperFilter f = new PaperFilter();

    @Test
    public void getAndSet() {
        f.setNumber(1L);
        f.setAuthorMask("authorMask");
        f.setMethodsMask("methodsMask");
        f.setSearchMask("searchMask");
        f.setPublicationYearFrom(2015);
        f.setPublicationYearUntil(2017);
        f.setNewsletterId(2);

        assertThat(f.getNumber()).isEqualTo(1L);
        assertThat(f.getAuthorMask()).isEqualTo("authorMask");
        assertThat(f.getMethodsMask()).isEqualTo("methodsMask");
        assertThat(f.getSearchMask()).isEqualTo("searchMask");
        assertThat(f.getPublicationYearFrom()).isEqualTo(2015);
        assertThat(f.getPublicationYearUntil()).isEqualTo(2017);
        assertThat(f.getNewsletterId()).isEqualTo(2);

        assertThat(f.toString()).isEqualTo(
            "PaperFilter(number=1, authorMask=authorMask, methodsMask=methodsMask, searchMask=searchMask, publicationYearFrom=2015, publicationYearUntil=2017, newsletterId=2)");
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(PaperFilter.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    public void assertEnumFields() {
        assertThat(PaperFilter.PaperFilterFields.values())
            .extracting("name")
            .containsExactly("number", "authorMask", "methodsMask", "searchMask", "publicationYearFrom",
                "publicationYearUntil", "newsletterId");
    }

}
