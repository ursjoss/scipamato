package ch.difty.scipamato.common.web.model

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.entity.CodeLike
import ch.difty.scipamato.common.persistence.CodeLikeService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

private const val LANG_CODE = "en"
private val CC_ID = CodeClassId.CC1

@Suppress("SpellCheckingInspection")
internal class CodeLikeModelTest {

    private val cclMock = mockk<CodeLike>()
    private val serviceMock = mockk<CodeLikeService<CodeLike>>()

    private val ccls = listOf(cclMock, cclMock)

    private val model = object : CodeLikeModel<CodeLike, CodeLikeService<CodeLike>>(CC_ID, LANG_CODE, serviceMock) {
        private val serialVersionUID: Long = 1L
        override fun injectThis() {
            // no-op
        }
    }

    @Test
    fun canGetCodeClass() {
        model.codeClassId shouldBeEqualTo CC_ID
    }

    @Test
    fun canGetLanguageCode() {
        model.languageCode shouldBeEqualTo LANG_CODE
    }

    @Test
    fun modelObject_gotCodeClassesFromService() {
        every { serviceMock.findCodesOfClass(CC_ID, LANG_CODE) } returns ccls
        model.getObject()?.shouldContainAll(listOf(cclMock, cclMock)) ?: fail("should have list as object")
        verify { serviceMock.findCodesOfClass(CC_ID, LANG_CODE) }
    }
}
