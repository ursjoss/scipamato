package ch.difty.scipamato.core.entity.projection

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID
import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NewsletterAssociationTest {

    private var na = NewsletterAssociation()

    @BeforeEach
    fun setUp() {
        na.id = 1
        na.issue = "issue"
        na.publicationStatusId = 2
        na.headline = "hl"
    }

    @Test
    fun getting() {
        na.id shouldBeEqualTo 1
        na.issue shouldBeEqualTo "issue"
        na.publicationStatusId shouldBeEqualTo 2
        na.headline shouldBeEqualTo "hl"
    }

    @Test
    fun displayValue() {
        na.displayValue shouldBeEqualTo "issue"
    }

    @Test
    fun testingToString() {
        na.toString() shouldBeEqualTo "NewsletterAssociation(issue=issue, publicationStatusId=2, headline=hl)"
    }

    @Test
    fun alternativeConstructor() {
        na = NewsletterAssociation(1, "issue", 2, "hl")
        getting()
    }

    @Test
    fun equals() {
        EqualsVerifier.simple()
            .forClass(NewsletterAssociation::class.java)
            .withRedefinedSuperclass()
            .usingGetClass()
            .withIgnoredFields(CREATED.fieldName, CREATOR_ID.fieldName, MODIFIED.fieldName, MODIFIER_ID.fieldName)
            .verify()
    }

    @Test
    fun assertEnumFields() {
        NewsletterAssociation.NewsletterAssociationFields.values().map { it.fieldName } shouldContainSame
            listOf("id", "issue", "publicationStatusId", "headline")
    }
}
