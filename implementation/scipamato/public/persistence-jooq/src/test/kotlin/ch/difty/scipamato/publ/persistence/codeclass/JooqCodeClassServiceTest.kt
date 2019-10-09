package ch.difty.scipamato.publ.persistence.codeclass

import ch.difty.scipamato.publ.entity.CodeClass
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

internal class JooqCodeClassServiceTest {

    private val languageCodeClass = "de"

    private val repoMock = mock<CodeClassRepository>()
    private val service = JooqCodeClassService(repoMock)

    private val ccs = listOf(
        CodeClass(1, "en", "cc1", ""),
        CodeClass(2, "en", "cc2", "")
    )

    @Test
    fun findingCodeClass_delegatesToRepo() {
        whenever(repoMock.find(languageCodeClass)).thenReturn(ccs)
        assertThat(service.find(languageCodeClass).map { it.name }).containsOnly("cc1", "cc2")
        verify(repoMock).find(languageCodeClass)
        verifyNoMoreInteractions(repoMock)
    }
}
