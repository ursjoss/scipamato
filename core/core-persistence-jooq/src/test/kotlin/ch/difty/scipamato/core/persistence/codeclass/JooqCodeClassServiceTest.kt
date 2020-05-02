@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.persistence.codeclass

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.CodeClass
import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition
import ch.difty.scipamato.core.entity.codeclass.CodeClassFilter
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class JooqCodeClassServiceTest {

    private val repo = mockk<CodeClassRepository>()
    private val filterMock = mockk<CodeClassFilter>()
    private val paginationContextMock = mockk<PaginationContext>()
    private val codeClassDefinitionMock = mockk<CodeClassDefinition>()
    private val persistedCodeClassDefinitionMock = mockk<CodeClassDefinition>()

    private var service = JooqCodeClassService(repo)
    private val codeClassDefinitions = listOf(codeClassDefinitionMock, codeClassDefinitionMock)

    @AfterEach
    fun specificTearDown() {
        confirmVerified(repo, filterMock, paginationContextMock, codeClassDefinitionMock)
    }

    @Test
    fun findingCodes_delegatesToRepo() {
        val languageCodeClass = "de"

        val ccs = listOf(CodeClass(1, "cc1", ""), CodeClass(2, "cc2", ""))
        every { repo.find(languageCodeClass) } returns ccs

        service.find(languageCodeClass).map { it.name } shouldContainSame listOf("cc1", "cc2")

        verify { repo.find(languageCodeClass) }

        confirmVerified(repo)
    }

    @Test
    fun newUnpersistedCodeClassDefinition_delegatesToRepo() {
        every { repo.newUnpersistedCodeClassDefinition() } returns codeClassDefinitionMock
        service.newUnpersistedCodeClassDefinition() shouldBeEqualTo codeClassDefinitionMock
        verify { codeClassDefinitionMock == codeClassDefinitionMock }
        verify { repo.newUnpersistedCodeClassDefinition() }
    }

    @Test
    fun findingPageOfCodeClassDefinitions_delegatesToRepo() {
        every { repo.findPageOfCodeClassDefinitions(filterMock, paginationContextMock) } returns codeClassDefinitions
        service.findPageOfCodeClassDefinitions(filterMock, paginationContextMock) shouldBeEqualTo codeClassDefinitions
        verify { repo.findPageOfCodeClassDefinitions(filterMock, paginationContextMock) }
    }

    @Test
    fun gettingPageOfEntityDefinitions_delegatesToRepo() {
        every { repo.findPageOfCodeClassDefinitions(filterMock, paginationContextMock) } returns codeClassDefinitions
        service.findPageOfEntityDefinitions(filterMock, paginationContextMock).asSequence() shouldContainSame
            codeClassDefinitions.asSequence()
        verify { codeClassDefinitionMock == codeClassDefinitionMock }
        verify { repo.findPageOfCodeClassDefinitions(filterMock, paginationContextMock) }
    }

    @Test
    fun countingCodes_delegatesToRepo() {
        every { repo.countByFilter(filterMock) } returns 3
        service.countByFilter(filterMock) shouldBeEqualTo 3
        verify { repo.countByFilter(filterMock) }
    }

    @Test
    fun insertingCodeClassDefinition_delegatesToRepo() {
        every { repo.saveOrUpdate(codeClassDefinitionMock) } returns persistedCodeClassDefinitionMock
        service.saveOrUpdate(codeClassDefinitionMock) shouldBeEqualTo persistedCodeClassDefinitionMock
        verify { repo.saveOrUpdate(codeClassDefinitionMock) }
    }

    @Test
    fun deletingCodeClassDefinition_delegatesToRepo() {
        val id = 1
        val version = 12
        every { repo.delete(id, version) } returns persistedCodeClassDefinitionMock
        service.delete(id, version) shouldBeEqualTo persistedCodeClassDefinitionMock
        verify { repo.delete(id, version) }
    }
}
