@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.common.web.model

import ch.difty.scipamato.common.entity.CodeClassLike
import ch.difty.scipamato.common.persistence.CodeClassLikeService
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify

internal class CodeClassLikeModelTest {

    private val cclMock = mock<CodeClassLike>()
    private val serviceMock = mock<CodeClassLikeService<CodeClassLike>>()
    private val ccls = listOf(cclMock, cclMock)

    private val model =
        object : CodeClassLikeModel<CodeClassLike, CodeClassLikeService<CodeClassLike>>(LANG_CODE, serviceMock) {
            override fun injectThis() {
                // no-op
            }
        }

    @Test
    fun modelObject_gotCodeClassesFromService() {
        whenever(serviceMock.find(LANG_CODE)).thenReturn(ccls)
        assertThat(model.getObject()).containsExactly(cclMock, cclMock)
        verify(serviceMock).find(LANG_CODE)
    }

    companion object {
        private const val LANG_CODE = "en"
    }
}
