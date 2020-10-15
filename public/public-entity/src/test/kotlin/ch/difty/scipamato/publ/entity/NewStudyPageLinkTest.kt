package ch.difty.scipamato.publ.entity

import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo

internal class NewStudyPageLinkTest : PublicDbEntityTest<NewStudyPageLink>() {

    override fun newEntity() =
        NewStudyPageLink(
            langCode = "en",
            sort = 1,
            title = "foo",
            url = "https://bar.org",
        )

    override fun assertSpecificGetters() {
        entity.langCode shouldBeEqualTo "en"
        entity.sort shouldBeEqualTo 1
        entity.title shouldBeEqualTo "foo"
        entity.url shouldBeEqualTo "https://bar.org"
    }

    override fun verifyEquals() {
        EqualsVerifier.simple()
            .forClass(NewStudyPageLink::class.java)
            .verify()
    }
}
