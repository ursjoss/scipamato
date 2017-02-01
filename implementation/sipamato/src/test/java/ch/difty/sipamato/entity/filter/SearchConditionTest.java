package ch.difty.sipamato.entity.filter;

import static ch.difty.sipamato.entity.Paper.FLD_DOI;
import static ch.difty.sipamato.entity.Paper.FLD_FIRST_AUTHOR_OVERRIDDEN;
import static ch.difty.sipamato.entity.Paper.FLD_ID;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.sipamato.entity.Code;

public class SearchConditionTest {

    private static final long SEARCH_CONDITION_ID = 1;
    private static final String X = "x";

    private final SearchCondition sc = new SearchCondition(1l);

    @Test
    public void allStringSearchTerms() {
        sc.setDoi(X);
        sc.setPmId(X);
        sc.setAuthors(X);
        sc.setFirstAuthor(X);
        sc.setTitle(X);
        sc.setLocation(X);
        sc.setGoals(X);
        sc.setPopulation(X);
        sc.setPopulationPlace(X);
        sc.setPopulationParticipants(X);
        sc.setPopulationDuration(X);
        sc.setExposureAssessment(X);
        sc.setExposurePollutant(X);
        sc.setMethods(X);
        sc.setMethodStudyDesign(X);
        sc.setMethodOutcome(X);
        sc.setMethodStatistics(X);
        sc.setMethodConfounders(X);
        sc.setResult(X);
        sc.setResultExposureRange(X);
        sc.setResultEffectEstimate(X);
        sc.setResultMeasuredOutcome(X);
        sc.setComment(X);
        sc.setIntern(X);
        sc.setOriginalAbstract(X);
        sc.setMainCodeOfCodeclass1(X);
        assertThat(sc.getStringSearchTerms()).hasSize(26);
        assertThat(sc.getIntegerSearchTerms()).isEmpty();
        assertThat(sc.getBooleanSearchTerms()).isEmpty();
        assertThat(sc.getCreatedDisplayValue()).isNull();
        assertThat(sc.getModifiedDisplayValue()).isNull();

        assertThat(sc.getSearchConditionId()).isEqualTo(SEARCH_CONDITION_ID);
    }

    @Test
    public void allIntegerSearchTerms() {
        sc.setId("3");
        sc.setPublicationYear("2017");
        assertThat(sc.getStringSearchTerms()).isEmpty();
        assertThat(sc.getIntegerSearchTerms()).hasSize(2);
        assertThat(sc.getBooleanSearchTerms()).isEmpty();
        assertThat(sc.getCreatedDisplayValue()).isNull();
        assertThat(sc.getModifiedDisplayValue()).isNull();

        assertThat(sc.getSearchConditionId()).isEqualTo(SEARCH_CONDITION_ID);
    }

    @Test
    public void allBooleanSearchTerms() {
        sc.setFirstAuthorOverridden(true);
        assertThat(sc.getStringSearchTerms()).isEmpty();
        assertThat(sc.getIntegerSearchTerms()).isEmpty();
        assertThat(sc.getBooleanSearchTerms()).hasSize(1);
        assertThat(sc.getCreatedDisplayValue()).isNull();
        assertThat(sc.getModifiedDisplayValue()).isNull();

        assertThat(sc.getSearchConditionId()).isEqualTo(SEARCH_CONDITION_ID);
    }

    @Test
    public void createdAndModifiedDisplayValues() {
        sc.setCreatedDisplayValue(X);
        sc.setModifiedDisplayValue(X + X);
        assertThat(sc.getStringSearchTerms()).isEmpty();
        assertThat(sc.getIntegerSearchTerms()).isEmpty();
        assertThat(sc.getBooleanSearchTerms()).isEmpty();
        assertThat(sc.getCreatedDisplayValue()).isEqualTo(X);
        assertThat(sc.getModifiedDisplayValue()).isEqualTo(X + X);

        assertThat(sc.getSearchConditionId()).isEqualTo(SEARCH_CONDITION_ID);
    }

    @Test
    public void id_extensiveTest() {
        assertThat(sc.getId()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
        assertThat(sc.getIntegerSearchTerms()).isEmpty();
        assertThat(sc.getBooleanSearchTerms()).isEmpty();

        sc.setId("5");
        assertThat(sc.getId()).isEqualTo("5");
        assertThat(sc.getStringSearchTerms()).isEmpty();
        assertThat(sc.getIntegerSearchTerms()).hasSize(1);
        assertThat(sc.getBooleanSearchTerms()).isEmpty();
        IntegerSearchTerm st = sc.getIntegerSearchTerms().iterator().next();
        assertThat(st.getFieldName()).isEqualTo(FLD_ID);
        assertThat(st.getRawSearchTerm()).isEqualTo("5");

        sc.setId("10");
        assertThat(sc.getId()).isEqualTo("10");
        assertThat(sc.getStringSearchTerms()).isEmpty();
        assertThat(sc.getIntegerSearchTerms()).hasSize(1);
        assertThat(sc.getBooleanSearchTerms()).isEmpty();
        st = sc.getIntegerSearchTerms().iterator().next();
        assertThat(st.getFieldName()).isEqualTo(FLD_ID);
        assertThat(st.getRawSearchTerm()).isEqualTo("10");

        sc.setId(null);
        assertThat(sc.getId()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
        assertThat(sc.getIntegerSearchTerms()).isEmpty();
        assertThat(sc.getBooleanSearchTerms()).isEmpty();
    }

    @Test
    public void doi_extensiveTest() {
        assertThat(sc.getDoi()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
        assertThat(sc.getIntegerSearchTerms()).isEmpty();
        assertThat(sc.getBooleanSearchTerms()).isEmpty();

        sc.setDoi("101111");
        assertThat(sc.getDoi()).isEqualTo("101111");
        assertThat(sc.getStringSearchTerms()).hasSize(1);
        assertThat(sc.getIntegerSearchTerms()).isEmpty();
        assertThat(sc.getBooleanSearchTerms()).isEmpty();
        StringSearchTerm st = sc.getStringSearchTerms().iterator().next();
        assertThat(st.getFieldName()).isEqualTo(FLD_DOI);
        assertThat(st.getRawSearchTerm()).isEqualTo("101111");

        sc.setDoi("102222");
        assertThat(sc.getDoi()).isEqualTo("102222");
        assertThat(sc.getStringSearchTerms()).hasSize(1);
        assertThat(sc.getIntegerSearchTerms()).isEmpty();
        assertThat(sc.getBooleanSearchTerms()).isEmpty();
        st = sc.getStringSearchTerms().iterator().next();
        assertThat(st.getFieldName()).isEqualTo(FLD_DOI);
        assertThat(st.getRawSearchTerm()).isEqualTo("102222");

        sc.setDoi(null);
        assertThat(sc.getDoi()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
        assertThat(sc.getIntegerSearchTerms()).isEmpty();
        assertThat(sc.getBooleanSearchTerms()).isEmpty();

        assertThat(sc.getSearchConditionId()).isEqualTo(SEARCH_CONDITION_ID);
    }

    @Test
    public void pmId() {
        assertThat(sc.getPmId()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setPmId(X);
        assertThat(sc.getPmId()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setPmId(null);
        assertThat(sc.getPmId()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void authors() {
        assertThat(sc.getAuthors()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setAuthors(X);
        assertThat(sc.getAuthors()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setAuthors(null);
        assertThat(sc.getAuthors()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void firstAuthor() {
        assertThat(sc.getFirstAuthor()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setFirstAuthor(X);
        assertThat(sc.getFirstAuthor()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setFirstAuthor(null);
        assertThat(sc.getFirstAuthor()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void firstAuthorOverridden_extensiveTest() {
        assertThat(sc.isFirstAuthorOverridden()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
        assertThat(sc.getIntegerSearchTerms()).isEmpty();
        assertThat(sc.getBooleanSearchTerms()).isEmpty();

        sc.setFirstAuthorOverridden(true);
        assertThat(sc.isFirstAuthorOverridden()).isTrue();
        assertThat(sc.getStringSearchTerms()).isEmpty();
        assertThat(sc.getIntegerSearchTerms()).isEmpty();
        assertThat(sc.getBooleanSearchTerms()).hasSize(1);
        BooleanSearchTerm st = sc.getBooleanSearchTerms().iterator().next();
        assertThat(st.getFieldName()).isEqualTo(FLD_FIRST_AUTHOR_OVERRIDDEN);
        assertThat(st.getRawSearchTerm()).isEqualTo("true");
        assertThat(st.getValue()).isTrue();

        sc.setFirstAuthorOverridden(false);
        assertThat(sc.isFirstAuthorOverridden()).isFalse();
        assertThat(sc.getStringSearchTerms()).isEmpty();
        assertThat(sc.getIntegerSearchTerms()).isEmpty();
        assertThat(sc.getBooleanSearchTerms()).hasSize(1);
        st = sc.getBooleanSearchTerms().iterator().next();
        assertThat(st.getFieldName()).isEqualTo(FLD_FIRST_AUTHOR_OVERRIDDEN);
        assertThat(st.getRawSearchTerm()).isEqualTo("false");
        assertThat(st.getValue()).isFalse();

        sc.setFirstAuthorOverridden(null);
        assertThat(sc.isFirstAuthorOverridden()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
        assertThat(sc.getIntegerSearchTerms()).isEmpty();
        assertThat(sc.getBooleanSearchTerms()).isEmpty();
    }

    @Test
    public void title() {
        assertThat(sc.getTitle()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setTitle(X);
        assertThat(sc.getTitle()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setTitle(null);
        assertThat(sc.getTitle()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void location() {
        assertThat(sc.getLocation()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setLocation(X);
        assertThat(sc.getLocation()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setLocation(null);
        assertThat(sc.getLocation()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void publicationYear() {
        assertThat(sc.getPublicationYear()).isNull();
        assertThat(sc.getIntegerSearchTerms()).isEmpty();

        sc.setPublicationYear("2016");
        assertThat(sc.getPublicationYear()).isEqualTo("2016");
        assertThat(sc.getIntegerSearchTerms()).hasSize(1);

        sc.setPublicationYear(null);
        assertThat(sc.getPublicationYear()).isNull();
        assertThat(sc.getIntegerSearchTerms()).isEmpty();
    }

    @Test
    public void goals() {
        assertThat(sc.getGoals()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setGoals(X);
        assertThat(sc.getGoals()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setGoals(null);
        assertThat(sc.getGoals()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void population() {
        assertThat(sc.getPopulation()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setPopulation(X);
        assertThat(sc.getPopulation()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setPopulation(null);
        assertThat(sc.getPopulation()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void populationPlace() {
        assertThat(sc.getPopulationPlace()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setPopulationPlace(X);
        assertThat(sc.getPopulationPlace()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setPopulationPlace(null);
        assertThat(sc.getPopulationPlace()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void populationParticipants() {
        assertThat(sc.getPopulationParticipants()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setPopulationParticipants(X);
        assertThat(sc.getPopulationParticipants()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setPopulationParticipants(null);
        assertThat(sc.getPopulationParticipants()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void populationDuration() {
        assertThat(sc.getPopulationDuration()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setPopulationDuration(X);
        assertThat(sc.getPopulationDuration()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setPopulationDuration(null);
        assertThat(sc.getPopulationDuration()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void exposurePollutant() {
        assertThat(sc.getExposurePollutant()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setExposurePollutant(X);
        assertThat(sc.getExposurePollutant()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setExposurePollutant(null);
        assertThat(sc.getExposurePollutant()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void exposureAssessment() {
        assertThat(sc.getExposureAssessment()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setExposureAssessment(X);
        assertThat(sc.getExposureAssessment()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setExposureAssessment(null);
        assertThat(sc.getExposureAssessment()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void methods() {
        assertThat(sc.getMethods()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setMethods(X);
        assertThat(sc.getMethods()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setMethods(null);
        assertThat(sc.getMethods()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void methodStudyDesign() {
        assertThat(sc.getMethodStudyDesign()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setMethodStudyDesign(X);
        assertThat(sc.getMethodStudyDesign()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setMethodStudyDesign(null);
        assertThat(sc.getMethodStudyDesign()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void methodOutcome() {
        assertThat(sc.getMethodOutcome()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setMethodOutcome(X);
        assertThat(sc.getMethodOutcome()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setMethodOutcome(null);
        assertThat(sc.getMethodOutcome()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void methodStatistics() {
        assertThat(sc.getMethodStatistics()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setMethodStatistics(X);
        assertThat(sc.getMethodStatistics()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setMethodStatistics(null);
        assertThat(sc.getMethodStatistics()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void methodConfounders() {
        assertThat(sc.getMethodConfounders()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setMethodConfounders(X);
        assertThat(sc.getMethodConfounders()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setMethodConfounders(null);
        assertThat(sc.getMethodConfounders()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void result() {
        assertThat(sc.getResult()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setResult(X);
        assertThat(sc.getResult()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setResult(null);
        assertThat(sc.getResult()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void resultExposureRange() {
        assertThat(sc.getResultExposureRange()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setResultExposureRange(X);
        assertThat(sc.getResultExposureRange()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setResultExposureRange(null);
        assertThat(sc.getResultExposureRange()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void resultEffectEstimate() {
        assertThat(sc.getResultEffectEstimate()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setResultEffectEstimate(X);
        assertThat(sc.getResultEffectEstimate()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setResultEffectEstimate(null);
        assertThat(sc.getResultEffectEstimate()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void resultMeasuredOutcome() {
        assertThat(sc.getResultMeasuredOutcome()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setResultMeasuredOutcome(X);
        assertThat(sc.getResultMeasuredOutcome()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setResultMeasuredOutcome(null);
        assertThat(sc.getResultMeasuredOutcome()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void comment() {
        assertThat(sc.getComment()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setComment(X);
        assertThat(sc.getComment()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setComment(null);
        assertThat(sc.getComment()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void intern() {
        assertThat(sc.getIntern()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setIntern(X);
        assertThat(sc.getIntern()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setIntern(null);
        assertThat(sc.getIntern()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void originalAbstract() {
        assertThat(sc.getOriginalAbstract()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setOriginalAbstract(X);
        assertThat(sc.getOriginalAbstract()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setOriginalAbstract(null);
        assertThat(sc.getOriginalAbstract()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void mainCodeOfClass1() {
        assertThat(sc.getMainCodeOfCodeclass1()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setMainCodeOfCodeclass1(X);
        assertThat(sc.getMainCodeOfCodeclass1()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(1);

        sc.setMainCodeOfCodeclass1(null);
        assertThat(sc.getMainCodeOfCodeclass1()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void createdDisplayValue() {
        assertThat(sc.getCreatedDisplayValue()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setCreatedDisplayValue(X);
        assertThat(sc.getCreatedDisplayValue()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(0);

        sc.setCreatedDisplayValue(null);
        assertThat(sc.getCreatedDisplayValue()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void modifiedDisplayValue() {
        assertThat(sc.getModifiedDisplayValue()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();

        sc.setModifiedDisplayValue(X);
        assertThat(sc.getModifiedDisplayValue()).isEqualTo(X);
        assertThat(sc.getStringSearchTerms()).hasSize(0);

        sc.setModifiedDisplayValue(null);
        assertThat(sc.getModifiedDisplayValue()).isNull();
        assertThat(sc.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void testDisplayValue_withSingleStringSearchTerms_returnsIt() {
        sc.setAuthors("hoops");
        assertThat(sc.getDisplayValue()).isEqualTo("hoops");
    }

    @Test
    public void testDisplayValue_withTwoStringSearchTerms_joinsThemUsingAnd() {
        sc.setAuthors("rag");
        sc.setMethodConfounders("bones");
        assertThat(sc.getDisplayValue()).isEqualTo("rag AND bones");
    }

    @Test
    public void testDisplayValue_forBooleanSearchTermsBeginFalse() {
        sc.setFirstAuthorOverridden(false);
        assertThat(sc.getDisplayValue()).isEqualTo("-first_author_overridden");
    }

    @Test
    public void testDisplayValue_withMultipleSearchTerms_joinsThemAllUsingAND() {
        sc.setAuthors("fooAuth");
        sc.setMethodStudyDesign("bar");
        sc.setDoi("baz");
        sc.setPublicationYear("2016");
        sc.setFirstAuthorOverridden(true);
        assertThat(sc.getDisplayValue()).isEqualTo("fooAuth AND bar AND baz AND 2016 AND first_author_overridden");
    }

    @Test
    public void testDisplayValue_withCodesOnly() {
        sc.addCode(new Code("1F", "C1F", "", false, 1, "CC1", "", 0));
        sc.addCode(new Code("5H", "C5H", "", false, 5, "CC5", "", 0));
        assertThat(sc.getDisplayValue()).isEqualTo("1F&5H");
    }

    @Test
    public void testDisplayValue_withSearchTermsAndCodes() {
        sc.setAuthors("foobar");
        sc.addCode(new Code("1F", "C1F", "", false, 1, "CC1", "", 0));
        sc.addCode(new Code("5H", "C5H", "", false, 5, "CC5", "", 0));
        assertThat(sc.getDisplayValue()).isEqualTo("foobar AND 1F&5H");
    }

    @Test
    public void equalsAndHash1_ofFieldSc() {
        assertThat(sc.hashCode()).isEqualTo(-1664622465);
        assertThat(sc.equals(sc)).isTrue();
        assertThat(sc.equals(null)).isFalse();
        assertThat(sc.equals(new String())).isFalse();
    }

    @Test
    public void equalsAndHash2_withEmptySearchConditions() {
        SearchCondition f1 = new SearchCondition();
        SearchCondition f2 = new SearchCondition();
        assertEquality(f1, f2, 1742841150);
    }

    private void assertEquality(SearchCondition f1, SearchCondition f2, int hashCode) {
        assertThat(f1.hashCode()).isEqualTo(f2.hashCode());
        assertThat(f2.hashCode()).isEqualTo(hashCode);
        assertThat(f1.equals(f2)).isTrue();
        assertThat(f2.equals(f1)).isTrue();
    }

    @Test
    public void equalsAndHash3_withSingleAttribute() {
        SearchCondition f1 = new SearchCondition();
        f1.setAuthors("foo");
        SearchCondition f2 = new SearchCondition();
        f2.setAuthors("foo");
        assertEquality(f1, f2, 1231471297);
    }

    @Test
    public void equalsAndHash4_withManyAttributes() {
        SearchCondition f1 = new SearchCondition();
        f1.setAuthors("foo");
        f1.setComment("bar");
        f1.setPublicationYear("2014");
        f1.setFirstAuthor("baz");
        f1.setFirstAuthorOverridden(true);
        f1.setMethodOutcome("blup");
        SearchCondition f2 = new SearchCondition();
        f2.setAuthors("foo");
        f2.setComment("bar");
        f2.setPublicationYear("2014");
        f2.setFirstAuthor("baz");
        f2.setFirstAuthorOverridden(true);
        f2.setMethodOutcome("blup");
        assertEquality(f1, f2, -470432492);

        f2.setMethodOutcome("blup2");
        assertThat(f1.equals(f2)).isFalse();
        assertThat(f1.hashCode()).isNotEqualTo(f2.hashCode());

        f2.setMethodOutcome("blup");
        f2.setMethodStatistics("bloop");
        assertThat(f1.equals(f2)).isFalse();
        assertThat(f1.hashCode()).isNotEqualTo(f2.hashCode());
    }

    @Test
    public void equalsAndHash5_withDifferentSearchConditionIds() {
        SearchCondition f1 = new SearchCondition();
        f1.setAuthors("foo");
        SearchCondition f2 = new SearchCondition();
        f2.setAuthors("foo");
        assertEquality(f1, f2, 1231471297);

        f1.setSearchConditionId(3l);
        assertThat(f1.hashCode()).isNotEqualTo(f2.hashCode());
        assertThat(f1.equals(f2)).isFalse();
        assertThat(f2.equals(f1)).isFalse();

        f2.setSearchConditionId(4l);
        assertThat(f1.hashCode()).isNotEqualTo(f2.hashCode());
        assertThat(f1.equals(f2)).isFalse();
        assertThat(f2.equals(f1)).isFalse();

        f2.setSearchConditionId(3l);
        assertEquality(f1, f2, -400984956);
    }

    @Test
    public void equalsAndHash6_withCreatedDisplayValue() {
        SearchCondition f1 = new SearchCondition();
        f1.setCreatedDisplayValue("foo");
        SearchCondition f2 = new SearchCondition();
        assertThat(f1.equals(f2)).isFalse();

        f2.setCreatedDisplayValue("foo");
        assertEquality(f1, f2, 2027365432);

        f2.setCreatedDisplayValue("bar");
        assertThat(f1.equals(f2)).isFalse();

        f1.setCreatedDisplayValue(null);
        assertThat(f2.equals(f1)).isFalse();
    }

    @Test
    public void equalsAndHash7_withModifiedDisplayValue() {
        SearchCondition f1 = new SearchCondition();
        f1.setModifiedDisplayValue("foo");
        SearchCondition f2 = new SearchCondition();
        assertThat(f1.equals(f2)).isFalse();

        f2.setModifiedDisplayValue("foo");
        assertEquality(f1, f2, 1059282692);

        f2.setModifiedDisplayValue("bar");
        assertThat(f1.equals(f2)).isFalse();

        f1.setCreatedDisplayValue(null);
        assertThat(f2.equals(f1)).isFalse();
    }

    @Test
    public void newSearchCondition_hasEmptyRemovedKeys() {
        assertThat(new SearchCondition().getRemovedKeys()).isEmpty();
    }

    @Test
    public void addingSearchTerms_leavesRemovedKeysEmpty() {
        SearchCondition sc = new SearchCondition();
        sc.setAuthors("foo");
        sc.setPublicationYear("2014");
        sc.setFirstAuthorOverridden(true);
        assertThat(sc.getRemovedKeys()).isEmpty();
    }

    @Test
    public void removingSearchTerms_addsThemToRemovedKeys() {
        SearchCondition sc = new SearchCondition();
        sc.setAuthors("foo");
        sc.setPublicationYear("2014");
        sc.setGoals("bar");

        sc.setPublicationYear(null);
        assertThat(sc.getRemovedKeys()).hasSize(1).containsOnly("publication_year");
    }

    @Test
    public void addingSearchTerm_afterRemovingIt_removesItFromRemovedKeys() {
        SearchCondition sc = new SearchCondition();
        sc.setPublicationYear("2014");
        sc.setPublicationYear(null);
        sc.setPublicationYear("2015");
        assertThat(sc.getRemovedKeys()).isEmpty();
    }

    @Test
    public void addingSearchTerm_xafterRemovingIt_removesItFromRemovedKeys() {
        SearchCondition sc = new SearchCondition();
        sc.setAuthors("foo");
        sc.setAuthors(null);
        sc.setPublicationYear("2014");
        sc.setPublicationYear(null);
        assertThat(sc.getRemovedKeys()).hasSize(2);

        sc.clearRemovedKeys();
        assertThat(sc.getRemovedKeys()).isEmpty();
    }
}
