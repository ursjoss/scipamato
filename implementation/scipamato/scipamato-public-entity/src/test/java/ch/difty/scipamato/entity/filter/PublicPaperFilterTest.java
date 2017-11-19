package ch.difty.scipamato.entity.filter;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.Test;

import ch.difty.scipamato.entity.PopulationCode;
import ch.difty.scipamato.entity.StudyDesignCode;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class PublicPaperFilterTest {

    @Test
    public void construct() {
        PublicPaperFilter filter = new PublicPaperFilter();
        filter.setNumber(1l);
        filter.setAuthorMask("am");
        filter.setMethodsMask("mm");
        filter.setPublicationYearFrom(2000);
        filter.setPublicationYearUntil(3000);
        filter.setPopulationCodes(Arrays.asList(PopulationCode.CHILDREN, PopulationCode.ADULTS));
        filter.setStudyDesignCodes(Arrays.asList(StudyDesignCode.EXPERIMENTAL));

        assertThat(filter.getNumber()).isEqualTo(1l);
        assertThat(filter.getAuthorMask()).isEqualTo("am");
        assertThat(filter.getMethodsMask()).isEqualTo("mm");
        assertThat(filter.getPublicationYearFrom()).isEqualTo(2000);
        assertThat(filter.getPublicationYearUntil()).isEqualTo(3000);

        assertThat(filter.getPopulationCodes()).contains(PopulationCode.CHILDREN, PopulationCode.ADULTS);
        assertThat(filter.getStudyDesignCodes()).contains(StudyDesignCode.EXPERIMENTAL);

        assertThat(filter.toString()).isEqualTo("PublicPaperFilter(number=1, authorMask=am, methodsMask=mm, publicationYearFrom=2000, publicationYearUntil=3000, "
                + "populationCodes=[CHILDREN, ADULTS], studyDesignCodes=[EXPERIMENTAL])");
    }

    @Test
    public void equals() {
        EqualsVerifier.forClass(PublicPaperFilter.class).withRedefinedSuperclass().suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS).verify();
    }

}
