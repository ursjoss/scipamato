package ch.difty.scipamato.publ.web.model

import ch.difty.scipamato.publ.entity.CodeClass
import ch.difty.scipamato.publ.persistence.api.CodeClassService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test
import java.util.ArrayList

class CodeClassModelTest : ModelTest() {

    @MockkBean
    private lateinit var serviceMock: CodeClassService

    @Test
    fun loading_delegatesToCodeClassService() {
        val languageCode = "de"
        val codeClasses: MutableList<CodeClass> = ArrayList()
        codeClasses.add(CodeClass(1, "en", "cc1", ""))
        codeClasses.add(CodeClass(2, "en", "cc2", ""))

        every { serviceMock.find(languageCode) } returns codeClasses

        val model = CodeClassModel("de")
        model.load().map { it.name } shouldContainSame listOf("cc1", "cc2")

        verify { serviceMock.find(languageCode) }
        confirmVerified(serviceMock)
    }
}
