package ch.difty.scipamato.core.entity.search;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class PaperFilterTest {

    private PaperFilter f = new PaperFilter();

    @Test
    public void getset() {
        f.setNumber(1l);
        f.setAuthorMask("authorMask");
        f.setMethodsMask("methodsMask");
        f.setSearchMask("searchMask");
        f.setPublicationYearFrom(2015);
        f.setPublicationYearUntil(2017);

        assertThat(f.getNumber()).isEqualTo(1l);
        assertThat(f.getAuthorMask()).isEqualTo("authorMask");
        assertThat(f.getMethodsMask()).isEqualTo("methodsMask");
        assertThat(f.getSearchMask()).isEqualTo("searchMask");
        assertThat(f.getPublicationYearFrom()).isEqualTo(2015);
        assertThat(f.getPublicationYearUntil()).isEqualTo(2017);

        assertThat(f.toString()).isEqualTo(
            "PaperFilter(number=1, authorMask=authorMask, methodsMask=methodsMask, searchMask=searchMask, publicationYearFrom=2015, publicationYearUntil=2017)");
    }

    @Test
    public void equals() {
        EqualsVerifier.forClass(PaperFilter.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

}
