package ch.difty.scipamato.publ.entity

import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import java.time.LocalDateTime

internal class NewStudyPageLinkTest : PublicDbEntityTest<NewStudyPageLink>() {

    override fun newEntity(created: LocalDateTime, lastModified: LocalDateTime, version: Int) =
        NewStudyPageLink(
            langCode = "en",
            sort = 1,
            title = "foo",
            url = "https://bar.org",
            created = created,
            lastModified = lastModified,
            version = version
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
