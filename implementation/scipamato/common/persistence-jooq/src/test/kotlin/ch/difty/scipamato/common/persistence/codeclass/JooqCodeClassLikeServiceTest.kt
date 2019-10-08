package ch.difty.scipamato.common.persistence.codeclass

import ch.difty.scipamato.common.entity.CodeClassLike
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify

internal class JooqCodeClassLikeServiceTest {

    private val cclMock = mock<CodeClassLike>()
    private val repoMock = mock<CodeClassLikeRepository<CodeClassLike>>()

    private val codeClasses = listOf(cclMock, cclMock)

    private val service = object :
            JooqCodeClassLikeService<CodeClassLike, CodeClassLikeRepository<CodeClassLike>>(repoMock) {
    }

    @Test
    fun canGetRepo() {
        assertThat(service.repo).isEqualTo(repoMock)
    }

    @Test
    fun finding_delegatesToRepo() {
        whenever(repoMock.find(LANG_CODE)).thenReturn(codeClasses)
        assertThat(service.find(LANG_CODE)).containsExactly(cclMock, cclMock)
        verify(repoMock).find(LANG_CODE)
    }

    companion object {
        private const val LANG_CODE = "en"
    }
}
