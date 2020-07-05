package ch.difty.scipamato.common.persistence.codeclass

import ch.difty.scipamato.common.entity.CodeClassLike
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test

internal class JooqCodeClassLikeServiceTest {

    private val cclMock = mockk<CodeClassLike>()
    private val repoMock = mockk<CodeClassLikeRepository<CodeClassLike>>()

    private val codeClasses = listOf(cclMock, cclMock)

    private val service = object :
        JooqCodeClassLikeService<CodeClassLike, CodeClassLikeRepository<CodeClassLike>>(repoMock) {
    }

    @Test
    fun canGetRepo() {
        service.repo shouldBeEqualTo repoMock
    }

    @Test
    fun finding_delegatesToRepo() {
        every { repoMock.find(LANG_CODE) } returns codeClasses
        service.find(LANG_CODE) shouldContainAll listOf(cclMock, cclMock)
        verify { repoMock.find(LANG_CODE) }
    }

    companion object {
        private const val LANG_CODE = "en"
    }
}
