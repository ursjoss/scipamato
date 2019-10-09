package ch.difty.scipamato.core.entity.search

import ch.difty.scipamato.common.NullArgumentException
import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.core.entity.Code
import ch.difty.scipamato.core.entity.IdScipamatoEntity.IdScipamatoEntityFields.ID
import ch.difty.scipamato.core.entity.Paper.PaperFields.DOI
import ch.difty.scipamato.core.entity.Paper.PaperFields.FIRST_AUTHOR_OVERRIDDEN
import ch.difty.scipamato.core.entity.Paper.PaperFields.NUMBER
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

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
        assertThat(sc1.stringSearchTerms).hasSize(27)
        assertThat(sc1.integerSearchTerms).isEmpty()
        assertThat(sc1.booleanSearchTerms).isEmpty()
        assertThat(sc1.auditSearchTerms).isEmpty()
        assertNull(sc1.createdDisplayValue)
        assertNull(sc1.modifiedDisplayValue)

        assertThat(sc1.searchConditionId).isEqualTo(SEARCH_CONDITION_ID)
    }

    @Test
    fun allIntegerSearchTerms() {
        sc1.id = "3"
        sc1.number = "30"
        sc1.publicationYear = "2017"
        assertThat(sc1.stringSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).hasSize(3)
        assertThat(sc1.booleanSearchTerms).isEmpty()
        assertThat(sc1.auditSearchTerms).isEmpty()
        assertNull(sc1.createdDisplayValue)
        assertNull(sc1.modifiedDisplayValue)

        assertThat(sc1.searchConditionId).isEqualTo(SEARCH_CONDITION_ID)
        assertThat(sc1.number).isEqualTo("30")
    }

    @Test
    fun allBooleanSearchTerms() {
        sc1.isFirstAuthorOverridden = true
        assertThat(sc1.stringSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).isEmpty()
        assertThat(sc1.booleanSearchTerms).hasSize(1)
        assertThat(sc1.auditSearchTerms).isEmpty()
        assertNull(sc1.createdDisplayValue)
        assertNull(sc1.modifiedDisplayValue)

        assertThat(sc1.searchConditionId).isEqualTo(SEARCH_CONDITION_ID)
    }

    @Test
    fun allAuditSearchTerms() {
        sc1.createdDisplayValue = X
        sc1.modifiedDisplayValue = X + X
        assertThat(sc1.stringSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).isEmpty()
        assertThat(sc1.booleanSearchTerms).isEmpty()
        assertThat(sc1.auditSearchTerms).hasSize(4)

        assertThat(sc1.searchConditionId).isEqualTo(SEARCH_CONDITION_ID)
    }

    @Test
    fun id_extensiveTest() {
        assertNull(sc1.id)
        assertThat(sc1.stringSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).isEmpty()
        assertThat(sc1.booleanSearchTerms).isEmpty()

        sc1.id = "5"
        assertThat(sc1.id).isEqualTo("5")
        assertThat(sc1.stringSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).hasSize(1)
        assertThat(sc1.booleanSearchTerms).isEmpty()
        var st = sc1.integerSearchTerms.first()
        assertThat(st.fieldName).isEqualTo(ID.fieldName)
        assertThat(st.rawSearchTerm).isEqualTo("5")

        sc1.id = "10"
        assertThat(sc1.id).isEqualTo("10")
        assertThat(sc1.stringSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).hasSize(1)
        assertThat(sc1.booleanSearchTerms).isEmpty()
        st = sc1.integerSearchTerms.first()
        assertThat(st.fieldName).isEqualTo(ID.fieldName)
        assertThat(st.rawSearchTerm).isEqualTo("10")

        sc1.id = null
        assertNull(sc1.id)
        assertThat(sc1.stringSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).isEmpty()
        assertThat(sc1.booleanSearchTerms).isEmpty()
    }

    @Test
    fun number_extensiveTest() {
        assertNull(sc1.number)
        assertThat(sc1.stringSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).isEmpty()
        assertThat(sc1.booleanSearchTerms).isEmpty()

        sc1.number = "50"
        assertThat(sc1.number).isEqualTo("50")
        assertThat(sc1.stringSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).hasSize(1)
        assertThat(sc1.booleanSearchTerms).isEmpty()
        var st = sc1.integerSearchTerms.first()
        assertThat(st.fieldName).isEqualTo(NUMBER.fieldName)
        assertThat(st.rawSearchTerm).isEqualTo("50")

        sc1.number = "100"
        assertThat(sc1.number).isEqualTo("100")
        assertThat(sc1.stringSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).hasSize(1)
        assertThat(sc1.booleanSearchTerms).isEmpty()
        st = sc1.integerSearchTerms.first()
        assertThat(st.fieldName).isEqualTo(NUMBER.fieldName)
        assertThat(st.rawSearchTerm).isEqualTo("100")

        sc1.number = null
        assertNull(sc1.number)
        assertThat(sc1.stringSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).isEmpty()
        assertThat(sc1.booleanSearchTerms).isEmpty()
    }

    @Test
    fun doi_extensiveTest() {
        assertNull(sc1.doi)
        assertThat(sc1.stringSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).isEmpty()
        assertThat(sc1.booleanSearchTerms).isEmpty()

        sc1.doi = "101111"
        assertThat(sc1.doi).isEqualTo("101111")
        assertThat(sc1.stringSearchTerms).hasSize(1)
        assertThat(sc1.integerSearchTerms).isEmpty()
        assertThat(sc1.booleanSearchTerms).isEmpty()
        var st = sc1.stringSearchTerms.first()
        assertThat(st.fieldName).isEqualTo(DOI.fieldName)
        assertThat(st.rawSearchTerm).isEqualTo("101111")

        sc1.doi = "102222"
        assertThat(sc1.doi).isEqualTo("102222")
        assertThat(sc1.stringSearchTerms).hasSize(1)
        assertThat(sc1.integerSearchTerms).isEmpty()
        assertThat(sc1.booleanSearchTerms).isEmpty()
        st = sc1.stringSearchTerms.first()
        assertThat(st.fieldName).isEqualTo(DOI.fieldName)
        assertThat(st.rawSearchTerm).isEqualTo("102222")

        sc1.doi = null
        assertNull(sc1.doi)
        assertThat(sc1.stringSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).isEmpty()
        assertThat(sc1.booleanSearchTerms).isEmpty()

        assertThat(sc1.searchConditionId).isEqualTo(SEARCH_CONDITION_ID)
    }

    @Test
    fun pmId() {
        assertNull(sc1.pmId)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.pmId = X
        assertThat(sc1.pmId).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.pmId = null
        assertNull(sc1.pmId)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun authors() {
        assertNull(sc1.authors)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.authors = X
        assertThat(sc1.authors).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.authors = null
        assertNull(sc1.authors)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun firstAuthor() {
        assertNull(sc1.firstAuthor)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.firstAuthor = X
        assertThat(sc1.firstAuthor).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.firstAuthor = null
        assertNull(sc1.firstAuthor)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun firstAuthorOverridden_extensiveTest() {
        assertNull(sc1.isFirstAuthorOverridden)
        assertThat(sc1.stringSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).isEmpty()
        assertThat(sc1.booleanSearchTerms).isEmpty()

        sc1.isFirstAuthorOverridden = true
        assertThat(sc1.isFirstAuthorOverridden).isTrue()
        assertThat(sc1.stringSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).isEmpty()
        assertThat(sc1.booleanSearchTerms).hasSize(1)
        var st = sc1.booleanSearchTerms.first()
        assertThat(st.fieldName).isEqualTo(FIRST_AUTHOR_OVERRIDDEN.fieldName)
        assertThat(st.rawSearchTerm).isEqualTo("true")
        assertThat(st.value).isTrue()

        sc1.isFirstAuthorOverridden = false
        assertThat(sc1.isFirstAuthorOverridden).isFalse()
        assertThat(sc1.stringSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).isEmpty()
        assertThat(sc1.booleanSearchTerms).hasSize(1)
        st = sc1.booleanSearchTerms.first()
        assertThat(st.fieldName).isEqualTo(FIRST_AUTHOR_OVERRIDDEN.fieldName)
        assertThat(st.rawSearchTerm).isEqualTo("false")
        assertThat(st.value).isFalse()

        sc1.isFirstAuthorOverridden = null
        assertNull(sc1.isFirstAuthorOverridden)
        assertThat(sc1.stringSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).isEmpty()
        assertThat(sc1.booleanSearchTerms).isEmpty()
    }

    @Test
    fun title() {
        assertNull(sc1.title)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.title = X
        assertThat(sc1.title).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.title = null
        assertNull(sc1.title)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun location() {
        assertNull(sc1.location)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.location = X
        assertThat(sc1.location).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.location = null
        assertNull(sc1.location)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun publicationYear() {
        assertNull(sc1.publicationYear)
        assertThat(sc1.integerSearchTerms).isEmpty()

        sc1.publicationYear = "2016"
        assertThat(sc1.publicationYear).isEqualTo("2016")
        assertThat(sc1.integerSearchTerms).hasSize(1)

        sc1.publicationYear = null
        assertNull(sc1.publicationYear)
        assertThat(sc1.integerSearchTerms).isEmpty()
    }

    @Test
    fun goals() {
        assertNull(sc1.goals)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.goals = X
        assertThat(sc1.goals).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.goals = null
        assertNull(sc1.goals)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun population() {
        assertNull(sc1.population)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.population = X
        assertThat(sc1.population).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.population = null
        assertNull(sc1.population)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun populationPlace() {
        assertNull(sc1.populationPlace)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.populationPlace = X
        assertThat(sc1.populationPlace).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.populationPlace = null
        assertNull(sc1.populationPlace)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun populationParticipants() {
        assertNull(sc1.populationParticipants)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.populationParticipants = X
        assertThat(sc1.populationParticipants).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.populationParticipants = null
        assertNull(sc1.populationParticipants)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun populationDuration() {
        assertNull(sc1.populationDuration)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.populationDuration = X
        assertThat(sc1.populationDuration).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.populationDuration = null
        assertNull(sc1.populationDuration)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun exposurePollutant() {
        assertNull(sc1.exposurePollutant)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.exposurePollutant = X
        assertThat(sc1.exposurePollutant).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.exposurePollutant = null
        assertNull(sc1.exposurePollutant)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun exposureAssessment() {
        assertNull(sc1.exposureAssessment)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.exposureAssessment = X
        assertThat(sc1.exposureAssessment).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.exposureAssessment = null
        assertNull(sc1.exposureAssessment)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun methods() {
        assertNull(sc1.methods)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.methods = X
        assertThat(sc1.methods).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.methods = null
        assertNull(sc1.methods)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun methodStudyDesign() {
        assertNull(sc1.methodStudyDesign)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.methodStudyDesign = X
        assertThat(sc1.methodStudyDesign).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.methodStudyDesign = null
        assertNull(sc1.methodStudyDesign)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun methodOutcome() {
        assertNull(sc1.methodOutcome)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.methodOutcome = X
        assertThat(sc1.methodOutcome).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.methodOutcome = null
        assertNull(sc1.methodOutcome)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun methodStatistics() {
        assertNull(sc1.methodStatistics)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.methodStatistics = X
        assertThat(sc1.methodStatistics).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.methodStatistics = null
        assertNull(sc1.methodStatistics)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun methodConfounders() {
        assertNull(sc1.methodConfounders)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.methodConfounders = X
        assertThat(sc1.methodConfounders).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.methodConfounders = null
        assertNull(sc1.methodConfounders)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun result() {
        assertNull(sc1.result)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.result = X
        assertThat(sc1.result).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.result = null
        assertNull(sc1.result)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun resultExposureRange() {
        assertNull(sc1.resultExposureRange)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.resultExposureRange = X
        assertThat(sc1.resultExposureRange).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.resultExposureRange = null
        assertNull(sc1.resultExposureRange)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun resultEffectEstimate() {
        assertNull(sc1.resultEffectEstimate)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.resultEffectEstimate = X
        assertThat(sc1.resultEffectEstimate).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.resultEffectEstimate = null
        assertNull(sc1.resultEffectEstimate)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun resultMeasuredOutcome() {
        assertNull(sc1.resultMeasuredOutcome)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.resultMeasuredOutcome = X
        assertThat(sc1.resultMeasuredOutcome).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.resultMeasuredOutcome = null
        assertNull(sc1.resultMeasuredOutcome)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun conclusion() {
        assertNull(sc1.conclusion)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.conclusion = X
        assertThat(sc1.conclusion).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.conclusion = null
        assertNull(sc1.conclusion)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun comment() {
        assertNull(sc1.comment)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.comment = X
        assertThat(sc1.comment).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.comment = null
        assertNull(sc1.comment)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun intern() {
        assertNull(sc1.intern)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.intern = X
        assertThat(sc1.intern).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.intern = null
        assertNull(sc1.intern)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun originalAbstract() {
        assertNull(sc1.originalAbstract)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.originalAbstract = X
        assertThat(sc1.originalAbstract).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.originalAbstract = null
        assertNull(sc1.originalAbstract)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun mainCodeOfClass1() {
        assertNull(sc1.mainCodeOfCodeclass1)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.mainCodeOfCodeclass1 = X
        assertThat(sc1.mainCodeOfCodeclass1).isEqualTo(X)
        assertThat(sc1.stringSearchTerms).hasSize(1)

        sc1.mainCodeOfCodeclass1 = null
        assertNull(sc1.mainCodeOfCodeclass1)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun createdDisplayValue() {
        assertNull(sc1.createdDisplayValue)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.createdDisplayValue = X
        assertThat(sc1.createdDisplayValue).isEqualTo(X)
        assertThat(sc1.created).isEqualTo(X)
        assertThat(sc1.createdBy).isEqualTo(X)
        assertNull(sc1.lastModified)
        assertNull(sc1.lastModifiedBy)
        assertThat(sc1.stringSearchTerms).hasSize(0)

        sc1.createdDisplayValue = null
        assertNull(sc1.createdDisplayValue)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun modifiedDisplayValue() {
        assertNull(sc1.modifiedDisplayValue)
        assertThat(sc1.stringSearchTerms).isEmpty()

        sc1.modifiedDisplayValue = X
        assertThat(sc1.modifiedDisplayValue).isEqualTo(X)
        assertThat(sc1.lastModified).isEqualTo(X)
        assertThat(sc1.lastModifiedBy).isEqualTo(X)
        assertNull(sc1.created)
        assertNull(sc1.createdBy)
        assertThat(sc1.stringSearchTerms).hasSize(0)

        sc1.modifiedDisplayValue = null
        assertNull(sc1.modifiedDisplayValue)
        assertThat(sc1.stringSearchTerms).isEmpty()
    }

    @Test
    fun testDisplayValue_withSingleStringSearchTerms_returnsIt() {
        sc1.authors = "hoops"
        assertThat(sc1.displayValue).isEqualTo("hoops")
    }

    @Test
    fun testDisplayValue_withTwoStringSearchTerms_joinsThemUsingAnd() {
        sc1.authors = "rag"
        sc1.methodConfounders = "bones"
        assertThat(sc1.displayValue).isEqualTo("rag AND bones")
    }

    @Test
    fun testDisplayValue_forBooleanSearchTermsBeginFalse() {
        sc1.isFirstAuthorOverridden = false
        assertThat(sc1.displayValue).isEqualTo("-firstAuthorOverridden")
    }

    @Test
    fun testDisplayValue_forIntegerSearchTerms() {
        sc1.publicationYear = "2017"
        assertThat(sc1.displayValue).isEqualTo("2017")
    }

    @Test
    fun testDisplayValue_forAuditSearchTermsForAuthorSearch() {
        sc1.createdDisplayValue = "mkj"
        assertThat(sc1.displayValue).isEqualTo("mkj")
    }

    @Test
    fun testDisplayValue_forAuditSearchTermsForDateSearch() {
        sc1.createdDisplayValue = ">2017-01-23"
        assertThat(sc1.displayValue).isEqualTo(">2017-01-23")
    }

    @Test
    fun testDisplayValue_forAuditSearchTermsForCombinedSearch() {
        sc1.modifiedDisplayValue = "rk >=2017-01-23"
        assertThat(sc1.displayValue).isEqualTo("rk >=2017-01-23")
    }

    @Test
    fun testDisplayValue_withMultipleSearchTerms_joinsThemAllUsingAND() {
        sc1.authors = "fooAuth"
        sc1.methodStudyDesign = "bar"
        sc1.doi = "baz"
        sc1.publicationYear = "2016"
        sc1.isFirstAuthorOverridden = true
        assertThat(sc1.displayValue).isEqualTo("fooAuth AND bar AND baz AND 2016 AND firstAuthorOverridden")
    }

    @Test
    fun testDisplayValue_withCodesOnly() {
        sc1.addCode(Code("1F", "C1F", "", false, 1, "CC1", "", 0))
        sc1.addCode(Code("5H", "C5H", "", false, 5, "CC5", "", 0))
        assertThat(sc1.displayValue).isEqualTo("1F&5H")
    }

    @Test
    fun testDisplayValue_withSearchTermsAndCodes() {
        sc1.authors = "foobar"
        sc1.addCode(Code("1F", "C1F", "", false, 1, "CC1", "", 0))
        sc1.addCode(Code("5H", "C5H", "", false, 5, "CC5", "", 0))
        assertThat(sc1.displayValue).isEqualTo("foobar AND 1F&5H")
    }

    @Test
    fun testDisplayValue_withNewsletterHeadlineOnly() {
        sc1.newsletterHeadline = "foo"
        assertThat(sc1.displayValue).isEqualTo("headline=foo")
    }

    @Test
    fun testDisplayValue_withNewsletterTopicOnly() {
        sc1.setNewsletterTopic(NewsletterTopic(1, "t1"))
        assertThat(sc1.displayValue).isEqualTo("topic=t1")
    }

    @Test
    fun testDisplayValue_withNewsletterIssueOnly() {
        sc1.newsletterIssue = "2018/06"
        assertThat(sc1.displayValue).isEqualTo("issue=2018/06")
    }

    @Test
    fun testDisplayValue_withNewsletterHeadlinePlusSomethingElse() {
        sc1.authors = "foobar"
        sc1.newsletterHeadline = "foo"
        assertThat(sc1.displayValue).isEqualTo("foobar AND headline=foo")
    }

    @Test
    fun testDisplayValue_withNewsletterTopicPlusSomethingElse() {
        sc1.authors = "foobar"
        sc1.setNewsletterTopic(NewsletterTopic(1, "t1"))
        assertThat(sc1.displayValue).isEqualTo("foobar AND topic=t1")
    }

    @Test
    fun testDisplayValue_withNewsletterIssuePlusSomethingElse() {
        sc1.authors = "foobar"
        sc1.newsletterIssue = "2018/04"
        assertThat(sc1.displayValue).isEqualTo("foobar AND issue=2018/04")
    }

    @Test
    fun testDisplayValue_withAllNewsletterRelatedFields() {
        sc1.newsletterHeadline = "foobar"
        sc1.newsletterIssue = "2018/02"
        sc1.setNewsletterTopic(NewsletterTopic(10, "t2"))
        assertThat(sc1.displayValue).isEqualTo("issue=2018/02 AND headline=foobar AND topic=t2")
    }

    @Test
    fun testDisplayValue_withTopicWithNullTitle_dirtyWorksAroundWithDisplayingTopicIdInstead() {
        sc1.newsletterHeadline = "foobar"
        sc1.newsletterIssue = "2018/02"
        sc1.setNewsletterTopic(NewsletterTopic(10, null))
        assertThat(sc1.displayValue).isEqualTo("issue=2018/02 AND headline=foobar AND topic=10")
    }

    @Test
    fun equalsAndHash1_ofFieldSc() {
        assertThat(sc1 == sc1).isTrue()
    }

    @Test
    fun equalsAndHash2_withEmptySearchConditions() {
        val f1 = SearchCondition()
        val f2 = SearchCondition()
        assertEquality(f1, f2)
    }

    private fun assertEquality(f1: SearchCondition, f2: SearchCondition) {
        assertThat(f1.hashCode()).isEqualTo(f2.hashCode())
        assertThat(f1 == f2).isTrue()
        assertThat(f2 == f1).isTrue()
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
        assertThat(f1 == f2).isFalse()
        assertThat(f1.hashCode()).isNotEqualTo(f2.hashCode())

        f2.methodOutcome = "blup"
        f2.methodStatistics = "bloop"
        assertThat(f1 == f2).isFalse()
        assertThat(f1.hashCode()).isNotEqualTo(f2.hashCode())
    }

    @Test
    fun equalsAndHash5_withDifferentSearchConditionIds() {
        val f1 = SearchCondition()
        f1.authors = "foo"
        val f2 = SearchCondition()
        f2.authors = "foo"
        assertEquality(f1, f2)

        f1.searchConditionId = 3L
        assertThat(f1.hashCode()).isNotEqualTo(f2.hashCode())
        assertThat(f1 == f2).isFalse()
        assertThat(f2 == f1).isFalse()

        f2.searchConditionId = 4L
        assertThat(f1.hashCode()).isNotEqualTo(f2.hashCode())
        assertThat(f1 == f2).isFalse()
        assertThat(f2 == f1).isFalse()

        f2.searchConditionId = 3L
        assertEquality(f1, f2)
    }

    @Test
    fun equalsAndHash6_withCreatedDisplayValue() {
        val f1 = SearchCondition()
        f1.createdDisplayValue = "foo"
        val f2 = SearchCondition()
        assertThat(f1 == f2).isFalse()

        f2.createdDisplayValue = "foo"
        assertEquality(f1, f2)

        f2.createdDisplayValue = "bar"
        assertThat(f1 == f2).isFalse()

        f1.createdDisplayValue = null
        assertThat(f2 == f1).isFalse()
    }

    @Test
    fun equalsAndHash7_withModifiedDisplayValue() {
        val f1 = SearchCondition()
        f1.modifiedDisplayValue = "foo"
        val f2 = SearchCondition()
        assertThat(f1 == f2).isFalse()

        f2.modifiedDisplayValue = "foo"
        assertEquality(f1, f2)

        f2.modifiedDisplayValue = "bar"
        assertThat(f1 == f2).isFalse()

        f1.createdDisplayValue = null
        assertThat(f2 == f1).isFalse()
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
        assertThat(f1 == f2).isFalse()
        assertThat(f2 == f1).isFalse()
        assertThat(f1.hashCode()).isNotEqualTo(f2.hashCode())
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
        assertThat(SearchCondition().removedKeys).isEmpty()
    }

    @Test
    fun addingSearchTerms_leavesRemovedKeysEmpty() {
        sc2.authors = "foo"
        sc2.publicationYear = "2014"
        sc2.isFirstAuthorOverridden = true
        assertThat(sc2.removedKeys).isEmpty()
    }

    @Test
    fun removingSearchTerms_addsThemToRemovedKeys() {
        sc2.authors = "foo"
        sc2.publicationYear = "2014"
        sc2.goals = "bar"

        sc2.publicationYear = null
        assertThat(sc2.removedKeys)
            .hasSize(1)
            .containsOnly("publicationYear")
    }

    @Test
    fun addingSearchTerm_afterRemovingIt_removesItFromRemovedKeys() {
        sc2.publicationYear = "2014"
        sc2.publicationYear = null
        sc2.publicationYear = "2015"
        assertThat(sc2.removedKeys).isEmpty()
    }

    @Test
    fun clearingRemovedKeys_removesAllPresent() {
        sc1.authors = "foo"
        sc1.authors = null
        sc1.publicationYear = "2014"
        sc1.publicationYear = null
        assertThat(sc1.removedKeys).hasSize(2)

        sc1.clearRemovedKeys()
        assertThat(sc1.removedKeys).isEmpty()
    }

    @Test
    fun addingBooleanSearchTerm() {
        sc2.addSearchTerm(SearchTerm.newBooleanSearchTerm("fn", "rst"))
        assertThat(sc2.booleanSearchTerms).hasSize(1)
        assertThat(sc2.integerSearchTerms).isEmpty()
        assertThat(sc2.stringSearchTerms).isEmpty()
        assertThat(sc2.auditSearchTerms).isEmpty()
    }

    @Test
    fun addingIntegerTerm() {
        sc2.addSearchTerm(SearchTerm.newIntegerSearchTerm("fn", "1"))
        assertThat(sc2.booleanSearchTerms).isEmpty()
        assertThat(sc2.integerSearchTerms).hasSize(1)
        assertThat(sc2.stringSearchTerms).isEmpty()
        assertThat(sc2.auditSearchTerms).isEmpty()
    }

    @Test
    fun addingStringSearchTerm() {
        sc1.addSearchTerm(SearchTerm.newStringSearchTerm("fn", "rst"))
        assertThat(sc1.booleanSearchTerms).isEmpty()
        assertThat(sc1.integerSearchTerms).isEmpty()
        assertThat(sc1.stringSearchTerms).hasSize(1)
        assertThat(sc1.auditSearchTerms).isEmpty()
    }

    @Test
    fun addingAuditSearchTerm() {
        sc2.addSearchTerm(SearchTerm.newAuditSearchTerm("fn", "rst"))
        assertThat(sc2.booleanSearchTerms).isEmpty()
        assertThat(sc2.integerSearchTerms).isEmpty()
        assertThat(sc2.stringSearchTerms).isEmpty()
        assertThat(sc2.auditSearchTerms).hasSize(1)
    }

    @Test
    fun addingUnsupportedSearchTerm() {
        val stMock = mock(SearchTerm::class.java)
        `when`(stMock.searchTermType).thenReturn(SearchTermType.UNSUPPORTED)
        try {
            sc2.addSearchTerm(stMock)
            fail<Any>("should have thrown exception")
        } catch (ex: Error) {
            assertThat(ex)
                .isInstanceOf(AssertionError::class.java)
                .hasMessage("SearchTermType.UNSUPPORTED is not supported")
        }
    }

    @Test
    fun addingNullSearchTerm() {
        try {
            sc2.addSearchTerm(null)
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException::class.java)
                .hasMessage("searchTerm must not be null.")
        }
    }

    @Test
    fun addingCodes() {
        val c1 = Code("c1", "c1", "", false, 1, "cc1", "", 0)
        val c2 = Code("c2", "c2", "", false, 2, "cc2", "", 0)
        val c3 = Code("c3", "c3", "", false, 3, "cc3", "", 0)
        val c4 = Code("c4", "c4", "", false, 3, "cc3", "", 0)
        sc2.addCodes(listOf(c1, c2, c3, c4))
        assertThat(sc2.codes).hasSize(4)
        assertThat(sc2.getCodesOf(CodeClassId.CC3)).containsExactly(c3, c4)

        sc2.clearCodesOf(CodeClassId.CC3)
        assertThat(sc2.codes).hasSize(2)
        sc2.clearCodes()
        assertThat(sc2.codes).isEmpty()
    }

    @Test
    fun settingAndResettingNewsletterHeadline() {
        assertNull(sc1.newsletterHeadline)

        sc1.newsletterHeadline = "foo"
        assertThat(sc1.newsletterHeadline).isEqualTo("foo")

        sc1.newsletterHeadline = null
        assertNull(sc1.newsletterHeadline)
    }

    @Test
    fun settingAndResettingNewsletterTopic() {
        assertNull(sc1.newsletterTopicId)

        sc1.setNewsletterTopic(NewsletterTopic(1, "tp1"))
        assertThat(sc1.newsletterTopicId).isEqualTo(1)

        sc1.setNewsletterTopic(null)
        assertNull(sc1.newsletterTopicId)
    }

    @Test
    fun settingAndResettingNewsletterIssue() {
        assertNull(sc1.newsletterIssue)

        sc1.newsletterIssue = "foo"
        assertThat(sc1.newsletterIssue).isEqualTo("foo")

        sc1.newsletterIssue = null
        assertNull(sc1.newsletterIssue)
    }
}
