package ch.difty.scipamato.publ.persistence.codeclass

import ch.difty.scipamato.publ.entity.CodeClass
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

internal class JooqCodeClassServiceTest {

    private val languageCodeClass = "de"

    private val repoMock = mockk<CodeClassRepository>()
    private val service = JooqCodeClassService(repoMock)

    private val ccs = listOf(
        CodeClass(1, "en", "cc1", ""),
        CodeClass(2, "en", "cc2", "")
    )

    @Test
    fun findingCodeClass_delegatesToRepo() {
        every { repoMock.find(languageCodeClass) } returns ccs
        service.find(languageCodeClass).map { it.name } shouldContainSame listOf("cc1", "cc2")
        verify { repoMock.find(languageCodeClass) }
        confirmVerified(repoMock)
    }
}
