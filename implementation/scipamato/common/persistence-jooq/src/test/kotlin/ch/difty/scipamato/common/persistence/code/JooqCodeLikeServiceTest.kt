package ch.difty.scipamato.common.persistence.code

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.entity.CodeLike
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify

internal class JooqCodeLikeServiceTest {

    private val repoMock = mock<CodeLikeRepository<CodeLike>>()
    private val cclMock = mock<CodeLike>()

    private val codeClasses = listOf(cclMock, cclMock)

    private val service = object : JooqCodeLikeService<CodeLike, CodeLikeRepository<CodeLike>>(repoMock) {
    }

    @Test
    fun canGetRepo() {
        assertThat(service.repo).isEqualTo(repoMock)
    }

    @Test
    fun finding_delegatesToRepo() {
        whenever(repoMock.findCodesOfClass(CC_ID, LANG_CODE)).thenReturn(codeClasses)
        assertThat(service.findCodesOfClass(CC_ID, LANG_CODE)).containsExactly(cclMock, cclMock)
        verify(repoMock).findCodesOfClass(CC_ID, LANG_CODE)
    }

    companion object {
        private const val LANG_CODE = "en"
        private val CC_ID = CodeClassId.CC1
    }

}
