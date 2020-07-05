package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test

internal class NewStudyPageLinkTest : PublicEntityTest<NewStudyPageLink>() {

    override val toString: String
        get() = "NewStudyPageLink(langCode=en, sort=1, title=foo, url=https://bar.org)"

    override fun newEntity(): NewStudyPageLink = NewStudyPageLink("en", 1, "foo", "https://bar.org")

    override fun assertSpecificGetters() {
        entity.langCode shouldBeEqualTo "en"
        entity.sort shouldBeEqualTo 1
        entity.title shouldBeEqualTo "foo"
        entity.url shouldBeEqualTo "https://bar.org"
    }

    override fun verifyEquals() {
        EqualsVerifier.forClass(NewStudy::class.java)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.fieldName, MODIFIED.fieldName)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }

    @Test
    fun assertEnumFields() {
        NewStudyPageLink.NewStudyPageLinkFields.values().map { it.fieldName } shouldContainAll
            listOf("langCode", "sort", "title", "url")
    }
}
