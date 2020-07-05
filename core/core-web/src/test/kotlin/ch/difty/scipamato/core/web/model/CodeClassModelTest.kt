package ch.difty.scipamato.core.web.model

import ch.difty.scipamato.core.entity.CodeClass
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test
import java.util.ArrayList

internal class CodeClassModelTest : ModelTest() {

    @Test
    fun loading_delegatesToCodeClassService() {
        val languageCode = "de"
        val codeClasses: MutableList<CodeClass> = ArrayList()
        codeClasses.add(CodeClass(1, "cc1", ""))
        codeClasses.add(CodeClass(2, "cc2", ""))
        every { codeClassServiceMock.find(languageCode) } returns codeClasses
        val model = CodeClassModel("de")
        model.load().map { it.name } shouldContainSame listOf("cc1", "cc2")
        verify { codeClassServiceMock.find(languageCode) }
        confirmVerified(codeClassServiceMock)
    }
}
