package ch.difty.scipamato.core.web.model

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.core.entity.Code
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

internal class CodeModelTest : ModelTest() {

    @Test
    fun loading_delegatesToCodeService() {
        val ccId = CodeClassId.CC1
        val languageCode = "de"
        val codes = listOf(
            Code("1F", "code 1F", null, false, 1, "cc1", "", 1),
            Code("1N", "code 1N", null, false, 1, "cc1", "", 13)
        )
        every { codeServiceMock.findCodesOfClass(ccId, languageCode) } returns codes
        val model = CodeModel(CodeClassId.CC1, "de")

        model.load().map { it.code } shouldContainSame listOf("1F", "1N")

        verify { codeServiceMock.findCodesOfClass(ccId, languageCode) }
        confirmVerified(codeServiceMock)
    }
}