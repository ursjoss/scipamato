package ch.difty.scipamato.core.entity.search

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.core.entity.Code
import ch.difty.scipamato.core.entity.IdScipamatoEntity.IdScipamatoEntityFields.ID
import ch.difty.scipamato.core.entity.Paper.PaperFields.DOI
import ch.difty.scipamato.core.entity.Paper.PaperFields.FIRST_AUTHOR_OVERRIDDEN
import ch.difty.scipamato.core.entity.Paper.PaperFields.NUMBER
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.junit.jupiter.api.Test

private const val SEARCH_CONDITION_ID: Long = 1
private const val X = "x"

@Suppress("LargeClass", "SpellCheckingInspection")
internal class SearchConditionTest {

    private val sc1 = SearchCondition(SEARCH_CONDITION_ID)
    private val sc2 = SearchCondition()

    @Test
    fun allStringSearchTerms() {
        sc1.doi = X
        sc1.pmId = X
        sc1.authors = X
        sc1.firstAuthor = X
        sc1.title = X
        sc1.location = X
        sc1.goals = X
        sc1.population = X
        sc1.populationPlace = X
        sc1.populationParticipants = X
        sc1.populationDuration = X
        sc1.exposureAssessment = X
        sc1.exposurePollutant = X
        sc1.methods = X
        sc1.methodStudyDesign = X
        sc1.methodOutcome = X
        sc1.methodStatistics = X
        sc1.methodConfounders = X
        sc1.result = X
        sc1.resultExposureRange = X
        sc1.resultEffectEstimate = X
        sc1.resultMeasuredOutcome = X
        sc1.conclusion = X
        sc1.comment = X
        sc1.intern = X
        sc1.originalAbstract = X
        sc1.mainCodeOfCodeclass1 = X
        sc1.stringSearchTerms shouldHaveSize 27
        sc1.integerSearchTerms.shouldBeEmpty()
        sc1.booleanSearchTerms.shouldBeEmpty()
        sc1.auditSearchTerms.shouldBeEmpty()
        sc1.createdDisplayValue.shouldBeNull()
        sc1.modifiedDisplayValue.shouldBeNull()

        sc1.searchConditionId shouldBeEqualTo SEARCH_CONDITION_ID
    }

    @Test
    fun allIntegerSearchTerms() {
        sc1.id = "3"
        sc1.number = "30"
        sc1.publicationYear = "2017"
        sc1.stringSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms shouldHaveSize 3
        sc1.booleanSearchTerms.shouldBeEmpty()
        sc1.auditSearchTerms.shouldBeEmpty()
        sc1.createdDisplayValue.shouldBeNull()
        sc1.modifiedDisplayValue.shouldBeNull()

        sc1.searchConditionId shouldBeEqualTo SEARCH_CONDITION_ID
        sc1.number shouldBeEqualTo "30"
    }

    @Test
    fun allBooleanSearchTerms() {
        sc1.isFirstAuthorOverridden = true
        sc1.stringSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms.shouldBeEmpty()
        sc1.booleanSearchTerms shouldHaveSize 1
        sc1.auditSearchTerms.shouldBeEmpty()
        sc1.createdDisplayValue.shouldBeNull()
        sc1.modifiedDisplayValue.shouldBeNull()

        sc1.searchConditionId shouldBeEqualTo SEARCH_CONDITION_ID
    }

    @Test
    fun allAuditSearchTerms() {
        sc1.createdDisplayValue = X
        sc1.modifiedDisplayValue = X + X
        sc1.stringSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms.shouldBeEmpty()
        sc1.booleanSearchTerms.shouldBeEmpty()
        sc1.auditSearchTerms shouldHaveSize 4

        sc1.searchConditionId shouldBeEqualTo SEARCH_CONDITION_ID
    }

    @Test
    fun id_extensiveTest() {
        sc1.id.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms.shouldBeEmpty()
        sc1.booleanSearchTerms.shouldBeEmpty()

        sc1.id = "5"
        sc1.id shouldBeEqualTo "5"
        sc1.stringSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms shouldHaveSize 1
        sc1.booleanSearchTerms.shouldBeEmpty()
        var st = sc1.integerSearchTerms.first()
        st.fieldName shouldBeEqualTo ID.fieldName
        st.rawSearchTerm shouldBeEqualTo "5"

        sc1.id = "10"
        sc1.id shouldBeEqualTo "10"
        sc1.stringSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms shouldHaveSize 1
        sc1.booleanSearchTerms.shouldBeEmpty()
        st = sc1.integerSearchTerms.first()
        st.fieldName shouldBeEqualTo ID.fieldName
        st.rawSearchTerm shouldBeEqualTo "10"

        sc1.id = null
        sc1.id.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms.shouldBeEmpty()
        sc1.booleanSearchTerms.shouldBeEmpty()
    }

    @Test
    fun number_extensiveTest() {
        sc1.number.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms.shouldBeEmpty()
        sc1.booleanSearchTerms.shouldBeEmpty()

        sc1.number = "50"
        sc1.number shouldBeEqualTo "50"
        sc1.stringSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms shouldHaveSize 1
        sc1.booleanSearchTerms.shouldBeEmpty()
        var st = sc1.integerSearchTerms.first()
        st.fieldName shouldBeEqualTo NUMBER.fieldName
        st.rawSearchTerm shouldBeEqualTo "50"

        sc1.number = "100"
        sc1.number shouldBeEqualTo "100"
        sc1.stringSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms shouldHaveSize 1
        sc1.booleanSearchTerms.shouldBeEmpty()
        st = sc1.integerSearchTerms.first()
        st.fieldName shouldBeEqualTo NUMBER.fieldName
        st.rawSearchTerm shouldBeEqualTo "100"

        sc1.number = null
        sc1.number.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms.shouldBeEmpty()
        sc1.booleanSearchTerms.shouldBeEmpty()
    }

    @Test
    fun doi_extensiveTest() {
        sc1.doi.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms.shouldBeEmpty()
        sc1.booleanSearchTerms.shouldBeEmpty()

        sc1.doi = "101111"
        sc1.doi shouldBeEqualTo "101111"
        sc1.stringSearchTerms shouldHaveSize 1
        sc1.integerSearchTerms.shouldBeEmpty()
        sc1.booleanSearchTerms.shouldBeEmpty()
        var st = sc1.stringSearchTerms.first()
        st.fieldName shouldBeEqualTo DOI.fieldName
        st.rawSearchTerm shouldBeEqualTo "101111"

        sc1.doi = "102222"
        sc1.doi shouldBeEqualTo "102222"
        sc1.stringSearchTerms shouldHaveSize 1
        sc1.integerSearchTerms.shouldBeEmpty()
        sc1.booleanSearchTerms.shouldBeEmpty()
        st = sc1.stringSearchTerms.first()
        st.fieldName shouldBeEqualTo DOI.fieldName
        st.rawSearchTerm shouldBeEqualTo "102222"

        sc1.doi = null
        sc1.doi.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms.shouldBeEmpty()
        sc1.booleanSearchTerms.shouldBeEmpty()

        sc1.searchConditionId shouldBeEqualTo SEARCH_CONDITION_ID
    }

    @Test
    fun pmId() {
        sc1.pmId.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.pmId = X
        sc1.pmId shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.pmId = null
        sc1.pmId.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun authors() {
        sc1.authors.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.authors = X
        sc1.authors shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.authors = null
        sc1.authors.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun firstAuthor() {
        sc1.firstAuthor.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.firstAuthor = X
        sc1.firstAuthor shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.firstAuthor = null
        sc1.firstAuthor.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun firstAuthorOverridden_extensiveTest() {
        sc1.isFirstAuthorOverridden.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms.shouldBeEmpty()
        sc1.booleanSearchTerms.shouldBeEmpty()

        sc1.isFirstAuthorOverridden = true
        (sc1.isFirstAuthorOverridden == true).shouldBeTrue()
        sc1.stringSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms.shouldBeEmpty()
        sc1.booleanSearchTerms shouldHaveSize 1
        var st = sc1.booleanSearchTerms.first()
        st.fieldName shouldBeEqualTo FIRST_AUTHOR_OVERRIDDEN.fieldName
        st.rawSearchTerm shouldBeEqualTo "true"
        st.value.shouldBeTrue()

        sc1.isFirstAuthorOverridden = false
        (sc1.isFirstAuthorOverridden == true).shouldBeFalse()
        sc1.stringSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms.shouldBeEmpty()
        sc1.booleanSearchTerms shouldHaveSize 1
        st = sc1.booleanSearchTerms.first()
        st.fieldName shouldBeEqualTo FIRST_AUTHOR_OVERRIDDEN.fieldName
        st.rawSearchTerm shouldBeEqualTo "false"
        st.value.shouldBeFalse()

        sc1.isFirstAuthorOverridden = null
        sc1.isFirstAuthorOverridden.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms.shouldBeEmpty()
        sc1.booleanSearchTerms.shouldBeEmpty()
    }

    @Test
    fun title() {
        sc1.title.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.title = X
        sc1.title shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.title = null
        sc1.title.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun location() {
        sc1.location.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.location = X
        sc1.location shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.location = null
        sc1.location.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun publicationYear() {
        sc1.publicationYear.shouldBeNull()
        sc1.integerSearchTerms.shouldBeEmpty()

        sc1.publicationYear = "2016"
        sc1.publicationYear shouldBeEqualTo "2016"
        sc1.integerSearchTerms shouldHaveSize 1

        sc1.publicationYear = null
        sc1.publicationYear.shouldBeNull()
        sc1.integerSearchTerms.shouldBeEmpty()
    }

    @Test
    fun goals() {
        sc1.goals.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.goals = X
        sc1.goals shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.goals = null
        sc1.goals.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun population() {
        sc1.population.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.population = X
        sc1.population shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.population = null
        sc1.population.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun populationPlace() {
        sc1.populationPlace.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.populationPlace = X
        sc1.populationPlace shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.populationPlace = null
        sc1.populationPlace.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun populationParticipants() {
        sc1.populationParticipants.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.populationParticipants = X
        sc1.populationParticipants shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.populationParticipants = null
        sc1.populationParticipants.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun populationDuration() {
        sc1.populationDuration.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.populationDuration = X
        sc1.populationDuration shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.populationDuration = null
        sc1.populationDuration.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun exposurePollutant() {
        sc1.exposurePollutant.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.exposurePollutant = X
        sc1.exposurePollutant shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.exposurePollutant = null
        sc1.exposurePollutant.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun exposureAssessment() {
        sc1.exposureAssessment.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.exposureAssessment = X
        sc1.exposureAssessment shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.exposureAssessment = null
        sc1.exposureAssessment.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun methods() {
        sc1.methods.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.methods = X
        sc1.methods shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.methods = null
        sc1.methods.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun methodStudyDesign() {
        sc1.methodStudyDesign.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.methodStudyDesign = X
        sc1.methodStudyDesign shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.methodStudyDesign = null
        sc1.methodStudyDesign.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun methodOutcome() {
        sc1.methodOutcome.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.methodOutcome = X
        sc1.methodOutcome shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.methodOutcome = null
        sc1.methodOutcome.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun methodStatistics() {
        sc1.methodStatistics.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.methodStatistics = X
        sc1.methodStatistics shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.methodStatistics = null
        sc1.methodStatistics.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun methodConfounders() {
        sc1.methodConfounders.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.methodConfounders = X
        sc1.methodConfounders shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.methodConfounders = null
        sc1.methodConfounders.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun result() {
        sc1.result.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.result = X
        sc1.result shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.result = null
        sc1.result.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun resultExposureRange() {
        sc1.resultExposureRange.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.resultExposureRange = X
        sc1.resultExposureRange shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.resultExposureRange = null
        sc1.resultExposureRange.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun resultEffectEstimate() {
        sc1.resultEffectEstimate.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.resultEffectEstimate = X
        sc1.resultEffectEstimate shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.resultEffectEstimate = null
        sc1.resultEffectEstimate.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun resultMeasuredOutcome() {
        sc1.resultMeasuredOutcome.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.resultMeasuredOutcome = X
        sc1.resultMeasuredOutcome shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.resultMeasuredOutcome = null
        sc1.resultMeasuredOutcome.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun conclusion() {
        sc1.conclusion.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.conclusion = X
        sc1.conclusion shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.conclusion = null
        sc1.conclusion.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun comment() {
        sc1.comment.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.comment = X
        sc1.comment shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.comment = null
        sc1.comment.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun intern() {
        sc1.intern.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.intern = X
        sc1.intern shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.intern = null
        sc1.intern.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun originalAbstract() {
        sc1.originalAbstract.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.originalAbstract = X
        sc1.originalAbstract shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.originalAbstract = null
        sc1.originalAbstract.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun mainCodeOfClass1() {
        sc1.mainCodeOfCodeclass1.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.mainCodeOfCodeclass1 = X
        sc1.mainCodeOfCodeclass1 shouldBeEqualTo X
        sc1.stringSearchTerms shouldHaveSize 1

        sc1.mainCodeOfCodeclass1 = null
        sc1.mainCodeOfCodeclass1.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun createdDisplayValue() {
        sc1.createdDisplayValue.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.createdDisplayValue = X
        sc1.createdDisplayValue shouldBeEqualTo X
        sc1.created shouldBeEqualTo X
        sc1.createdBy shouldBeEqualTo X
        sc1.lastModified.shouldBeNull()
        sc1.lastModifiedBy.shouldBeNull()
        sc1.stringSearchTerms shouldHaveSize 0

        sc1.createdDisplayValue = null
        sc1.createdDisplayValue.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun modifiedDisplayValue() {
        sc1.modifiedDisplayValue.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()

        sc1.modifiedDisplayValue = X
        sc1.modifiedDisplayValue shouldBeEqualTo X
        sc1.lastModified shouldBeEqualTo X
        sc1.lastModifiedBy shouldBeEqualTo X
        sc1.created.shouldBeNull()
        sc1.createdBy.shouldBeNull()
        sc1.stringSearchTerms shouldHaveSize 0

        sc1.modifiedDisplayValue = null
        sc1.modifiedDisplayValue.shouldBeNull()
        sc1.stringSearchTerms.shouldBeEmpty()
    }

    @Test
    fun testDisplayValue_withSingleStringSearchTerms_returnsIt() {
        sc1.authors = "hoops"
        sc1.displayValue shouldBeEqualTo "hoops"
    }

    @Test
    fun testDisplayValue_withTwoStringSearchTerms_joinsThemUsingAnd() {
        sc1.authors = "rag"
        sc1.methodConfounders = "bones"
        sc1.displayValue shouldBeEqualTo "rag AND bones"
    }

    @Test
    fun testDisplayValue_forBooleanSearchTermsBeginFalse() {
        sc1.isFirstAuthorOverridden = false
        sc1.displayValue shouldBeEqualTo "-firstAuthorOverridden"
    }

    @Test
    fun testDisplayValue_forIntegerSearchTerms() {
        sc1.publicationYear = "2017"
        sc1.displayValue shouldBeEqualTo "2017"
    }

    @Test
    fun testDisplayValue_forAuditSearchTermsForAuthorSearch() {
        sc1.createdDisplayValue = "mkj"
        sc1.displayValue shouldBeEqualTo "mkj"
    }

    @Test
    fun testDisplayValue_forAuditSearchTermsForDateSearch() {
        sc1.createdDisplayValue = ">2017-01-23"
        sc1.displayValue shouldBeEqualTo ">2017-01-23"
    }

    @Test
    fun testDisplayValue_forAuditSearchTermsForCombinedSearch() {
        sc1.modifiedDisplayValue = "rk >=2017-01-23"
        sc1.displayValue shouldBeEqualTo "rk >=2017-01-23"
    }

    @Test
    fun testDisplayValue_withMultipleSearchTerms_joinsThemAllUsingAND() {
        sc1.authors = "fooAuth"
        sc1.methodStudyDesign = "bar"
        sc1.doi = "baz"
        sc1.publicationYear = "2016"
        sc1.isFirstAuthorOverridden = true
        sc1.displayValue shouldBeEqualTo "fooAuth AND bar AND baz AND 2016 AND firstAuthorOverridden"
    }

    @Test
    fun testDisplayValue_withCodesOnly() {
        sc1.addCode(Code("1F", "C1F", "", false, 1, "CC1", "", 0))
        sc1.addCode(Code("5H", "C5H", "", false, 5, "CC5", "", 0))
        sc1.displayValue shouldBeEqualTo "1F&5H"
    }

    @Test
    fun testDisplayValue_withSearchTermsAndCodes() {
        sc1.authors = "foobar"
        sc1.addCode(Code("1F", "C1F", "", false, 1, "CC1", "", 0))
        sc1.addCode(Code("5H", "C5H", "", false, 5, "CC5", "", 0))
        sc1.displayValue shouldBeEqualTo "foobar AND 1F&5H"
    }

    @Test
    fun testDisplayValue_withNewsletterHeadlineOnly() {
        sc1.newsletterHeadline = "foo"
        sc1.displayValue shouldBeEqualTo "headline=foo"
    }

    @Test
    fun testDisplayValue_withNewsletterTopicOnly() {
        sc1.setNewsletterTopic(NewsletterTopic(1, "t1"))
        sc1.displayValue shouldBeEqualTo "topic=t1"
    }

    @Test
    fun testDisplayValue_withNewsletterIssueOnly() {
        sc1.newsletterIssue = "2018/06"
        sc1.displayValue shouldBeEqualTo "issue=2018/06"
    }

    @Test
    fun testDisplayValue_withNewsletterHeadlinePlusSomethingElse() {
        sc1.authors = "foobar"
        sc1.newsletterHeadline = "foo"
        sc1.displayValue shouldBeEqualTo "foobar AND headline=foo"
    }

    @Test
    fun testDisplayValue_withNewsletterTopicPlusSomethingElse() {
        sc1.authors = "foobar"
        sc1.setNewsletterTopic(NewsletterTopic(1, "t1"))
        sc1.displayValue shouldBeEqualTo "foobar AND topic=t1"
    }

    @Test
    fun testDisplayValue_withNewsletterIssuePlusSomethingElse() {
        sc1.authors = "foobar"
        sc1.newsletterIssue = "2018/04"
        sc1.displayValue shouldBeEqualTo "foobar AND issue=2018/04"
    }

    @Test
    fun testDisplayValue_withAllNewsletterRelatedFields() {
        sc1.newsletterHeadline = "foobar"
        sc1.newsletterIssue = "2018/02"
        sc1.setNewsletterTopic(NewsletterTopic(10, "t2"))
        sc1.displayValue shouldBeEqualTo "issue=2018/02 AND headline=foobar AND topic=t2"
    }

    @Test
    fun testDisplayValue_withNewsletterTopicNull_() {
        sc1.newsletterHeadline = "foobar"
        sc1.newsletterIssue = "2018/02"
        sc1.setNewsletterTopic(null)
        sc1.displayValue shouldBeEqualTo "issue=2018/02 AND headline=foobar"
    }

    @Test
    fun equalsAndHash1_ofFieldSc() {
        (sc1 == sc1).shouldBeTrue()
    }

    @Test
    fun equalsAndHash2_withEmptySearchConditions() {
        val f1 = SearchCondition()
        val f2 = SearchCondition()
        assertEquality(f1, f2)
    }

    private fun assertEquality(f1: SearchCondition, f2: SearchCondition) {
        f1.hashCode() shouldBeEqualTo f2.hashCode()
        ((f1 == f2)).shouldBeTrue()
        ((f2 == f1)).shouldBeTrue()
    }

    @Test
    fun equalsAndHash3_withSingleAttribute() {
        val f1 = SearchCondition()
        f1.authors = "foo"
        val f2 = SearchCondition()
        f2.authors = "foo"
        assertEquality(f1, f2)
    }

    @Test
    fun equalsAndHash4_withManyAttributes() {
        val f1 = SearchCondition()
        f1.authors = "foo"
        f1.comment = "bar"
        f1.publicationYear = "2014"
        f1.firstAuthor = "baz"
        f1.isFirstAuthorOverridden = true
        f1.methodOutcome = "blup"
        val f2 = SearchCondition()
        f2.authors = "foo"
        f2.comment = "bar"
        f2.publicationYear = "2014"
        f2.firstAuthor = "baz"
        f2.isFirstAuthorOverridden = true
        f2.methodOutcome = "blup"
        assertEquality(f1, f2)

        f2.methodOutcome = "blup2"
        ((f1 == f2)).shouldBeFalse()
        f1.hashCode() shouldNotBeEqualTo f2.hashCode()

        f2.methodOutcome = "blup"
        f2.methodStatistics = "bloop"
        ((f1 == f2)).shouldBeFalse()
        f1.hashCode() shouldNotBeEqualTo f2.hashCode()
    }

    @Test
    fun equalsAndHash5_withDifferentSearchConditionIds() {
        val f1 = SearchCondition()
        f1.authors = "foo"
        val f2 = SearchCondition()
        f2.authors = "foo"
        assertEquality(f1, f2)

        f1.searchConditionId = 3L
        f1.hashCode() shouldNotBeEqualTo f2.hashCode()
        (f1 == f2).shouldBeFalse()
        (f2 == f1).shouldBeFalse()

        f2.searchConditionId = 4L
        f1.hashCode() shouldNotBeEqualTo f2.hashCode()
        (f1 == f2).shouldBeFalse()
        (f2 == f1).shouldBeFalse()

        f2.searchConditionId = 3L
        assertEquality(f1, f2)
    }

    @Test
    fun equalsAndHash6_withCreatedDisplayValue() {
        val f1 = SearchCondition()
        f1.createdDisplayValue = "foo"
        val f2 = SearchCondition()
        (f1 == f2).shouldBeFalse()

        f2.createdDisplayValue = "foo"
        assertEquality(f1, f2)

        f2.createdDisplayValue = "bar"
        (f1 == f2).shouldBeFalse()

        f1.createdDisplayValue = null
        (f2 == f1).shouldBeFalse()
    }

    @Test
    fun equalsAndHash7_withModifiedDisplayValue() {
        val f1 = SearchCondition()
        f1.modifiedDisplayValue = "foo"
        val f2 = SearchCondition()
        (f1 == f2).shouldBeFalse()

        f2.modifiedDisplayValue = "foo"
        assertEquality(f1, f2)

        f2.modifiedDisplayValue = "bar"
        (f1 == f2).shouldBeFalse()

        f1.createdDisplayValue = null
        (f2 == f1).shouldBeFalse()
    }

    @Test
    fun equalsAndHash8_withDifferentBooleanSearchTerms() {
        val f1 = SearchCondition()
        f1.addSearchTerm(SearchTerm.newBooleanSearchTerm("f1", "false"))
        val f2 = SearchCondition()
        f2.addSearchTerm(SearchTerm.newBooleanSearchTerm("f1", "true"))
        assertInequality(f1, f2)
    }

    private fun assertInequality(f1: SearchCondition, f2: SearchCondition) {
        (f1 == f2).shouldBeFalse()
        (f2 == f1).shouldBeFalse()
        f1.hashCode() shouldNotBeEqualTo f2.hashCode()
    }

    @Test
    fun equalsAndHash8_withDifferentIntegerSearchTerms() {
        val f1 = SearchCondition()
        f1.addSearchTerm(SearchTerm.newIntegerSearchTerm("f1", "1"))
        val f2 = SearchCondition()
        f2.addSearchTerm(SearchTerm.newIntegerSearchTerm("f1", "2"))
        assertInequality(f1, f2)
    }

    @Test
    fun equalsAndHash9_withDifferentStringSearchTerms() {
        val f1 = SearchCondition()
        f1.addSearchTerm(SearchTerm.newStringSearchTerm("f1", "foo"))
        val f2 = SearchCondition()
        f2.addSearchTerm(SearchTerm.newStringSearchTerm("f1", "bar"))
        assertInequality(f1, f2)
    }

    @Test
    fun equalsAndHash10_withDifferentStringAuditTerms() {
        val f1 = SearchCondition()
        f1.addSearchTerm(SearchTerm.newAuditSearchTerm("f1", "foo"))
        val f2 = SearchCondition()
        f2.addSearchTerm(SearchTerm.newAuditSearchTerm("f1", "bar"))
        assertInequality(f1, f2)
    }

    @Test
    fun equalsAndHash11_withDifferentCodes() {
        val f1 = SearchCondition()
        f1.addCode(Code("1F", "C1F", "", false, 1, "CC1", "", 0))
        val f2 = SearchCondition()
        f2.addCode(Code("1G", "C1G", "", false, 1, "CC1", "", 0))
        assertInequality(f1, f2)
    }

    @Test
    fun equalsAndHash12_withDifferentNewsletterTopics_notDifferent() {
        val f1 = SearchCondition()
        f1.setNewsletterTopic(NewsletterTopic(1, "foo"))
        val f2 = SearchCondition()
        f2.setNewsletterTopic(NewsletterTopic(2, "foo"))
        assertEquality(f1, f2)
    }

    @Test
    fun equalsAndHash13_withDifferentNewsletterHeadlines_notDifferent() {
        val f1 = SearchCondition()
        f1.newsletterHeadline = "foo"
        val f2 = SearchCondition()
        f2.newsletterHeadline = "bar"
        assertEquality(f1, f2)
    }

    @Test
    fun equalsAndHash14_withDifferentNewsletterIssue_notDifferent() {
        val f1 = SearchCondition()
        f1.newsletterIssue = "foo"
        val f2 = SearchCondition()
        f2.newsletterIssue = "bar"
        assertEquality(f1, f2)
    }

    @Test
    fun equalsAndHash15_withDifferentNewsletterTopics_firstNull_notDifferent() {
        val f1 = SearchCondition()
        f1.setNewsletterTopic(null)
        val f2 = SearchCondition()
        f2.setNewsletterTopic(NewsletterTopic(2, "foo"))
        assertEquality(f1, f2)
    }

    @Test
    fun equalsAndHash16_withDifferentNewsletterHeadlines_firstNull_notDifferent() {
        val f1 = SearchCondition()
        f1.newsletterHeadline = null
        val f2 = SearchCondition()
        f2.newsletterHeadline = "bar"
        assertEquality(f1, f2)
    }

    @Test
    fun equalsAndHash17_withDifferentNewsletterIssue_firstNull_notDifferent() {
        val f1 = SearchCondition()
        f1.newsletterIssue = null
        val f2 = SearchCondition()
        f2.newsletterIssue = "bar"
        assertEquality(f1, f2)
    }

    @Test
    fun newSearchCondition_hasEmptyRemovedKeys() {
        SearchCondition().removedKeys.shouldBeEmpty()
    }

    @Test
    fun addingSearchTerms_leavesRemovedKeysEmpty() {
        sc2.authors = "foo"
        sc2.publicationYear = "2014"
        sc2.isFirstAuthorOverridden = true
        sc2.removedKeys.shouldBeEmpty()
    }

    @Test
    fun removingSearchTerms_addsThemToRemovedKeys() {
        sc2.authors = "foo"
        sc2.publicationYear = "2014"
        sc2.goals = "bar"

        sc2.publicationYear = null
        sc2.removedKeys shouldContainSame listOf("publicationYear")
    }

    @Test
    fun addingSearchTerm_afterRemovingIt_removesItFromRemovedKeys() {
        sc2.publicationYear = "2014"
        sc2.publicationYear = null
        sc2.publicationYear = "2015"
        sc2.removedKeys.shouldBeEmpty()
    }

    @Test
    fun clearingRemovedKeys_removesAllPresent() {
        sc1.authors = "foo"
        sc1.authors = null
        sc1.publicationYear = "2014"
        sc1.publicationYear = null
        sc1.removedKeys shouldHaveSize 2

        sc1.clearRemovedKeys()
        sc1.removedKeys.shouldBeEmpty()
    }

    @Test
    fun addingBooleanSearchTerm() {
        sc2.addSearchTerm(SearchTerm.newBooleanSearchTerm("fn", "rst"))
        sc2.booleanSearchTerms shouldHaveSize 1
        sc2.integerSearchTerms.shouldBeEmpty()
        sc2.stringSearchTerms.shouldBeEmpty()
        sc2.auditSearchTerms.shouldBeEmpty()
    }

    @Test
    fun addingIntegerTerm() {
        sc2.addSearchTerm(SearchTerm.newIntegerSearchTerm("fn", "1"))
        sc2.booleanSearchTerms.shouldBeEmpty()
        sc2.integerSearchTerms shouldHaveSize 1
        sc2.stringSearchTerms.shouldBeEmpty()
        sc2.auditSearchTerms.shouldBeEmpty()
    }

    @Test
    fun addingStringSearchTerm() {
        sc1.addSearchTerm(SearchTerm.newStringSearchTerm("fn", "rst"))
        sc1.booleanSearchTerms.shouldBeEmpty()
        sc1.integerSearchTerms.shouldBeEmpty()
        sc1.stringSearchTerms shouldHaveSize 1
        sc1.auditSearchTerms.shouldBeEmpty()
    }

    @Test
    fun addingAuditSearchTerm() {
        sc2.addSearchTerm(SearchTerm.newAuditSearchTerm("fn", "rst"))
        sc2.booleanSearchTerms.shouldBeEmpty()
        sc2.integerSearchTerms.shouldBeEmpty()
        sc2.stringSearchTerms.shouldBeEmpty()
        sc2.auditSearchTerms shouldHaveSize 1
    }

    @Test
    fun addingUnsupportedSearchTerm() {
        val stMock = mockk<SearchTerm> {
            every { searchTermType } returns SearchTermType.UNSUPPORTED
        }
        invoking { sc2.addSearchTerm(stMock) } shouldThrow AssertionError::class withMessage "SearchTermType.UNSUPPORTED is not supported"
    }

    @Test
    fun addingCodes() {
        val c1 = Code("c1", "c1", "", false, 1, "cc1", "", 0)
        val c2 = Code("c2", "c2", "", false, 2, "cc2", "", 0)
        val c3 = Code("c3", "c3", "", false, 3, "cc3", "", 0)
        val c4 = Code("c4", "c4", "", false, 3, "cc3", "", 0)
        sc2.addCodes(listOf(c1, c2, c3, c4))
        sc2.codes shouldHaveSize 4
        sc2.getCodesOf(CodeClassId.CC3) shouldContainAll listOf(c3, c4)

        sc2.clearCodesOf(CodeClassId.CC3)
        sc2.codes shouldHaveSize 2
        sc2.clearCodes()
        sc2.codes.shouldBeEmpty()
    }

    @Test
    fun settingAndResettingNewsletterHeadline() {
        sc1.newsletterHeadline.shouldBeNull()

        sc1.newsletterHeadline = "foo"
        sc1.newsletterHeadline shouldBeEqualTo "foo"

        sc1.newsletterHeadline = null
        sc1.newsletterHeadline.shouldBeNull()
    }

    @Test
    fun settingAndResettingNewsletterTopic() {
        sc1.newsletterTopicId.shouldBeNull()

        sc1.setNewsletterTopic(NewsletterTopic(1, "tp1"))
        sc1.newsletterTopicId shouldBeEqualTo 1

        sc1.setNewsletterTopic(null)
        sc1.newsletterTopicId.shouldBeNull()
    }

    @Test
    fun settingAndResettingNewsletterIssue() {
        sc1.newsletterIssue.shouldBeNull()

        sc1.newsletterIssue = "foo"
        sc1.newsletterIssue shouldBeEqualTo "foo"

        sc1.newsletterIssue = null
        sc1.newsletterIssue.shouldBeNull()
    }
}
