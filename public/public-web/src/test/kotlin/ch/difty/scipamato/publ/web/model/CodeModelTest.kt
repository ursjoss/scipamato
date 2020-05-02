package ch.difty.scipamato.publ.web.model

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.publ.entity.Code
import ch.difty.scipamato.publ.persistence.api.CodeService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test
import java.util.ArrayList

internal class CodeModelTest : ModelTest() {
    @MockkBean
    private lateinit var serviceMock: CodeService

    @Test
    fun loading_delegatesToCodeService() {
        val ccId = CodeClassId.CC1
        val languageCode = "de"
        val codes: MutableList<Code> = ArrayList()
        codes.add(Code(1, "1F", "en", "code 1F", null, 1))
        codes.add(Code(1, "1N", "en", "code 1N", null, 2))

        every { serviceMock.findCodesOfClass(ccId, languageCode) } returns codes
        val model = CodeModel(CodeClassId.CC1, "de")
        model.load().map { it.code } shouldContainSame listOf("1F", "1N")

        verify { serviceMock.findCodesOfClass(ccId, languageCode) }
        confirmVerified(serviceMock)
    }
}
