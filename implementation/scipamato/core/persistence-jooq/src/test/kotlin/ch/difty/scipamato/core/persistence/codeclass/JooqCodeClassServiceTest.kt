package ch.difty.scipamato.core.persistence.codeclass

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.CodeClass
import ch.difty.scipamato.core.entity.code_class.CodeClassDefinition
import ch.difty.scipamato.core.entity.code_class.CodeClassFilter
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

internal class JooqCodeClassServiceTest {

    private val repo = mock<CodeClassRepository>()
    private val filterMock = mock<CodeClassFilter>()
    private val paginationContextMock = mock<PaginationContext>()
    private val codeClassDefinitionMock = mock<CodeClassDefinition>()
    private val persistedCodeClassDefinitionMock = mock<CodeClassDefinition>()

    private var service = JooqCodeClassService(repo)
    private val codeClassDefinitions = listOf(codeClassDefinitionMock, codeClassDefinitionMock)

    @AfterEach
    fun specificTearDown() {
        verifyNoMoreInteractions(repo, filterMock, paginationContextMock, codeClassDefinitionMock)
    }

    @Test
    fun findingCodes_delegatesToRepo() {
        val languageCodeClass = "de"

        val ccs = listOf(CodeClass(1, "cc1", ""), CodeClass(2, "cc2", ""))
        whenever(repo.find(languageCodeClass)).thenReturn(ccs)

        assertThat(service.find(languageCodeClass).map { it.name }).containsOnly("cc1", "cc2")

        verify(repo).find(languageCodeClass)

        verifyNoMoreInteractions(repo)
    }

    @Test
    fun newUnpersistedCodeClassDefinition_delegatesToRepo() {
        whenever(repo.newUnpersistedCodeClassDefinition()).thenReturn(codeClassDefinitionMock)
        assertThat(service.newUnpersistedCodeClassDefinition()).isEqualTo(codeClassDefinitionMock)
        verify(repo).newUnpersistedCodeClassDefinition()
    }

    @Test
    fun findingPageOfCodeClassDefinitions_delegatesToRepo() {
        whenever(repo.findPageOfCodeClassDefinitions(filterMock, paginationContextMock)).thenReturn(codeClassDefinitions)
        assertThat(service.findPageOfCodeClassDefinitions(filterMock, paginationContextMock)).isEqualTo(codeClassDefinitions)
        verify(repo).findPageOfCodeClassDefinitions(filterMock, paginationContextMock)
    }

    @Test
    fun gettingPageOfEntityDefinitions_delegatesToRepo() {
        whenever(repo.findPageOfCodeClassDefinitions(filterMock, paginationContextMock)).thenReturn(codeClassDefinitions)
        assertThat(service.findPageOfEntityDefinitions(filterMock, paginationContextMock)).hasSameElementsAs(codeClassDefinitions)
        verify(repo).findPageOfCodeClassDefinitions(filterMock, paginationContextMock)
    }

    @Test
    fun countingCodes_delegatesToRepo() {
        whenever(repo.countByFilter(filterMock)).thenReturn(3)
        assertThat(service.countByFilter(filterMock)).isEqualTo(3)
        verify(repo).countByFilter(filterMock)
    }

    @Test
    fun insertingCodeClassDefinition_delegatesToRepo() {
        whenever(repo.saveOrUpdate(codeClassDefinitionMock)).thenReturn(persistedCodeClassDefinitionMock)
        assertThat(service.saveOrUpdate(codeClassDefinitionMock)).isEqualTo(persistedCodeClassDefinitionMock)
        verify(repo).saveOrUpdate(codeClassDefinitionMock)
    }

    @Test
    fun deletingCodeClassDefinition_delegatesToRepo() {
        val id = 1
        val version = 12
        whenever(repo.delete(id, version)).thenReturn(persistedCodeClassDefinitionMock)
        assertThat(service.delete(id, version)).isEqualTo(persistedCodeClassDefinitionMock)
        verify(repo).delete(id, version)
    }

}
