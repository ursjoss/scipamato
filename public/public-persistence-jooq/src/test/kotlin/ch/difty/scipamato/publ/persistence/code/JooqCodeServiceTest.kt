package ch.difty.scipamato.publ.persistence.code

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.publ.entity.Code
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

internal class JooqCodeServiceTest {

    private val repoMock = mockk<CodeRepository>()
    private val service = JooqCodeService(repoMock)

    private val ccId = CodeClassId.CC1
    private val languageCode = "de"

    private val codes = listOf(
        Code(1, "c1", "en", "Code1", null, 1),
        Code(1, "c2", "en", "Code2", null, 2)
    )

    @Test
    fun findingCodes_delegatesToRepo() {
        every { repoMock.findCodesOfClass(ccId, languageCode) } returns codes
        service.findCodesOfClass(ccId, languageCode).map { it.code } shouldContainSame listOf("c1", "c2")
        verify { repoMock.findCodesOfClass(ccId, languageCode) }
        confirmVerified(repoMock)
    }
}
