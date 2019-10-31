package ch.difty.scipamato.publ.persistence.code

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.publ.entity.Code
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

internal class JooqCodeServiceTest {

    private val repoMock = mock<CodeRepository>()
    private val service = JooqCodeService(repoMock)

    private val ccId = CodeClassId.CC1
    private val languageCode = "de"

    private val codes = listOf(
        Code(1, "c1", "en", "Code1", null, 1),
        Code(1, "c2", "en", "Code2", null, 2)
    )

    @Test
    fun findingCodes_delegatesToRepo() {
        whenever(repoMock.findCodesOfClass(ccId, languageCode)).thenReturn(codes)
        assertThat(service.findCodesOfClass(ccId, languageCode).map { it.code }).containsOnly("c1", "c2")
        verify(repoMock).findCodesOfClass(ccId, languageCode)
        verifyNoMoreInteractions(repoMock)
    }
}
