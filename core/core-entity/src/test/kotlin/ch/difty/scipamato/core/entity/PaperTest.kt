package ch.difty.scipamato.core.entity

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.core.entity.Paper.PaperFields.AUTHORS
import ch.difty.scipamato.core.entity.Paper.PaperFields.DOI
import ch.difty.scipamato.core.entity.Paper.PaperFields.FIRST_AUTHOR
import ch.difty.scipamato.core.entity.Paper.PaperFields.GOALS
import ch.difty.scipamato.core.entity.Paper.PaperFields.LOCATION
import ch.difty.scipamato.core.entity.Paper.PaperFields.NUMBER
import ch.difty.scipamato.core.entity.Paper.PaperFields.PUBL_YEAR
import ch.difty.scipamato.core.entity.Paper.PaperFields.TITLE
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeLessThan
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeEmpty
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@Suppress("SpellCheckingInspection", "SameParameterValue")
internal class PaperTest : Jsr303ValidatedEntityTest<Paper>(Paper::class.java) {

    override val toString: String
        get() =
            """Paper[number=2,doi=10.1093/aje/kwu275,pmId=1000
                |,authors=Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.
                |,firstAuthor=Turner MC,firstAuthorOverridden=false
                |,title=Title,location=foo,publicationYear=2016,goals=foo,population=<null>,populationPlace=<null>
                |,populationParticipants=<null>,populationDuration=<null>,exposurePollutant=<null>
                |,exposureAssessment=<null>,methods=<null>,methodStudyDesign=<null>,methodOutcome=<null>,methodStatistics=<null>
                |,methodConfounders=<null>,result=<null>,resultExposureRange=<null>,resultEffectEstimate=<null>
                |,resultMeasuredOutcome=<null>,conclusion=<null>,comment=<null>,intern=<null>,originalAbstract=<null>
                |,mainCodeOfCodeclass1=<null>,newsletterLink=<null>,attachments=[],codes=[],id=1,createdBy=10
                |,lastModifiedBy=20,created=2017-01-01T22:15:13.111,lastModified=2017-01-10T22:15:13.111,version=10]
                |""".trimMargin()

    override val displayValue: String
        get() = "Turner MC (2016): Title."

    override fun newValidEntity(): Paper =
        Paper().apply {
            id = 1L
            number = 2L
            doi = VALID_DOI
            pmId = 1000
            authors = VALID_AUTHORS
            firstAuthor = VALID_FIRST_AUTHOR
            isFirstAuthorOverridden = false
            title = VALID_TITLE
            location = NON_NULL_STRING
            publicationYear = 2016
            goals = NON_NULL_STRING

            created = LocalDateTime.parse("2017-01-01T22:15:13.111")
            createdBy = 10
            createdByName = "creator"
            createdByFullName = "creator full name"
            lastModified = LocalDateTime.parse("2017-01-10T22:15:13.111")
            lastModifiedBy = 20
            lastModifiedByName = "modifier"
            version = 10
        }

    @Test
    fun validatingPaper_withMultipleAuthorsWithFirstname_withPeriod_succeeds() {
        val p = newValidEntity()
        verifySuccessfulValidation(p)
    }

    @Test
    fun validatingPaper_withNullNumber_fails() {
        val p = newValidEntity()
        p.number = null
        validateAndAssertFailure(p, NUMBER, null, "{javax.validation.constraints.NotNull.message}")
    }

    @Test
    fun validatingPaper_withNegativeNumber_fails() {
        val p = newValidEntity()
        p.number = -1L
        validateAndAssertFailure(p, NUMBER, -1L, "{javax.validation.constraints.Min.message}")
    }

    @Test
    fun validatingPaper_withNullTitle_fails() {
        val p = newValidEntity()
        p.title = null
        validateAndAssertFailure(p, TITLE, null, "{javax.validation.constraints.NotNull.message}")
    }

    private fun verifyFailedAuthorValidation(p: Paper, invalidValue: String) {
        validateAndAssertFailure(p, AUTHORS, invalidValue, "{paper.invalidAuthor}")
    }

    @Test
    fun validatingPaper_withBlankAuthor_fails() {
        val invalidValue = ""
        val p = newValidEntity()
        p.authors = invalidValue
        verifyFailedAuthorValidation(p, invalidValue)
    }

    @Test
    fun validatingPaper_withSingleAuthorWithoutFirstname_withoutPeriod_fails() {
        val invalidValue = "Turner"
        val p = newValidEntity()
        p.authors = invalidValue
        verifyFailedAuthorValidation(p, invalidValue)
    }

    @Test
    fun validatingPaper_withSingleAuthorWithoutFirstname_withPeriod_succeeds() {
        val validValue = "Turner."
        val p = newValidEntity()
        p.authors = validValue
        verifySuccessfulValidation(p)
    }

    @Test
    fun validatingPaper_withAuthorsPlusCollectiveAuthor_succeeds() {
        val p = newValidEntity()
        p.authors = VALID_COLLECTIVE
        verifySuccessfulValidation(p)
    }

    @Test
    fun validatingPaper_withSingleAuthorWithFirstname_withoutPeriod_fails() {
        val p = newValidEntity()
        p.authors = "Turner MC"
        verifyFailedAuthorValidation(p, "Turner MC")
    }

    @Test
    fun validatingPaper_withSingleAuthorWithFirstname_withPeriod_succeeds() {
        val p = newValidEntity()
        p.authors = "Turner MC."
        verifySuccessfulValidation(p)
    }

    @Test
    fun validatingPaper_withTwoAuthorsWithFirstnames_withoutPeriod_fails() {
        val invalidValue = "Turner MC, Cohan A"
        val p = newValidEntity()
        p.authors = invalidValue
        verifyFailedAuthorValidation(p, invalidValue)
    }

    @Test
    fun validatingPaper_withTwoAuthorsWithFirstname_withPeriod_succeeds() {
        val p = newValidEntity()
        p.authors = "Turner MC, Cohen A."
        verifySuccessfulValidation(p)
    }

    @Test
    fun validatingPaper_withMultipleAuthorsWithFirstnames_withoutMissingSpace_fails() {
        val invalidValue =
            "Turner MC,Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM."
        val p = newValidEntity()
        p.authors = invalidValue
        verifyFailedAuthorValidation(p, invalidValue)
    }

    @Test
    fun validatingPaper_withNullFirstAuthor_fails() {
        val p = newValidEntity()
        p.firstAuthor = null
        validateAndAssertFailure(p, FIRST_AUTHOR, null, "{javax.validation.constraints.NotNull.message}")
    }

    @Test
    fun validatingPaper_withAuthorWithDashInName_succeeds() {
        val p = newValidEntity()
        p.authors = "Alpha-Beta G."
        verifySuccessfulValidation(p)
    }

    @Test
    fun validatingPaper_withAuthorWithTickInName_succeeds() {
        val p = newValidEntity()
        p.authors = "d'Alpha G."
        verifySuccessfulValidation(p)
    }

    @Test
    fun validatingPaper_withNullLocation_fails() {
        val p = newValidEntity()
        p.location = null
        validateAndAssertFailure(p, LOCATION, null, "{javax.validation.constraints.NotNull.message}")
    }

    @Test
    fun validatingPaper_withTooSmallPublicationYear_fails() {
        val tooEarly = 1499
        val p = newValidEntity()
        p.publicationYear = tooEarly
        validateAndAssertFailure(p, PUBL_YEAR, tooEarly, "{paper.invalidPublicationYear}")
    }

    @Test
    fun validatingPaper_withOkPublicationYear_succeeds() {
        val p = newValidEntity()
        p.publicationYear = 1500
        verifySuccessfulValidation(p)

        p.publicationYear = 2016
        verifySuccessfulValidation(p)

        p.publicationYear = 2100
        verifySuccessfulValidation(p)
    }

    @Test
    fun validatingPaper_withTooLargePublicationYear_fails() {
        val tooLate = 2101
        val p = newValidEntity()
        p.publicationYear = tooLate
        validateAndAssertFailure(p, PUBL_YEAR, tooLate, "{paper.invalidPublicationYear}")
    }

    @Test
    fun validatingPaper_withInvalidDoi_fails() {
        val invalidDoi = "abc"
        val p = newValidEntity()
        p.doi = invalidDoi
        validateAndAssertFailure(p, DOI, invalidDoi, "{paper.invalidDOI}")
    }

    @Test
    fun validatingPaper_withNullGoals_fails() {
        val p = newValidEntity()
        p.goals = null
        validateAndAssertFailure(p, GOALS, null, "{javax.validation.constraints.NotNull.message}")
    }

    @Test
    fun validatingPaper_withNonAsciiChars_passes() {
        val valueWithUmlaut = "ÄäÖöÜüéèàêç A."
        val p = newValidEntity()
        p.authors = valueWithUmlaut
        verifySuccessfulValidation(p)
    }

    @Test
    @Disabled("TODO")
    fun testingToString_withCodeClassesAndMainCodeOfClass1() {
        val p = newValidEntity().apply {
            addCode(makeCode(1, "D"))
            addCode(makeCode(1, "E"))
            addCode(makeCode(5, "A"))
            mainCodeOfCodeclass1 = "1D"
        }
        p.toString() shouldBeEqualTo
            """Paper[number=2,doi=10.1093/aje/kwu275,pmId=1000
                |,authors=Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.
                |,firstAuthor=Turner MC,firstAuthorOverridden=false,title=Title,location=foo,publicationYear=2016
                |,goals=foo,population=<null>,populationPlace=<null>,populationParticipants=<null>,populationDuration=<null>
                |,exposurePollutant=<null>,exposureAssessment=<null>,methods=<null>,methodStudyDesign=<null>,methodOutcome=<null>
                |,methodStatistics=<null>,methodConfounders=<null>,result=<null>,resultExposureRange=<null>,resultEffectEstimate=<null>
                |,resultMeasuredOutcome=<null>,conclusion=<null>,comment=<null>,intern=<null>,originalAbstract=<null>
                |,mainCodeOfCodeclass1=1D,newsletterLink=<null>,attachments=[],codes=[codesOfClass1=[Code[code=1D,name=code 1D
                |,comment=<null>,internal=false,codeClass=CodeClass[id=1],sort=1,createdBy=<null>,lastModifiedBy=<null>
                |,created=<null>,lastModified=<null>,version=0]],codesOfClass1=[Code[code=1E,name=code 1E,comment=<null>
                |,internal=false,codeClass=CodeClass[id=1],sort=1,createdBy=<null>,lastModifiedBy=<null>,created=<null>
                |,lastModified=<null>,version=0]],codesOfClass5=[Code[code=5A,name=code 5A,comment=<null>,internal=false
                |,codeClass=CodeClass[id=5],sort=1,createdBy=<null>,lastModifiedBy=<null>,created=<null>,lastModified=<null>
                |,version=0]]],id=1,createdBy=10,lastModifiedBy=20,created=2017-01-01T22:15:13.111,lastModified=2017-01-10T22:15:13.111
                |,version=10]""".trimMargin()
    }

    private fun makeCode(codeClassId: Int, codePart: String): Code {
        val code = codeClassId.toString() + codePart
        return Code(code, "code $code", null, false, codeClassId, "cc$code", "code class $code", 1)
    }

    @Test
    @Disabled("TODO")
    fun testingToString_withAttachments() {
        val p = newValidEntity().apply {
            attachments = ArrayList<PaperAttachment>().apply {
                add(newAttachment(1, 1, "p1"))
                add(newAttachment(2, 1, "p2"))
            }
        }
        p.toString() shouldBeEqualTo
            """Paper[number=2,doi=10.1093/aje/kwu275,pmId=1000
                |,authors=Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.
                |,firstAuthor=Turner MC,firstAuthorOverridden=false,title=Title,location=foo,publicationYear=2016,goals=foo
                |,population=<null>,populationPlace=<null>,populationParticipants=<null>,populationDuration=<null>
                |,exposurePollutant=<null>,exposureAssessment=<null>,methods=<null>,methodStudyDesign=<null>,methodOutcome=<null>,
                |methodStatistics=<null>,methodConfounders=<null>,result=<null>,resultExposureRange=<null>,resultEffectEstimate=<null>
                |,resultMeasuredOutcome=<null>,conclusion=<null>,comment=<null>,intern=<null>,originalAbstract=<null>
                |,mainCodeOfCodeclass1=<null>,newsletterLink=<null>,attachments=[PaperAttachment[paperId=1,name=p1,id=1]
                |, PaperAttachment[paperId=1,name=p2,id=2]],codes=[],id=1,createdBy=10,lastModifiedBy=20
                |,created=2017-01-01T22:15:13.111,lastModified=2017-01-10T22:15:13.111,version=10]""".trimMargin()
    }

    private fun newAttachment(id: Int, paperId: Long, name: String): PaperAttachment = PaperAttachment().apply {
        this.id = id
        this.paperId = paperId
        this.name = name
        content = name.toByteArray()
        contentType = "ct"
        size = name.length.toLong()
    }

    @Test
    fun addingCode_addsItAndAllowsToRetrieveIt() {
        val p = newValidEntity()
        p.codes.shouldNotBeNull().shouldBeEmpty()
        p.addCode(makeCode(1, "C"))

        p.codes.map { it.code } shouldContainAll listOf("1C")
        p.getCodesOf(CodeClassId.CC1).map { it.code } shouldContainAll listOf("1C")
        p.getCodesOf(CodeClassId.CC2).map { it.code }.shouldBeEmpty()
    }

    @Test
    fun addingCodes() {
        val p = newValidEntity()
        p.addCode(makeCode(1, "C"))
        val c1D = makeCode(1, "D")
        val c2A = makeCode(2, "A")
        p.addCodes(listOf(c1D, c2A))

        p.codes.map { it.code } shouldContainAll listOf("1C", "1D", "2A")

        p.clearCodes()
        p.codes.shouldBeEmpty()
    }

    @Test
    fun addingCodesOfSeveralClasses_allowsToRetrieveThemByClass() {
        val c1D = makeCode(1, "D")
        val c2F = makeCode(2, "F")
        val c2A = makeCode(2, "A")

        val p = newValidEntity()
        p.addCodes(listOf(c1D, c2F, c2A))

        p.getCodesOf(CodeClassId.CC1) shouldContainAll listOf(c1D)
        p.getCodesOf(CodeClassId.CC2) shouldContainAll listOf(c2F, c2A)
        p.getCodesOf(CodeClassId.CC3).shouldBeEmpty()
    }

    @Test
    fun clearingCode_delegatesClearingToCode() {
        val p = newValidEntity()
        p.codes.shouldBeEmpty()
        p.addCode(makeCode(CodeClassId.CC1.id, "C"))
        p.codes.shouldNotBeEmpty()
        p.clearCodesOf(CodeClassId.CC1)
        p.codes.shouldBeEmpty()
    }

    @Test
    fun testingMainCodeOfCodeClass1() {
        val c1D = makeCode(1, "D")
        val c1E = makeCode(1, "E")
        val c5A = makeCode(5, "A")
        val p = newValidEntity()
        p.addCodes(listOf(c1E, c1D, c5A))
        p.mainCodeOfCodeclass1 = c1E.code

        p.getCodesOf(CodeClassId.CC1) shouldContainAll listOf(c1E, c1D)
        p.getCodesOf(CodeClassId.CC2).shouldBeEmpty()
        p.getCodesOf(CodeClassId.CC5) shouldContainAll listOf(c5A)
        p.mainCodeOfCodeclass1 shouldBeEqualTo "1E"
    }

    @Test
    fun createdDisplayValue() {
        val p = newValidEntity()
        p.createdDisplayValue shouldBeEqualTo "creator (2017-01-01 22:15:13)"
    }

    @Test
    fun modifiedDisplayValue() {
        val p = newValidEntity()
        p.modifiedDisplayValue shouldBeEqualTo "modifier (2017-01-10 22:15:13)"
    }

    @Test
    fun assertNotAvailableValueForAuthors() {
        Paper.NA_AUTHORS shouldBeEqualTo "N A."
    }

    @Test
    fun assertNotAvailableValueForOtherStringFields() {
        Paper.NA_STRING shouldBeEqualTo "n.a."
    }

    @Test
    fun assertNotAvailableValueForPublicationYear() {
        Paper.NA_PUBL_YEAR shouldBeEqualTo 1500
    }

    @Test
    fun newPaper_hasNonNullButEmptyAttachments() {
        val p = newValidEntity()
        p.attachments.shouldBeEmpty()
    }

    @Test
    fun cannotAddAttachment_viaGetter() {
        val p = newValidEntity()
        p.attachments.add(PaperAttachment())
        p.attachments.shouldBeEmpty()
    }

    @Test
    fun canSetAttachments() {
        val p = newValidEntity()
        p.attachments = listOf(PaperAttachment(), PaperAttachment())
        p.attachments shouldHaveSize 2
    }

    @Test
    fun cannotModifyAttachmentsAfterSettig() {
        val attachments = mutableListOf(PaperAttachment(), PaperAttachment())
        val p = newValidEntity()
        p.attachments = attachments
        attachments.add(PaperAttachment())
        p.attachments.size shouldBeLessThan attachments.size
    }

    @Test
    fun canUnsetAttachments_withNullParameter() {
        val attachments = listOf(PaperAttachment(), PaperAttachment())
        val p = newValidEntity()
        p.attachments = attachments
        p.attachments shouldHaveSize 2
        p.attachments = null
        p.attachments.size shouldBeEqualTo 0
    }

    @Test
    fun canUnsetAttachments_withEmptyListParameter() {
        val attachments = ArrayList(listOf(PaperAttachment(), PaperAttachment()))
        val p = newValidEntity()
        p.attachments = attachments
        p.attachments shouldHaveSize 2
        p.attachments = ArrayList()
        p.attachments.shouldBeEmpty()
    }

    @Test
    fun equalityAndHashCode() {
        val p1 = newValidEntity()
        p1.id = 1L

        (p1 == p1).shouldBeTrue()

        val p2 = newValidEntity()
        p2.id = 1L
        (p1 == p2).shouldBeTrue()
        p1.hashCode() shouldBeEqualTo p2.hashCode()
        p2.id = 2L
        (p1 == p2).shouldBeFalse()
        p1.hashCode() shouldNotBeEqualTo p2.hashCode()
        p2.id = 1L
        p2.publicationYear = 2017
        (p1 == p2).shouldBeFalse()
        p1.hashCode() shouldNotBeEqualTo p2.hashCode()
    }
    // Note: Did not get this to run with equalsverifier due to 'Abstract
    // delegation: Paper's hashCode method delegates to an abstract method' on codes

    override fun verifyEquals() {
        // no-op - see comment on previous test equalityAndHashCode
    }

    @Test
    fun canSetAndGetNewsletterLinkAsObject() {
        val p = newValidEntity()
        p.newsletterLink.shouldBeNull()

        val nl = Paper.NewsletterLink(1, "issue", 0, null, null, null)

        p.newsletterLink = nl

        p.newsletterLink shouldBeEqualTo nl
    }

    @Test
    fun canSetAndGetNewsletterLinkAsFields() {
        val p = newValidEntity()
        p.newsletterLink.shouldBeNull()

        p.setNewsletterLink(1, "issue", 0, null, null, null)

        p.newsletterLink shouldBeEqualTo Paper.NewsletterLink(1, "issue", 0, null, null, null)
    }

    @Test
    fun settingNewsletterAssociation() {
        val p = newValidEntity()
        p.newsletterLink.shouldBeNull()

        p.newsletterLink = Paper.NewsletterLink(1, "1806", PublicationStatus.WIP.id, 1, "mytopic", "headline")

        p.newsletterLink.shouldNotBeNull()
        validateNewsletterLink(p.newsletterLink, 1, "1806", PublicationStatus.WIP, 1, "mytopic", "headline")
        p.newsletterHeadline shouldBeEqualTo "headline"
        p.newsletterTopicId shouldBeEqualTo 1

        p.newsletterHeadline = "otherHeadline"
        validateNewsletterLink(
            p.newsletterLink, 1, "1806", PublicationStatus.WIP, 1, "mytopic", "otherHeadline"
        )

        p.setNewsletterTopic(NewsletterTopic(10, "someothertopic"))
        validateNewsletterLink(
            p.newsletterLink, 1, "1806", PublicationStatus.WIP, 10,
            "someothertopic", "otherHeadline"
        )

        p.setNewsletterTopic(null)
        validateNewsletterLink(
            p.newsletterLink, 1, "1806", PublicationStatus.WIP,
            null, null, "otherHeadline"
        )

        p.newsletterHeadline = null
        validateNewsletterLink(
            p.newsletterLink, 1, "1806", PublicationStatus.WIP, null, null, null
        )

        p.newsletterIssue shouldBeEqualTo "1806"
        p.newsletterLink.shouldNotBeNull()
        p.newsletterLink.issue shouldBeEqualTo "1806"
    }

    @Suppress("LongParameterList")
    private fun validateNewsletterLink(
        newsletterLink: Paper.NewsletterLink,
        newsletterId: Int?,
        issue: String,
        status: PublicationStatus,
        topicId: Int?,
        topic: String?,
        headline: String?
    ) {
        newsletterLink.newsletterId shouldBeEqualTo newsletterId
        newsletterLink.issue shouldBeEqualTo issue
        newsletterLink.publicationStatusId shouldBeEqualTo status.id
        topicId?.let {
            newsletterLink.topicId shouldBeEqualTo topicId
        } ?: newsletterLink.topicId.shouldBeNull()
        newsletterLink.topic shouldBeEqualTo topic
        newsletterLink.headline shouldBeEqualTo headline
    }

    @Test
    fun settingNewsletterLinkFields_whileNewsletterLinkIsNull_doesNothing() {
        val p = newValidEntity()
        p.newsletterLink.shouldBeNull()

        p.setNewsletterTopic(NewsletterTopic(10, "someothertopic"))
        p.newsletterHeadline = "foo"

        p.newsletterLink.shouldBeNull()
        p.newsletterTopicId.shouldBeNull()
        p.newsletterHeadline.shouldBeNull()
    }

    @Test
    fun settingNewsletterIssue_isNoOp() {
        val p = newValidEntity()
        p.newsletterIssue.shouldBeNull()
        p.newsletterIssue = "whatever"
        p.newsletterIssue.shouldBeNull()
    }

    companion object {
        private const val VALID_AUTHORS =
            "Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM."
        private const val VALID_COLLECTIVE =
            "Mehta AJ, Thun GA, Imboden M, Ferrarotti I, Keidel D, Künzli N, Kromhout H, Miedinger D, Phuleria H, " +
                "Rochat T, Russi EW, Schindler C, Schwartz J, Vermeulen R, Luisetti M, Probst-Hensch N; SAPALDIA team."
        private const val VALID_FIRST_AUTHOR = "Turner MC"
        private const val VALID_TITLE = "Title"
        private const val VALID_DOI = "10.1093/aje/kwu275"
        private const val NON_NULL_STRING = "foo"
    }
}
