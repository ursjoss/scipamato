package ch.difty.scipamato.common.web.model

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.entity.CodeLike
import ch.difty.scipamato.common.persistence.CodeLikeService
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

internal class CodeLikeModelTest {

    private val cclMock = mock<CodeLike>()
    private val serviceMock = mock<CodeLikeService<CodeLike>>()

    private val ccls = listOf(cclMock, cclMock)

    private val model = object : CodeLikeModel<CodeLike, CodeLikeService<CodeLike>>(CC_ID, LANG_CODE, serviceMock) {
        override fun injectThis() {
            // no-op
        }
    }

    @Test
    fun canGetCodeClass() {
        assertThat(model.codeClassId).isEqualTo(CC_ID)
    }

    @Test
    fun canGetLanguageCode() {
        assertThat(model.languageCode).isEqualTo(LANG_CODE)
    }

    @Test
    fun modelObject_gotCodeClassesFromService() {
        `when`(serviceMock.findCodesOfClass(CC_ID, LANG_CODE)).thenReturn(ccls)
        assertThat(model.getObject()).containsExactly(cclMock, cclMock)
        verify(serviceMock).findCodesOfClass(CC_ID, LANG_CODE)
    }

    companion object {
        private const val LANG_CODE = "en"
        private val CC_ID = CodeClassId.CC1
    }

}
