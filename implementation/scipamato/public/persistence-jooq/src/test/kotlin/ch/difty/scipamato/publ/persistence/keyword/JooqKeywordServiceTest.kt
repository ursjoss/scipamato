package ch.difty.scipamato.publ.persistence.keyword

import ch.difty.scipamato.publ.entity.Keyword
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

internal class JooqKeywordServiceTest {

    private val repoMock = mock<KeywordRepository>()
    private val service = JooqKeywordService(repoMock)

    private val languageCode = "de"

    private val keywords = listOf(
            Keyword(1, 1, "en", "Keyword1", null),
            Keyword(1, 1, "fr", "Keyword2", null)
    )

    @Test
    fun findingKeywords_delegatesToRepo() {
        whenever(repoMock.findKeywords(languageCode)).thenReturn(keywords)
        assertThat(service.findKeywords(languageCode).map { it.langCode }).containsOnly("en", "fr")
        verify(repoMock).findKeywords(languageCode)
        verifyNoMoreInteractions(repoMock)
    }
}