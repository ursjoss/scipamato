package ch.difty.sipamato.entity;

import static ch.difty.sipamato.entity.Paper.FLD_DOI;
import static ch.difty.sipamato.entity.Paper.FLD_FIRST_AUTHOR_OVERRIDDEN;
import static ch.difty.sipamato.entity.Paper.FLD_ID;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

// TODO test codes
public class ComplexPaperFilterTest {

    private static final String X = "x";

    private final ComplexPaperFilter f = new ComplexPaperFilter();

    @Test
    public void allStringSearchTerms() {
        f.setDoi(X);
        f.setPmId(X);
        f.setAuthors(X);
        f.setFirstAuthor(X);
        f.setTitle(X);
        f.setLocation(X);
        f.setGoals(X);
        f.setPopulation(X);
        f.setPopulationPlace(X);
        f.setPopulationParticipants(X);
        f.setPopulationDuration(X);
        f.setExposureAssessment(X);
        f.setExposurePollutant(X);
        f.setMethods(X);
        f.setMethodStudyDesign(X);
        f.setMethodOutcome(X);
        f.setMethodStatistics(X);
        f.setMethodConfounders(X);
        f.setResult(X);
        f.setResultExposureRange(X);
        f.setResultEffectEstimate(X);
        f.setComment(X);
        f.setIntern(X);
        f.setMainCodeOfCodeclass1(X);
        assertThat(f.getStringSearchTerms()).hasSize(24);
        assertThat(f.getIntegerSearchTerms()).isEmpty();
        assertThat(f.getBooleanSearchTerms()).isEmpty();
    }

    @Test
    public void allIntegerSearchTerms() {
        f.setId("3");
        f.setPublicationYear("2017");
        assertThat(f.getStringSearchTerms()).isEmpty();
        assertThat(f.getIntegerSearchTerms()).hasSize(2);
        assertThat(f.getBooleanSearchTerms()).isEmpty();
    }

    @Test
    public void allBooleanSearchTerms() {
        f.setFirstAuthorOverridden(true);
        assertThat(f.getStringSearchTerms()).isEmpty();
        assertThat(f.getIntegerSearchTerms()).isEmpty();
        assertThat(f.getBooleanSearchTerms()).hasSize(1);
    }

    @Test
    public void id_extensiveTest() {
        assertThat(f.getId()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
        assertThat(f.getIntegerSearchTerms()).isEmpty();
        assertThat(f.getBooleanSearchTerms()).isEmpty();

        f.setId("5");
        assertThat(f.getId()).isEqualTo("5");
        assertThat(f.getStringSearchTerms()).isEmpty();
        assertThat(f.getIntegerSearchTerms()).hasSize(1);
        assertThat(f.getBooleanSearchTerms()).isEmpty();
        IntegerSearchTerm st = f.getIntegerSearchTerms().iterator().next();
        assertThat(st.getKey()).isEqualTo(FLD_ID);
        assertThat(st.getRawValue()).isEqualTo("5");

        f.setId("10");
        assertThat(f.getId()).isEqualTo("10");
        assertThat(f.getStringSearchTerms()).isEmpty();
        assertThat(f.getIntegerSearchTerms()).hasSize(1);
        assertThat(f.getBooleanSearchTerms()).isEmpty();
        st = f.getIntegerSearchTerms().iterator().next();
        assertThat(st.getKey()).isEqualTo(FLD_ID);
        assertThat(st.getRawValue()).isEqualTo("10");

        f.setId(null);
        assertThat(f.getId()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
        assertThat(f.getIntegerSearchTerms()).isEmpty();
        assertThat(f.getBooleanSearchTerms()).isEmpty();
    }

    @Test
    public void doi_extensiveTest() {
        assertThat(f.getDoi()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
        assertThat(f.getIntegerSearchTerms()).isEmpty();
        assertThat(f.getBooleanSearchTerms()).isEmpty();

        f.setDoi("101111");
        assertThat(f.getDoi()).isEqualTo("101111");
        assertThat(f.getStringSearchTerms()).hasSize(1);
        assertThat(f.getIntegerSearchTerms()).isEmpty();
        assertThat(f.getBooleanSearchTerms()).isEmpty();
        StringSearchTerm st = f.getStringSearchTerms().iterator().next();
        assertThat(st.getKey()).isEqualTo(FLD_DOI);
        assertThat(st.getRawValue()).isEqualTo("101111");

        f.setDoi("102222");
        assertThat(f.getDoi()).isEqualTo("102222");
        assertThat(f.getStringSearchTerms()).hasSize(1);
        assertThat(f.getIntegerSearchTerms()).isEmpty();
        assertThat(f.getBooleanSearchTerms()).isEmpty();
        st = f.getStringSearchTerms().iterator().next();
        assertThat(st.getKey()).isEqualTo(FLD_DOI);
        assertThat(st.getRawValue()).isEqualTo("102222");

        f.setDoi(null);
        assertThat(f.getDoi()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
        assertThat(f.getIntegerSearchTerms()).isEmpty();
        assertThat(f.getBooleanSearchTerms()).isEmpty();
    }

    @Test
    public void pmId() {
        assertThat(f.getPmId()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setPmId(X);
        assertThat(f.getPmId()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setPmId(null);
        assertThat(f.getPmId()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void authors() {
        assertThat(f.getAuthors()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setAuthors(X);
        assertThat(f.getAuthors()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setAuthors(null);
        assertThat(f.getAuthors()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void firstAuthor() {
        assertThat(f.getFirstAuthor()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setFirstAuthor(X);
        assertThat(f.getFirstAuthor()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setFirstAuthor(null);
        assertThat(f.getFirstAuthor()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void firstAuthorOverridden_extensiveTest() {
        assertThat(f.isFirstAuthorOverridden()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
        assertThat(f.getIntegerSearchTerms()).isEmpty();
        assertThat(f.getBooleanSearchTerms()).isEmpty();

        f.setFirstAuthorOverridden(true);
        assertThat(f.isFirstAuthorOverridden()).isTrue();
        assertThat(f.getStringSearchTerms()).isEmpty();
        assertThat(f.getIntegerSearchTerms()).isEmpty();
        assertThat(f.getBooleanSearchTerms()).hasSize(1);
        BooleanSearchTerm st = f.getBooleanSearchTerms().iterator().next();
        assertThat(st.key).isEqualTo(FLD_FIRST_AUTHOR_OVERRIDDEN);
        assertThat(st.rawValue).isEqualTo(true);

        f.setFirstAuthorOverridden(false);
        assertThat(f.isFirstAuthorOverridden()).isFalse();
        assertThat(f.getStringSearchTerms()).isEmpty();
        assertThat(f.getIntegerSearchTerms()).isEmpty();
        assertThat(f.getBooleanSearchTerms()).hasSize(1);
        st = f.getBooleanSearchTerms().iterator().next();
        assertThat(st.key).isEqualTo(FLD_FIRST_AUTHOR_OVERRIDDEN);
        assertThat(st.rawValue).isEqualTo(false);

        f.setFirstAuthorOverridden(null);
        assertThat(f.isFirstAuthorOverridden()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
        assertThat(f.getIntegerSearchTerms()).isEmpty();
        assertThat(f.getBooleanSearchTerms()).isEmpty();
    }

    @Test
    public void title() {
        assertThat(f.getTitle()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setTitle(X);
        assertThat(f.getTitle()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setTitle(null);
        assertThat(f.getTitle()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void location() {
        assertThat(f.getLocation()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setLocation(X);
        assertThat(f.getLocation()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setLocation(null);
        assertThat(f.getLocation()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void publicationYear() {
        assertThat(f.getPublicationYear()).isNull();
        assertThat(f.getIntegerSearchTerms()).isEmpty();

        f.setPublicationYear("2016");
        assertThat(f.getPublicationYear()).isEqualTo("2016");
        assertThat(f.getIntegerSearchTerms()).hasSize(1);

        f.setPublicationYear(null);
        assertThat(f.getPublicationYear()).isNull();
        assertThat(f.getIntegerSearchTerms()).isEmpty();
    }

    @Test
    public void goals() {
        assertThat(f.getGoals()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setGoals(X);
        assertThat(f.getGoals()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setGoals(null);
        assertThat(f.getGoals()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void population() {
        assertThat(f.getPopulation()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setPopulation(X);
        assertThat(f.getPopulation()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setPopulation(null);
        assertThat(f.getPopulation()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void populationPlace() {
        assertThat(f.getPopulationPlace()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setPopulationPlace(X);
        assertThat(f.getPopulationPlace()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setPopulationPlace(null);
        assertThat(f.getPopulationPlace()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void populationParticipants() {
        assertThat(f.getPopulationParticipants()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setPopulationParticipants(X);
        assertThat(f.getPopulationParticipants()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setPopulationParticipants(null);
        assertThat(f.getPopulationParticipants()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void populationDuration() {
        assertThat(f.getPopulationDuration()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setPopulationDuration(X);
        assertThat(f.getPopulationDuration()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setPopulationDuration(null);
        assertThat(f.getPopulationDuration()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void exposurePollutant() {
        assertThat(f.getExposurePollutant()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setExposurePollutant(X);
        assertThat(f.getExposurePollutant()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setExposurePollutant(null);
        assertThat(f.getExposurePollutant()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void exposureAssessment() {
        assertThat(f.getExposureAssessment()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setExposureAssessment(X);
        assertThat(f.getExposureAssessment()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setExposureAssessment(null);
        assertThat(f.getExposureAssessment()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void methods() {
        assertThat(f.getMethods()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setMethods(X);
        assertThat(f.getMethods()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setMethods(null);
        assertThat(f.getMethods()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void methodStudyDesign() {
        assertThat(f.getMethodStudyDesign()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setMethodStudyDesign(X);
        assertThat(f.getMethodStudyDesign()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setMethodStudyDesign(null);
        assertThat(f.getMethodStudyDesign()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void methodOutcome() {
        assertThat(f.getMethodOutcome()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setMethodOutcome(X);
        assertThat(f.getMethodOutcome()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setMethodOutcome(null);
        assertThat(f.getMethodOutcome()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void methodStatistics() {
        assertThat(f.getMethodStatistics()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setMethodStatistics(X);
        assertThat(f.getMethodStatistics()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setMethodStatistics(null);
        assertThat(f.getMethodStatistics()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void methodConfounders() {
        assertThat(f.getMethodConfounders()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setMethodConfounders(X);
        assertThat(f.getMethodConfounders()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setMethodConfounders(null);
        assertThat(f.getMethodConfounders()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void result() {
        assertThat(f.getResult()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setResult(X);
        assertThat(f.getResult()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setResult(null);
        assertThat(f.getResult()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void resultExposureRange() {
        assertThat(f.getResultExposureRange()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setResultExposureRange(X);
        assertThat(f.getResultExposureRange()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setResultExposureRange(null);
        assertThat(f.getResultExposureRange()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void resultEffectEstimate() {
        assertThat(f.getResultEffectEstimate()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setResultEffectEstimate(X);
        assertThat(f.getResultEffectEstimate()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setResultEffectEstimate(null);
        assertThat(f.getResultEffectEstimate()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void comment() {
        assertThat(f.getComment()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setComment(X);
        assertThat(f.getComment()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setComment(null);
        assertThat(f.getComment()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void intern() {
        assertThat(f.getIntern()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setIntern(X);
        assertThat(f.getIntern()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setIntern(null);
        assertThat(f.getIntern()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void mainCodeOfClass1() {
        assertThat(f.getMainCodeOfCodeclass1()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();

        f.setMainCodeOfCodeclass1(X);
        assertThat(f.getMainCodeOfCodeclass1()).isEqualTo(X);
        assertThat(f.getStringSearchTerms()).hasSize(1);

        f.setMainCodeOfCodeclass1(null);
        assertThat(f.getMainCodeOfCodeclass1()).isNull();
        assertThat(f.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void testToString_withSingleStringSearchTerms_returnsIt() {
        f.setAuthors("hoops");
        assertThat(f.toString()).isEqualTo("hoops");
    }

    @Test
    public void testToString_withTwoStringSearchTerms_joinsThemUsingAnd() {
        f.setAuthors("rag");
        f.setMethodConfounders("bones");
        assertThat(f.toString()).isEqualTo("rag AND bones");
    }

    @Test
    public void testToString_forBooleanSearchTerms_onlyConsidersTrueOnes() {
        f.setFirstAuthorOverridden(false);
        assertThat(f.toString()).isEqualTo("");
    }

    @Test
    public void testToString_withMultipleSearchTerms_joinsThemAllUsingAND() {
        f.setAuthors("fooAuth");
        f.setMethodStudyDesign("bar");
        f.setDoi("baz");
        f.setPublicationYear("2016");
        f.setFirstAuthorOverridden(true);
        assertThat(f.toString()).isEqualTo("fooAuth AND bar AND baz AND 2016 AND first_author_overridden");
    }

    @Test
    public void equalsAndHash() {
        assertThat(f.hashCode()).isEqualTo(30784);
        assertThat(f.equals(f)).isTrue();
        assertThat(f.equals(null)).isFalse();
        assertThat(f.equals(new String())).isFalse();
    }

    @Test
    public void equalsAndHash2() {
        ComplexPaperFilter f1 = new ComplexPaperFilter();
        ComplexPaperFilter f2 = new ComplexPaperFilter();
        assertEquality(f1, f2, 30784);
    }

    private void assertEquality(ComplexPaperFilter f1, ComplexPaperFilter f2, int hashCode) {
        assertThat(f1.hashCode()).isEqualTo(f2.hashCode());
        assertThat(f2.hashCode()).isEqualTo(hashCode);
        assertThat(f1.equals(f2)).isTrue();
        assertThat(f2.equals(f1)).isTrue();
    }

    @Test
    public void equalsAndHash3() {
        ComplexPaperFilter f1 = new ComplexPaperFilter();
        f1.setAuthors("foo");
        ComplexPaperFilter f2 = new ComplexPaperFilter();
        f2.setAuthors("foo");
        assertEquality(f1, f2, -1401938339);
    }

    @Test
    public void equalsAndHash4() {
        ComplexPaperFilter f1 = new ComplexPaperFilter();
        f1.setAuthors("foo");
        f1.setComment("bar");
        f1.setPublicationYear("2014");
        f1.setFirstAuthor("baz");
        f1.setFirstAuthorOverridden(true);
        f1.setMethodOutcome("blup");
        ComplexPaperFilter f2 = new ComplexPaperFilter();
        f2.setAuthors("foo");
        f2.setComment("bar");
        f2.setPublicationYear("2014");
        f2.setFirstAuthor("baz");
        f2.setFirstAuthorOverridden(true);
        f2.setMethodOutcome("blup");
        assertEquality(f1, f2, 2003277035);

        f2.setMethodOutcome("blup2");
        assertThat(f1.equals(f2)).isFalse();
        assertThat(f1.hashCode()).isNotEqualTo(f2.hashCode());

        f2.setMethodOutcome("blup");
        f2.setMethodStatistics("bloop");
        assertThat(f1.equals(f2)).isFalse();
        assertThat(f1.hashCode()).isNotEqualTo(f2.hashCode());
    }

}
