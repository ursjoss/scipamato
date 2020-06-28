package ch.difty.scipamato.core.entity

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.core.entity.Code.CodeFields.CODE
import ch.difty.scipamato.core.entity.Paper.PaperFields.AUTHORS
import ch.difty.scipamato.core.entity.Paper.PaperFields.DOI
import ch.difty.scipamato.core.entity.Paper.PaperFields.FIRST_AUTHOR
import ch.difty.scipamato.core.entity.Paper.PaperFields.GOALS
import ch.difty.scipamato.core.entity.Paper.PaperFields.LOCATION
import ch.difty.scipamato.core.entity.Paper.PaperFields.NUMBER
import ch.difty.scipamato.core.entity.Paper.PaperFields.PUBL_YEAR
import ch.difty.scipamato.core.entity.Paper.PaperFields.TITLE
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.extractProperty
import org.junit.jupiter.api.Assertions.assertNull
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
        assertThat(p.toString()).isEqualTo(
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
        )
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
        assertThat(p.toString()).isEqualTo(
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
        )
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
        assertThat(p.codes)
            .isNotNull
            .isEmpty()
        p.addCode(makeCode(1, "C"))

        assertThat(extractProperty(CODE.fieldName).from(p.codes)).containsExactly("1C")
        assertThat(extractProperty(CODE.fieldName).from(p.getCodesOf(CodeClassId.CC1))).containsExactly("1C")
        assertThat(extractProperty(CODE.fieldName).from(p.getCodesOf(CodeClassId.CC2))).isEmpty()
    }

    @Test
    fun addingCodes() {
        val p = newValidEntity()
        p.addCode(makeCode(1, "C"))
        val c1D = makeCode(1, "D")
        val c2A = makeCode(2, "A")
        p.addCodes(listOf(c1D, c2A))

        assertThat(extractProperty(CODE.fieldName).from(p.codes)).containsExactly("1C", "1D", "2A")

        p.clearCodes()
        assertThat(p.codes).isEmpty()
    }

    @Test
    fun addingCodesOfSeveralClasses_allowsToRetrieveThemByClass() {
        val c1D = makeCode(1, "D")
        val c2F = makeCode(2, "F")
        val c2A = makeCode(2, "A")

        val p = newValidEntity()
        p.addCodes(listOf(c1D, c2F, c2A))

        assertThat(p.getCodesOf(CodeClassId.CC1)).containsExactly(c1D)
        assertThat(p.getCodesOf(CodeClassId.CC2)).containsExactly(c2F, c2A)
        assertThat(p.getCodesOf(CodeClassId.CC3)).isEmpty()
    }

    @Test
    fun clearingCode_delegatesClearingToCode() {
        val p = newValidEntity()
        assertThat(p.codes).isEmpty()
        p.addCode(makeCode(CodeClassId.CC1.id, "C"))
        assertThat(p.codes).isNotEmpty
        p.clearCodesOf(CodeClassId.CC1)
        assertThat(p.codes).isEmpty()
    }

    @Test
    fun testingMainCodeOfCodeClass1() {
        val c1D = makeCode(1, "D")
        val c1E = makeCode(1, "E")
        val c5A = makeCode(5, "A")
        val p = newValidEntity()
        p.addCodes(listOf(c1E, c1D, c5A))
        p.mainCodeOfCodeclass1 = c1E.code

        assertThat(p.getCodesOf(CodeClassId.CC1)).containsExactly(c1E, c1D)
        assertThat(p.getCodesOf(CodeClassId.CC2)).isEmpty()
        assertThat(p.getCodesOf(CodeClassId.CC5)).containsExactly(c5A)
        assertThat(p.mainCodeOfCodeclass1).isEqualTo("1E")
    }

    @Test
    fun createdDisplayValue() {
        val p = newValidEntity()
        assertThat(p.createdDisplayValue).isEqualTo("creator (2017-01-01 22:15:13)")
    }

    @Test
    fun modifiedDisplayValue() {
        val p = newValidEntity()
        assertThat(p.modifiedDisplayValue).isEqualTo("modifier (2017-01-10 22:15:13)")
    }

    @Test
    fun assertNotAvailableValueForAuthors() {
        assertThat(Paper.NA_AUTHORS).isEqualTo("N A.")
    }

    @Test
    fun assertNotAvailableValueForOtherStringFields() {
        assertThat(Paper.NA_STRING).isEqualTo("n.a.")
    }

    @Test
    fun assertNotAvailableValueForPublicationYear() {
        assertThat(Paper.NA_PUBL_YEAR).isEqualTo(1500)
    }

    @Test
    fun newPaper_hasNonNullButEmptyAttachments() {
        val p = newValidEntity()
        assertThat(p.attachments).isEmpty()
    }

    @Test
    fun cannotAddAttachment_viaGetter() {
        val p = newValidEntity()
        p.attachments.add(PaperAttachment())
        assertThat(p.attachments).isEmpty()
    }

    @Test
    fun canSetAttachments() {
        val p = newValidEntity()
        p.attachments = listOf(PaperAttachment(), PaperAttachment())
        assertThat(p.attachments).hasSize(2)
    }

    @Test
    fun cannotModifyAttachmentsAfterSettig() {
        val attachments = mutableListOf(PaperAttachment(), PaperAttachment())
        val p = newValidEntity()
        p.attachments = attachments
        attachments.add(PaperAttachment())
        assertThat(p.attachments.size).isLessThan(attachments.size)
    }

    @Test
    fun canUnsetAttachments_withNullParameter() {
        val attachments = listOf(PaperAttachment(), PaperAttachment())
        val p = newValidEntity()
        p.attachments = attachments
        assertThat(p.attachments).hasSize(2)
        p.attachments = null
        assertThat(p.attachments.size).isEqualTo(0)
    }

    @Test
    fun canUnsetAttachments_withEmptyListParameter() {
        val attachments = ArrayList(listOf(PaperAttachment(), PaperAttachment()))
        val p = newValidEntity()
        p.attachments = attachments
        assertThat(p.attachments).hasSize(2)
        p.attachments = ArrayList()
        assertThat(p.attachments).isEmpty()
    }

    @Test
    fun equalityAndHashCode() {
        val p1 = newValidEntity()
        p1.id = 1L

        assertThat(p1 == p1).isTrue()

        val p2 = newValidEntity()
        p2.id = 1L
        assertThat(p1 == p2).isTrue()
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode())
        p2.id = 2L
        assertThat(p1 == p2).isFalse()
        assertThat(p1.hashCode()).isNotEqualTo(p2.hashCode())
        p2.id = 1L
        p2.publicationYear = 2017
        assertThat(p1 == p2).isFalse()
        assertThat(p1.hashCode()).isNotEqualTo(p2.hashCode())
    }
    // Note: Did not get this to run with equalsverifier due to 'Abstract
    // delegation: Paper's hashCode method delegates to an abstract method' on codes

    override fun verifyEquals() {
        // no-op - see comment on previous test equalityAndHashCode
    }

    @Test
    fun canSetAndGetNewsletterLinkAsObject() {
        val p = newValidEntity()
        assertThat(p.newsletterLink).isNull()

        val nl = Paper.NewsletterLink(1, "issue", 0, null, null, null)

        p.newsletterLink = nl

        assertThat(p.newsletterLink).isEqualTo(nl)
    }

    @Test
    fun canSetAndGetNewsletterLinkAsFields() {
        val p = newValidEntity()
        assertThat(p.newsletterLink).isNull()

        p.setNewsletterLink(1, "issue", 0, null, null, null)

        assertThat(p.newsletterLink).isEqualTo(Paper.NewsletterLink(1, "issue", 0, null, null, null))
    }

    @Test
    fun settingNewsletterAssociation() {
        val p = newValidEntity()
        assertThat(p.newsletterLink).isNull()

        p.newsletterLink = Paper.NewsletterLink(1, "1806", PublicationStatus.WIP.id, 1, "mytopic", "headline")

        assertThat(p.newsletterLink).isNotNull
        validateNewsletterLink(p.newsletterLink, 1, "1806", PublicationStatus.WIP, 1, "mytopic", "headline")
        assertThat(p.newsletterHeadline).isEqualTo("headline")
        assertThat(p.newsletterTopicId).isEqualTo(1)

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

        assertThat(p.newsletterIssue).isEqualTo("1806")
        assertThat(p.newsletterLink).isNotNull
        assertThat(p.newsletterLink.issue).isEqualTo("1806")
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
        assertThat(newsletterLink.newsletterId).isEqualTo(newsletterId)
        assertThat(newsletterLink.issue).isEqualTo(issue)
        assertThat(newsletterLink.publicationStatusId).isEqualTo(status.id)
        topicId?.let {
            assertThat(newsletterLink.topicId).isEqualTo(topicId)
        } ?: assertNull(newsletterLink.topicId)
        assertThat(newsletterLink.topic).isEqualTo(topic)
        assertThat(newsletterLink.headline).isEqualTo(headline)
    }

    @Test
    fun settingNewsletterLinkFields_whileNewsletterLinkIsNull_doesNothing() {
        val p = newValidEntity()
        assertThat(p.newsletterLink).isNull()

        p.setNewsletterTopic(NewsletterTopic(10, "someothertopic"))
        p.newsletterHeadline = "foo"

        assertNull(p.newsletterLink)
        assertNull(p.newsletterTopicId)
        assertNull(p.newsletterHeadline)
    }

    @Test
    fun settingNewsletterIssue_isNoOp() {
        val p = newValidEntity()
        assertThat(p.newsletterIssue).isNull()
        p.newsletterIssue = "whatever"
        assertThat(p.newsletterIssue).isNull()
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
