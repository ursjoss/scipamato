package ch.difty.scipamato.common.persistence.code

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.entity.CodeLike
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test

internal class JooqCodeLikeServiceTest {

    private val repoMock = mockk<CodeLikeRepository<CodeLike>>()
    private val cclMock = mockk<CodeLike>()

    private val codeClasses = listOf(cclMock, cclMock)

    private val service = object : JooqCodeLikeService<CodeLike, CodeLikeRepository<CodeLike>>(repoMock) {
    }

    @Test
    fun canGetRepo() {
        service.repo shouldBeEqualTo repoMock
    }

    @Test
    fun finding_delegatesToRepo() {
        every { repoMock.findCodesOfClass(CC_ID, LANG_CODE) } returns codeClasses
        service.findCodesOfClass(CC_ID, LANG_CODE) shouldContainAll listOf(cclMock, cclMock)
        verify { repoMock.findCodesOfClass(CC_ID, LANG_CODE) }
    }

    companion object {
        private const val LANG_CODE = "en"
        private val CC_ID = CodeClassId.CC1
    }
}
