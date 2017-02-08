package ch.difty.sipamato.entity.filter;

import static ch.difty.sipamato.entity.Paper.FLD_DOI;
import static ch.difty.sipamato.entity.Paper.FLD_FIRST_AUTHOR_OVERRIDDEN;
import static ch.difty.sipamato.entity.Paper.FLD_ID;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;

import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.CodeClassId;

public class SearchConditionTest {

    private static final long SEARCH_CONDITION_ID = 1;
    private static final String X = "x";

    private final SearchCondition sc1 = new SearchCondition(SEARCH_CONDITION_ID);
    private final SearchCondition sc2 = new SearchCondition();

    @Test
    public void allStringSearchTerms() {
        sc1.setDoi(X);
        sc1.setPmId(X);
        sc1.setAuthors(X);
        sc1.setFirstAuthor(X);
        sc1.setTitle(X);
        sc1.setLocation(X);
        sc1.setGoals(X);
        sc1.setPopulation(X);
        sc1.setPopulationPlace(X);
        sc1.setPopulationParticipants(X);
        sc1.setPopulationDuration(X);
        sc1.setExposureAssessment(X);
        sc1.setExposurePollutant(X);
        sc1.setMethods(X);
        sc1.setMethodStudyDesign(X);
        sc1.setMethodOutcome(X);
        sc1.setMethodStatistics(X);
        sc1.setMethodConfounders(X);
        sc1.setResult(X);
        sc1.setResultExposureRange(X);
        sc1.setResultEffectEstimate(X);
        sc1.setResultMeasuredOutcome(X);
        sc1.setComment(X);
        sc1.setIntern(X);
        sc1.setOriginalAbstract(X);
        sc1.setMainCodeOfCodeclass1(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(26);
        assertThat(sc1.getIntegerSearchTerms()).isEmpty();
        assertThat(sc1.getBooleanSearchTerms()).isEmpty();
        assertThat(sc1.getAuditSearchTerms()).isEmpty();
        assertThat(sc1.getCreatedDisplayValue()).isNull();
        assertThat(sc1.getModifiedDisplayValue()).isNull();

        assertThat(sc1.getSearchConditionId()).isEqualTo(SEARCH_CONDITION_ID);
    }

    @Test
    public void allIntegerSearchTerms() {
        sc1.setId("3");
        sc1.setPublicationYear("2017");
        assertThat(sc1.getStringSearchTerms()).isEmpty();
        assertThat(sc1.getIntegerSearchTerms()).hasSize(2);
        assertThat(sc1.getBooleanSearchTerms()).isEmpty();
        assertThat(sc1.getAuditSearchTerms()).isEmpty();
        assertThat(sc1.getCreatedDisplayValue()).isNull();
        assertThat(sc1.getModifiedDisplayValue()).isNull();

        assertThat(sc1.getSearchConditionId()).isEqualTo(SEARCH_CONDITION_ID);
    }

    @Test
    public void allBooleanSearchTerms() {
        sc1.setFirstAuthorOverridden(true);
        assertThat(sc1.getStringSearchTerms()).isEmpty();
        assertThat(sc1.getIntegerSearchTerms()).isEmpty();
        assertThat(sc1.getBooleanSearchTerms()).hasSize(1);
        assertThat(sc1.getAuditSearchTerms()).isEmpty();
        assertThat(sc1.getCreatedDisplayValue()).isNull();
        assertThat(sc1.getModifiedDisplayValue()).isNull();

        assertThat(sc1.getSearchConditionId()).isEqualTo(SEARCH_CONDITION_ID);
    }

    @Test
    public void allAuditSearchTerms() {
        sc1.setCreatedDisplayValue(X);
        sc1.setModifiedDisplayValue(X + X);
        assertThat(sc1.getStringSearchTerms()).isEmpty();
        assertThat(sc1.getIntegerSearchTerms()).isEmpty();
        assertThat(sc1.getBooleanSearchTerms()).isEmpty();
        assertThat(sc1.getAuditSearchTerms()).hasSize(4);

        assertThat(sc1.getSearchConditionId()).isEqualTo(SEARCH_CONDITION_ID);
    }

    @Test
    public void id_extensiveTest() {
        assertThat(sc1.getId()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
        assertThat(sc1.getIntegerSearchTerms()).isEmpty();
        assertThat(sc1.getBooleanSearchTerms()).isEmpty();

        sc1.setId("5");
        assertThat(sc1.getId()).isEqualTo("5");
        assertThat(sc1.getStringSearchTerms()).isEmpty();
        assertThat(sc1.getIntegerSearchTerms()).hasSize(1);
        assertThat(sc1.getBooleanSearchTerms()).isEmpty();
        IntegerSearchTerm st = sc1.getIntegerSearchTerms().iterator().next();
        assertThat(st.getFieldName()).isEqualTo(FLD_ID);
        assertThat(st.getRawSearchTerm()).isEqualTo("5");

        sc1.setId("10");
        assertThat(sc1.getId()).isEqualTo("10");
        assertThat(sc1.getStringSearchTerms()).isEmpty();
        assertThat(sc1.getIntegerSearchTerms()).hasSize(1);
        assertThat(sc1.getBooleanSearchTerms()).isEmpty();
        st = sc1.getIntegerSearchTerms().iterator().next();
        assertThat(st.getFieldName()).isEqualTo(FLD_ID);
        assertThat(st.getRawSearchTerm()).isEqualTo("10");

        sc1.setId(null);
        assertThat(sc1.getId()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
        assertThat(sc1.getIntegerSearchTerms()).isEmpty();
        assertThat(sc1.getBooleanSearchTerms()).isEmpty();
    }

    @Test
    public void doi_extensiveTest() {
        assertThat(sc1.getDoi()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
        assertThat(sc1.getIntegerSearchTerms()).isEmpty();
        assertThat(sc1.getBooleanSearchTerms()).isEmpty();

        sc1.setDoi("101111");
        assertThat(sc1.getDoi()).isEqualTo("101111");
        assertThat(sc1.getStringSearchTerms()).hasSize(1);
        assertThat(sc1.getIntegerSearchTerms()).isEmpty();
        assertThat(sc1.getBooleanSearchTerms()).isEmpty();
        StringSearchTerm st = sc1.getStringSearchTerms().iterator().next();
        assertThat(st.getFieldName()).isEqualTo(FLD_DOI);
        assertThat(st.getRawSearchTerm()).isEqualTo("101111");

        sc1.setDoi("102222");
        assertThat(sc1.getDoi()).isEqualTo("102222");
        assertThat(sc1.getStringSearchTerms()).hasSize(1);
        assertThat(sc1.getIntegerSearchTerms()).isEmpty();
        assertThat(sc1.getBooleanSearchTerms()).isEmpty();
        st = sc1.getStringSearchTerms().iterator().next();
        assertThat(st.getFieldName()).isEqualTo(FLD_DOI);
        assertThat(st.getRawSearchTerm()).isEqualTo("102222");

        sc1.setDoi(null);
        assertThat(sc1.getDoi()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
        assertThat(sc1.getIntegerSearchTerms()).isEmpty();
        assertThat(sc1.getBooleanSearchTerms()).isEmpty();

        assertThat(sc1.getSearchConditionId()).isEqualTo(SEARCH_CONDITION_ID);
    }

    @Test
    public void pmId() {
        assertThat(sc1.getPmId()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setPmId(X);
        assertThat(sc1.getPmId()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setPmId(null);
        assertThat(sc1.getPmId()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void authors() {
        assertThat(sc1.getAuthors()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setAuthors(X);
        assertThat(sc1.getAuthors()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setAuthors(null);
        assertThat(sc1.getAuthors()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void firstAuthor() {
        assertThat(sc1.getFirstAuthor()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setFirstAuthor(X);
        assertThat(sc1.getFirstAuthor()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setFirstAuthor(null);
        assertThat(sc1.getFirstAuthor()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void firstAuthorOverridden_extensiveTest() {
        assertThat(sc1.isFirstAuthorOverridden()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
        assertThat(sc1.getIntegerSearchTerms()).isEmpty();
        assertThat(sc1.getBooleanSearchTerms()).isEmpty();

        sc1.setFirstAuthorOverridden(true);
        assertThat(sc1.isFirstAuthorOverridden()).isTrue();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
        assertThat(sc1.getIntegerSearchTerms()).isEmpty();
        assertThat(sc1.getBooleanSearchTerms()).hasSize(1);
        BooleanSearchTerm st = sc1.getBooleanSearchTerms().iterator().next();
        assertThat(st.getFieldName()).isEqualTo(FLD_FIRST_AUTHOR_OVERRIDDEN);
        assertThat(st.getRawSearchTerm()).isEqualTo("true");
        assertThat(st.getValue()).isTrue();

        sc1.setFirstAuthorOverridden(false);
        assertThat(sc1.isFirstAuthorOverridden()).isFalse();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
        assertThat(sc1.getIntegerSearchTerms()).isEmpty();
        assertThat(sc1.getBooleanSearchTerms()).hasSize(1);
        st = sc1.getBooleanSearchTerms().iterator().next();
        assertThat(st.getFieldName()).isEqualTo(FLD_FIRST_AUTHOR_OVERRIDDEN);
        assertThat(st.getRawSearchTerm()).isEqualTo("false");
        assertThat(st.getValue()).isFalse();

        sc1.setFirstAuthorOverridden(null);
        assertThat(sc1.isFirstAuthorOverridden()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
        assertThat(sc1.getIntegerSearchTerms()).isEmpty();
        assertThat(sc1.getBooleanSearchTerms()).isEmpty();
    }

    @Test
    public void title() {
        assertThat(sc1.getTitle()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setTitle(X);
        assertThat(sc1.getTitle()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setTitle(null);
        assertThat(sc1.getTitle()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void location() {
        assertThat(sc1.getLocation()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setLocation(X);
        assertThat(sc1.getLocation()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setLocation(null);
        assertThat(sc1.getLocation()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void publicationYear() {
        assertThat(sc1.getPublicationYear()).isNull();
        assertThat(sc1.getIntegerSearchTerms()).isEmpty();

        sc1.setPublicationYear("2016");
        assertThat(sc1.getPublicationYear()).isEqualTo("2016");
        assertThat(sc1.getIntegerSearchTerms()).hasSize(1);

        sc1.setPublicationYear(null);
        assertThat(sc1.getPublicationYear()).isNull();
        assertThat(sc1.getIntegerSearchTerms()).isEmpty();
    }

    @Test
    public void goals() {
        assertThat(sc1.getGoals()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setGoals(X);
        assertThat(sc1.getGoals()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setGoals(null);
        assertThat(sc1.getGoals()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void population() {
        assertThat(sc1.getPopulation()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setPopulation(X);
        assertThat(sc1.getPopulation()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setPopulation(null);
        assertThat(sc1.getPopulation()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void populationPlace() {
        assertThat(sc1.getPopulationPlace()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setPopulationPlace(X);
        assertThat(sc1.getPopulationPlace()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setPopulationPlace(null);
        assertThat(sc1.getPopulationPlace()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void populationParticipants() {
        assertThat(sc1.getPopulationParticipants()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setPopulationParticipants(X);
        assertThat(sc1.getPopulationParticipants()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setPopulationParticipants(null);
        assertThat(sc1.getPopulationParticipants()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void populationDuration() {
        assertThat(sc1.getPopulationDuration()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setPopulationDuration(X);
        assertThat(sc1.getPopulationDuration()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setPopulationDuration(null);
        assertThat(sc1.getPopulationDuration()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void exposurePollutant() {
        assertThat(sc1.getExposurePollutant()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setExposurePollutant(X);
        assertThat(sc1.getExposurePollutant()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setExposurePollutant(null);
        assertThat(sc1.getExposurePollutant()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void exposureAssessment() {
        assertThat(sc1.getExposureAssessment()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setExposureAssessment(X);
        assertThat(sc1.getExposureAssessment()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setExposureAssessment(null);
        assertThat(sc1.getExposureAssessment()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void methods() {
        assertThat(sc1.getMethods()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setMethods(X);
        assertThat(sc1.getMethods()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setMethods(null);
        assertThat(sc1.getMethods()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void methodStudyDesign() {
        assertThat(sc1.getMethodStudyDesign()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setMethodStudyDesign(X);
        assertThat(sc1.getMethodStudyDesign()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setMethodStudyDesign(null);
        assertThat(sc1.getMethodStudyDesign()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void methodOutcome() {
        assertThat(sc1.getMethodOutcome()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setMethodOutcome(X);
        assertThat(sc1.getMethodOutcome()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setMethodOutcome(null);
        assertThat(sc1.getMethodOutcome()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void methodStatistics() {
        assertThat(sc1.getMethodStatistics()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setMethodStatistics(X);
        assertThat(sc1.getMethodStatistics()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setMethodStatistics(null);
        assertThat(sc1.getMethodStatistics()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void methodConfounders() {
        assertThat(sc1.getMethodConfounders()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setMethodConfounders(X);
        assertThat(sc1.getMethodConfounders()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setMethodConfounders(null);
        assertThat(sc1.getMethodConfounders()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void result() {
        assertThat(sc1.getResult()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setResult(X);
        assertThat(sc1.getResult()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setResult(null);
        assertThat(sc1.getResult()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void resultExposureRange() {
        assertThat(sc1.getResultExposureRange()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setResultExposureRange(X);
        assertThat(sc1.getResultExposureRange()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setResultExposureRange(null);
        assertThat(sc1.getResultExposureRange()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void resultEffectEstimate() {
        assertThat(sc1.getResultEffectEstimate()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setResultEffectEstimate(X);
        assertThat(sc1.getResultEffectEstimate()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setResultEffectEstimate(null);
        assertThat(sc1.getResultEffectEstimate()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void resultMeasuredOutcome() {
        assertThat(sc1.getResultMeasuredOutcome()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setResultMeasuredOutcome(X);
        assertThat(sc1.getResultMeasuredOutcome()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setResultMeasuredOutcome(null);
        assertThat(sc1.getResultMeasuredOutcome()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void comment() {
        assertThat(sc1.getComment()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setComment(X);
        assertThat(sc1.getComment()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setComment(null);
        assertThat(sc1.getComment()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void intern() {
        assertThat(sc1.getIntern()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setIntern(X);
        assertThat(sc1.getIntern()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setIntern(null);
        assertThat(sc1.getIntern()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void originalAbstract() {
        assertThat(sc1.getOriginalAbstract()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setOriginalAbstract(X);
        assertThat(sc1.getOriginalAbstract()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setOriginalAbstract(null);
        assertThat(sc1.getOriginalAbstract()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void mainCodeOfClass1() {
        assertThat(sc1.getMainCodeOfCodeclass1()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setMainCodeOfCodeclass1(X);
        assertThat(sc1.getMainCodeOfCodeclass1()).isEqualTo(X);
        assertThat(sc1.getStringSearchTerms()).hasSize(1);

        sc1.setMainCodeOfCodeclass1(null);
        assertThat(sc1.getMainCodeOfCodeclass1()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void createdDisplayValue() {
        assertThat(sc1.getCreatedDisplayValue()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setCreatedDisplayValue(X);
        assertThat(sc1.getCreatedDisplayValue()).isEqualTo(X);
        assertThat(sc1.getCreated()).isEqualTo(X);
        assertThat(sc1.getCreatedBy()).isEqualTo(X);
        assertThat(sc1.getLastModified()).isNull();
        assertThat(sc1.getLastModifiedBy()).isNull();
        assertThat(sc1.getStringSearchTerms()).hasSize(0);

        sc1.setCreatedDisplayValue(null);
        assertThat(sc1.getCreatedDisplayValue()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void modifiedDisplayValue() {
        assertThat(sc1.getModifiedDisplayValue()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();

        sc1.setModifiedDisplayValue(X);
        assertThat(sc1.getModifiedDisplayValue()).isEqualTo(X);
        assertThat(sc1.getLastModified()).isEqualTo(X);
        assertThat(sc1.getLastModifiedBy()).isEqualTo(X);
        assertThat(sc1.getCreated()).isNull();
        assertThat(sc1.getCreatedBy()).isNull();
        assertThat(sc1.getStringSearchTerms()).hasSize(0);

        sc1.setModifiedDisplayValue(null);
        assertThat(sc1.getModifiedDisplayValue()).isNull();
        assertThat(sc1.getStringSearchTerms()).isEmpty();
    }

    @Test
    public void testDisplayValue_withSingleStringSearchTerms_returnsIt() {
        sc1.setAuthors("hoops");
        assertThat(sc1.getDisplayValue()).isEqualTo("hoops");
    }

    @Test
    public void testDisplayValue_withTwoStringSearchTerms_joinsThemUsingAnd() {
        sc1.setAuthors("rag");
        sc1.setMethodConfounders("bones");
        assertThat(sc1.getDisplayValue()).isEqualTo("rag AND bones");
    }

    @Test
    public void testDisplayValue_forBooleanSearchTermsBeginFalse() {
        sc1.setFirstAuthorOverridden(false);
        assertThat(sc1.getDisplayValue()).isEqualTo("-first_author_overridden");
    }

    @Test
    public void testDisplayValue_forIntegerSearchTerms() {
        sc1.setPublicationYear("2017");
        assertThat(sc1.getDisplayValue()).isEqualTo("2017");
    }

    @Test
    public void testDisplayValue_forAuditSearchTermsForAuthorSearch() {
        sc1.setCreatedDisplayValue("mkj");
        assertThat(sc1.getDisplayValue()).isEqualTo("mkj");
    }

    @Test
    public void testDisplayValue_forAuditSearchTermsForDateSearch() {
        sc1.setCreatedDisplayValue(">2017-01-23");
        assertThat(sc1.getDisplayValue()).isEqualTo(">2017-01-23");
    }

    @Test
    public void testDisplayValue_forAuditSearchTermsForCombinedSearch() {
        sc1.setModifiedDisplayValue("rk >=2017-01-23");
        assertThat(sc1.getDisplayValue()).isEqualTo("rk >=2017-01-23");
    }

    @Test
    public void testDisplayValue_withMultipleSearchTerms_joinsThemAllUsingAND() {
        sc1.setAuthors("fooAuth");
        sc1.setMethodStudyDesign("bar");
        sc1.setDoi("baz");
        sc1.setPublicationYear("2016");
        sc1.setFirstAuthorOverridden(true);
        assertThat(sc1.getDisplayValue()).isEqualTo("fooAuth AND bar AND baz AND 2016 AND first_author_overridden");
    }

    @Test
    public void testDisplayValue_withCodesOnly() {
        sc1.addCode(new Code("1F", "C1F", "", false, 1, "CC1", "", 0));
        sc1.addCode(new Code("5H", "C5H", "", false, 5, "CC5", "", 0));
        assertThat(sc1.getDisplayValue()).isEqualTo("1F&5H");
    }

    @Test
    public void testDisplayValue_withSearchTermsAndCodes() {
        sc1.setAuthors("foobar");
        sc1.addCode(new Code("1F", "C1F", "", false, 1, "CC1", "", 0));
        sc1.addCode(new Code("5H", "C5H", "", false, 5, "CC5", "", 0));
        assertThat(sc1.getDisplayValue()).isEqualTo("foobar AND 1F&5H");
    }

    @Test
    public void equalsAndHash1_ofFieldSc() {
        assertThat(sc1.hashCode()).isEqualTo(917087168);
        assertThat(sc1.equals(sc1)).isTrue();
        assertThat(sc1.equals(null)).isFalse();
        assertThat(sc1.equals("")).isFalse();
    }

    @Test
    public void equalsAndHash2_withEmptySearchConditions() {
        SearchCondition f1 = new SearchCondition();
        SearchCondition f2 = new SearchCondition();
        assertEquality(f1, f2, 888458017);
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
        assertEquality(f1, f2, -2079105538);
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
        assertEquality(f1, f2, 996451851);

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
        assertEquality(f1, f2, -2079105538);

        f1.setSearchConditionId(3l);
        assertThat(f1.hashCode()).isNotEqualTo(f2.hashCode());
        assertThat(f1.equals(f2)).isFalse();
        assertThat(f2.equals(f1)).isFalse();

        f2.setSearchConditionId(4l);
        assertThat(f1.hashCode()).isNotEqualTo(f2.hashCode());
        assertThat(f1.equals(f2)).isFalse();
        assertThat(f2.equals(f1)).isFalse();

        f2.setSearchConditionId(3l);
        assertEquality(f1, f2, -1993218085);
    }

    @Test
    public void equalsAndHash6_withCreatedDisplayValue() {
        SearchCondition f1 = new SearchCondition();
        f1.setCreatedDisplayValue("foo");
        SearchCondition f2 = new SearchCondition();
        assertThat(f1.equals(f2)).isFalse();

        f2.setCreatedDisplayValue("foo");
        assertEquality(f1, f2, -1754170281);

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
        assertEquality(f1, f2, -781192085);

        f2.setModifiedDisplayValue("bar");
        assertThat(f1.equals(f2)).isFalse();

        f1.setCreatedDisplayValue(null);
        assertThat(f2.equals(f1)).isFalse();
    }

    @Test
    public void equalsAndHash8_withDifferentBooleanSearchTerms() {
        SearchCondition f1 = new SearchCondition();
        f1.addSearchTerm(new BooleanSearchTerm("f1", "false"));
        SearchCondition f2 = new SearchCondition();
        f2.addSearchTerm(new BooleanSearchTerm("f1", "true"));
        assertInequality(f1, f2);
    }

    private void assertInequality(SearchCondition f1, SearchCondition f2) {
        assertThat(f1.equals(f2)).isFalse();
        assertThat(f2.equals(f1)).isFalse();
        assertThat(f1.hashCode()).isNotEqualTo(f2.hashCode());
    }

    @Test
    public void equalsAndHash8_withDifferentIntegerSearchTerms() {
        SearchCondition f1 = new SearchCondition();
        f1.addSearchTerm(new IntegerSearchTerm("f1", "1"));
        SearchCondition f2 = new SearchCondition();
        f2.addSearchTerm(new IntegerSearchTerm("f1", "2"));
        assertInequality(f1, f2);
    }

    @Test
    public void equalsAndHash9_withDifferentStringSearchTerms() {
        SearchCondition f1 = new SearchCondition();
        f1.addSearchTerm(new StringSearchTerm("f1", "foo"));
        SearchCondition f2 = new SearchCondition();
        f2.addSearchTerm(new StringSearchTerm("f1", "bar"));
        assertInequality(f1, f2);
    }

    @Test
    public void equalsAndHash10_withDifferentStringAuditTerms() {
        SearchCondition f1 = new SearchCondition();
        f1.addSearchTerm(new AuditSearchTerm("f1", "foo"));
        SearchCondition f2 = new SearchCondition();
        f2.addSearchTerm(new AuditSearchTerm("f1", "bar"));
        assertInequality(f1, f2);
    }

    @Test
    public void equalsAndHash11_withDifferentCodes() {
        SearchCondition f1 = new SearchCondition();
        f1.addCode(new Code("1F", "C1F", "", false, 1, "CC1", "", 0));
        SearchCondition f2 = new SearchCondition();
        f2.addCode(new Code("1G", "C1G", "", false, 1, "CC1", "", 0));
        assertInequality(f1, f2);
    }

    @Test
    public void newSearchCondition_hasEmptyRemovedKeys() {
        assertThat(new SearchCondition().getRemovedKeys()).isEmpty();
    }

    @Test
    public void addingSearchTerms_leavesRemovedKeysEmpty() {
        sc2.setAuthors("foo");
        sc2.setPublicationYear("2014");
        sc2.setFirstAuthorOverridden(true);
        assertThat(sc2.getRemovedKeys()).isEmpty();
    }

    @Test
    public void removingSearchTerms_addsThemToRemovedKeys() {
        sc2.setAuthors("foo");
        sc2.setPublicationYear("2014");
        sc2.setGoals("bar");

        sc2.setPublicationYear(null);
        assertThat(sc2.getRemovedKeys()).hasSize(1).containsOnly("publication_year");
    }

    @Test
    public void addingSearchTerm_afterRemovingIt_removesItFromRemovedKeys() {
        sc2.setPublicationYear("2014");
        sc2.setPublicationYear(null);
        sc2.setPublicationYear("2015");
        assertThat(sc2.getRemovedKeys()).isEmpty();
    }

    @Test
    public void addingSearchTerm_xafterRemovingIt_removesItFromRemovedKeys() {
        sc1.setAuthors("foo");
        sc1.setAuthors(null);
        sc1.setPublicationYear("2014");
        sc1.setPublicationYear(null);
        assertThat(sc1.getRemovedKeys()).hasSize(2);

        sc1.clearRemovedKeys();
        assertThat(sc1.getRemovedKeys()).isEmpty();
    }

    @Test
    public void addingBooleanTermString() {
        sc2.addSearchTerm(new BooleanSearchTerm("fn", "rst"));
        assertThat(sc2.getBooleanSearchTerms()).hasSize(1);
        assertThat(sc2.getIntegerSearchTerms()).isEmpty();
        assertThat(sc2.getStringSearchTerms()).isEmpty();
        assertThat(sc2.getAuditSearchTerms()).isEmpty();
    }

    @Test
    public void addingIntegerTermString() {
        sc2.addSearchTerm(new IntegerSearchTerm("fn", "1"));
        assertThat(sc2.getBooleanSearchTerms()).isEmpty();
        assertThat(sc2.getIntegerSearchTerms()).hasSize(1);
        assertThat(sc2.getStringSearchTerms()).isEmpty();
        assertThat(sc2.getAuditSearchTerms()).isEmpty();
    }

    @Test
    public void addingSearchTermString() {
        sc1.addSearchTerm(new StringSearchTerm("fn", "rst"));
        assertThat(sc1.getBooleanSearchTerms()).isEmpty();
        assertThat(sc1.getIntegerSearchTerms()).isEmpty();
        assertThat(sc1.getStringSearchTerms()).hasSize(1);
        assertThat(sc1.getAuditSearchTerms()).isEmpty();
    }

    @Test
    public void addingAuditTermString() {
        sc2.addSearchTerm(new AuditSearchTerm("fn", "rst"));
        assertThat(sc2.getBooleanSearchTerms()).isEmpty();
        assertThat(sc2.getIntegerSearchTerms()).isEmpty();
        assertThat(sc2.getStringSearchTerms()).isEmpty();
        assertThat(sc2.getAuditSearchTerms()).hasSize(1);
    }

    @Test
    public void addingCodes() {
        Code c1 = new Code("c1", "c1", "", false, 1, "cc1", "", 0);
        Code c2 = new Code("c2", "c2", "", false, 2, "cc2", "", 0);
        Code c3 = new Code("c3", "c3", "", false, 3, "cc3", "", 0);
        Code c4 = new Code("c4", "c4", "", false, 3, "cc3", "", 0);
        sc2.addCodes(Arrays.asList(c1, c2, c3, c4));
        assertThat(sc2.getCodes()).hasSize(4);
        assertThat(sc2.getCodesOf(CodeClassId.CC3)).containsExactly(c3, c4);

        sc2.clearCodesOf(CodeClassId.CC3);
        assertThat(sc2.getCodes()).hasSize(2);
        sc2.clearCodes();
        assertThat(sc2.getCodes()).isEmpty();
    }

}
