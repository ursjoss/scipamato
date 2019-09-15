package ch.difty.scipamato.core.entity.projection

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID
import ch.difty.scipamato.core.entity.projection.PaperSlim.PaperSlimFields.*
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PaperSlimTest {

    private var ps: PaperSlim = PaperSlim()

    @BeforeEach
    fun setUp() {
        ps.id = 1L
        ps.number = 10L
        ps.firstAuthor = "firstAuthor"
        ps.title = "title"
        ps.publicationYear = 2016
        ps.newsletterAssociation = NewsletterAssociation(20, "nl", 1, "hl")
    }

    @Test
    fun getting_hasAllFields() {
        getting(20, "nl", 1, "hl")
    }

    private fun getting(nlId: Int? = null, nlIssue: String? = null, nlStatus: Int? = null, headline: String? = null) {
        assertThat(ps.id).isEqualTo(1L)
        assertThat(ps.number).isEqualTo(10L)
        assertThat(ps.publicationYear).isEqualTo(2016)
        assertThat(ps.title).isEqualTo("title")
        assertThat(ps.firstAuthor).isEqualTo("firstAuthor")
        if (nlId != null) {
            assertThat(ps
                    .newsletterAssociation
                    .id).isEqualTo(nlId)
            assertThat(ps
                    .newsletterAssociation
                    .issue).isEqualTo(nlIssue)
            assertThat(ps
                    .newsletterAssociation
                    .publicationStatusId).isEqualTo(nlStatus)
        } else {
            assertThat(ps.newsletterAssociation).isNull()
        }
    }

    @Test
    fun displayValue() {
        assertThat(ps.displayValue).isEqualTo("firstAuthor (2016): title.")
    }

    @Test
    fun testingToString() {
        assertThat(ps.toString()).isEqualTo(
                "PaperSlim(number=10, firstAuthor=firstAuthor, publicationYear=2016, title=title, newsletter=nl, headline=hl)")
    }

    @Test
    fun testingToString_withNoNewsletter() {
        ps.newsletterAssociation = null
        assertThat(ps.toString()).isEqualTo(
                "PaperSlim(number=10, firstAuthor=firstAuthor, publicationYear=2016, title=title)")
    }

    @Test
    fun testingToString_withNoHeadline() {
        ps
                .newsletterAssociation.headline = null
        assertThat(ps.toString()).isEqualTo(
                "PaperSlim(number=10, firstAuthor=firstAuthor, publicationYear=2016, title=title, newsletter=nl)")
    }

    @Test
    fun alternativeConstructor_withoutNewsletter_hasAllFields_exceptNewsletter() {
        ps = PaperSlim(1L, 10L, "firstAuthor", 2016, "title")
        getting(null, null, null, null)
    }

    @Test
    fun alternativeConstructor_withNewsletterFields() {
        ps = PaperSlim(1L, 10L, "firstAuthor", 2016, "title", 20, "nlTitle", 1, "hl")
        getting(20, "nlTitle", 1, "hl")
    }

    @Test
    fun alternativeConstructor_withNewsletter() {
        ps = PaperSlim(1L, 10L, "firstAuthor", 2016, "title", NewsletterAssociation(30, "t", 3, "headline"))
        getting(30, "t", 3, "headline")
    }

    @Test
    fun equals() {
        EqualsVerifier
                .forClass(PaperSlim::class.java)
                .withRedefinedSuperclass()
                .usingGetClass()
                .withIgnoredFields(CREATED.fieldName, CREATOR_ID.fieldName, MODIFIED.fieldName, MODIFIER_ID.fieldName)
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify()
    }

    @Test
    fun fields() {
        assertThat(PaperSlim.PaperSlimFields.values()).containsExactly(NUMBER, FIRST_AUTHOR, PUBLICATION_YEAR, TITLE, NEWSLETTER_ASSOCIATION)
        assertThat(PaperSlim.PaperSlimFields.values())
                .extracting("name")
                .containsExactly("number", "firstAuthor", "publicationYear", "title", "newsletterAssociation")
    }

}
