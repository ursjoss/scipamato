@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.common.web.model

import ch.difty.scipamato.common.entity.CodeClassLike
import ch.difty.scipamato.common.persistence.CodeClassLikeService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

private const val LANG_CODE = "en"

internal class CodeClassLikeModelTest {

    private val cclMock = mockk<CodeClassLike>()
    private val serviceMock = mockk<CodeClassLikeService<CodeClassLike>>()
    private val ccls = listOf(cclMock, cclMock)

    private val model =
        object : CodeClassLikeModel<CodeClassLike, CodeClassLikeService<CodeClassLike>>(LANG_CODE, serviceMock) {
            override fun injectThis() {
                // no-op
            }
        }

    @Test
    fun modelObject_gotCodeClassesFromService() {
        every { serviceMock.find(LANG_CODE) } returns ccls
        model.getObject()?.shouldContainAll(listOf(cclMock, cclMock)) ?: fail("should have list as object")
        verify { serviceMock.find(LANG_CODE) }
    }
}
