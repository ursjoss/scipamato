package ch.difty.scipamato.publ.persistence.keyword

import ch.difty.scipamato.publ.entity.Keyword
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

internal class JooqKeywordServiceTest {

    private val repoMock = mockk<KeywordRepository>()
    private val service = JooqKeywordService(repoMock)

    private val languageCode = "de"

    private val keywords = listOf(
        Keyword(1, 1, "en", "Keyword1", null),
        Keyword(1, 1, "fr", "Keyword2", null)
    )

    @Test
    fun findingKeywords_delegatesToRepo() {
        every { repoMock.findKeywords(languageCode) } returns keywords
        service.findKeywords(languageCode).map { it.langCode } shouldContainSame listOf("en", "fr")
        verify { repoMock.findKeywords(languageCode) }
        confirmVerified(repoMock)
    }
}
