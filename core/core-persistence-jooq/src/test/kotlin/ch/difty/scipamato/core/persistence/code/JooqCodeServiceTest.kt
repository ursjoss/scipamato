@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.persistence.code

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.Code
import ch.difty.scipamato.core.entity.CodeClass
import ch.difty.scipamato.core.entity.code.CodeDefinition
import ch.difty.scipamato.core.entity.code.CodeFilter
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class JooqCodeServiceTest {

    private val repo = mockk<CodeRepository>()
    private val filterMock = mockk<CodeFilter>()
    private val paginationContextMock = mockk<PaginationContext>()
    private val codeDefinitionMock = mockk<CodeDefinition>()
    private val persistedCodeDefinitionMock = mockk<CodeDefinition>()

    private val codeDefinitions = listOf(codeDefinitionMock, codeDefinitionMock)

    private val service = JooqCodeService(repo)

    @AfterEach
    fun specificTearDown() {
        confirmVerified(repo, filterMock, paginationContextMock, codeDefinitionMock)
    }

    @Test
    fun findingCodes_delegatesToRepo() {
        val ccId = CodeClassId.CC1
        val languageCode = "de"

        val codes = listOf(
            Code("c1", "Code1", null, false, 1, "cc1", "", 1),
            Code("c2", "Code2", null, false, 1, "cc1", "", 2)
        )
        every { repo.findCodesOfClass(ccId, languageCode) } returns codes

        service.findCodesOfClass(ccId, languageCode).map { it.code } shouldContainSame listOf("c1", "c2")

        verify { repo.findCodesOfClass(ccId, languageCode) }

        confirmVerified(repo)
    }

    @Test
    fun newUnpersistedCodeDefinition_delegatesToRepo() {
        every { repo.newUnpersistedCodeDefinition() } returns codeDefinitionMock
        service.newUnpersistedCodeDefinition() shouldBeEqualTo codeDefinitionMock
        verify { codeDefinitionMock == codeDefinitionMock }
        verify { repo.newUnpersistedCodeDefinition() }
        verify { codeDefinitionMock.toString() }
    }

    @Test
    fun findingPageOfCodeDefinitions_delegatesToRepo() {
        every { repo.findPageOfCodeDefinitions(filterMock, paginationContextMock) } returns codeDefinitions
        service.findPageOfCodeDefinitions(filterMock, paginationContextMock) shouldBeEqualTo codeDefinitions
        verify { repo.findPageOfCodeDefinitions(filterMock, paginationContextMock) }
        verify { codeDefinitionMock.toString() }
    }

    @Test
    fun gettingPageOfEntityDefinitions_delegatesToRepo() {
        every { repo.findPageOfCodeDefinitions(filterMock, paginationContextMock) } returns codeDefinitions
        service.findPageOfEntityDefinitions(filterMock, paginationContextMock).asSequence() shouldContainSame
            codeDefinitions.asSequence()
        verify { codeDefinitionMock == codeDefinitionMock }
        verify { repo.findPageOfCodeDefinitions(filterMock, paginationContextMock) }
    }

    @Test
    fun countingCodes_delegatesToRepo() {
        every { repo.countByFilter(filterMock) } returns 3
        service.countByFilter(filterMock) shouldBeEqualTo 3
        verify { repo.countByFilter(filterMock) }
    }

    @Test
    fun insertingCodeDefinition_delegatesToRepo() {
        every { repo.saveOrUpdate(codeDefinitionMock) } returns persistedCodeDefinitionMock
        service.saveOrUpdate(codeDefinitionMock) shouldBeEqualTo persistedCodeDefinitionMock
        verify { repo.saveOrUpdate(codeDefinitionMock) }
    }

    @Test
    fun deletingCodeDefinition_delegatesToRepo() {
        val code = "1A"
        val version = 12
        every { repo.delete(code, version) } returns persistedCodeDefinitionMock
        service.delete(code, version) shouldBeEqualTo persistedCodeDefinitionMock
        verify { repo.delete(code, version) }
    }

    @Test
    fun gettingCodeClass1_delegatesToRepo() {
        val cc1 = CodeClass(1, "cc1", "d1")
        every { repo.getCodeClass1("en") } returns cc1
        service.getCodeClass1("en") shouldBeEqualTo cc1
        verify { repo.getCodeClass1("en") }
    }
}
