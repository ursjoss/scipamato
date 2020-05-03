package ch.difty.scipamato.core.web.model

import ch.difty.scipamato.core.entity.keyword.Keyword
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

internal class KeywordModelTest : ModelTest() {

    @Test
    fun loading_delegatesToKeywordService() {
        val languageCode = "de"
        val topics = listOf(Keyword(1, "n1", null), Keyword(2, "n2", "nn2"))
        every { keywordServiceMock.findAll(languageCode) } returns topics

        val model = KeywordModel("de")
        val keywords = model.load()

        keywords.map { it.name } shouldContainSame listOf("n1", "n2")
        keywords.map { it.searchOverride } shouldContainSame listOf(null, "nn2")

        verify { keywordServiceMock.findAll(languageCode) }
        confirmVerified(keywordServiceMock)
    }
}
