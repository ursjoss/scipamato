package ch.difty.scipamato.core.persistence.code

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.Code
import ch.difty.scipamato.core.entity.CodeClass
import ch.difty.scipamato.core.entity.code.CodeDefinition
import ch.difty.scipamato.core.entity.code.CodeFilter
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

internal class JooqCodeServiceTest {

    private val repo = mock<CodeRepository>()
    private val filterMock = mock<CodeFilter>()
    private val paginationContextMock = mock<PaginationContext>()
    private val codeDefinitionMock = mock<CodeDefinition>()
    private val persistedCodeDefinitionMock = mock<CodeDefinition>()

    private val codeDefinitions = listOf(codeDefinitionMock, codeDefinitionMock)

    private val service = JooqCodeService(repo)

    @AfterEach
    fun specificTearDown() {
        verifyNoMoreInteractions(repo, filterMock, paginationContextMock, codeDefinitionMock)
    }

    @Test
    fun findingCodes_delegatesToRepo() {
        val ccId = CodeClassId.CC1
        val languageCode = "de"

        val codes = listOf(
            Code("c1", "Code1", null, false, 1, "cc1", "", 1),
            Code("c2", "Code2", null, false, 1, "cc1", "", 2)
        )
        whenever(repo.findCodesOfClass(ccId, languageCode)).thenReturn(codes)

        assertThat(service.findCodesOfClass(ccId, languageCode).map { it.code }).containsOnly("c1", "c2")

        verify(repo).findCodesOfClass(ccId, languageCode)

        verifyNoMoreInteractions(repo)
    }

    @Test
    fun newUnpersistedCodeDefinition_delegatesToRepo() {
        whenever(repo.newUnpersistedCodeDefinition()).thenReturn(codeDefinitionMock)
        assertThat(service.newUnpersistedCodeDefinition()).isEqualTo(codeDefinitionMock)
        verify(repo).newUnpersistedCodeDefinition()
    }

    @Test
    fun findingPageOfCodeDefinitions_delegatesToRepo() {
        whenever(repo.findPageOfCodeDefinitions(filterMock, paginationContextMock)).thenReturn(codeDefinitions)
        assertThat(service.findPageOfCodeDefinitions(filterMock, paginationContextMock)).isEqualTo(codeDefinitions)
        verify(repo).findPageOfCodeDefinitions(filterMock, paginationContextMock)
    }

    @Test
    fun gettingPageOfEntityDefinitions_delegatesToRepo() {
        whenever(repo.findPageOfCodeDefinitions(filterMock, paginationContextMock)).thenReturn(codeDefinitions)
        assertThat(service.findPageOfEntityDefinitions(filterMock, paginationContextMock)).toIterable()
            .hasSameElementsAs(codeDefinitions)
        verify(repo).findPageOfCodeDefinitions(filterMock, paginationContextMock)
    }

    @Test
    fun countingCodes_delegatesToRepo() {
        whenever(repo.countByFilter(filterMock)).thenReturn(3)
        assertThat(service.countByFilter(filterMock)).isEqualTo(3)
        verify(repo).countByFilter(filterMock)
    }

    @Test
    fun insertingCodeDefinition_delegatesToRepo() {
        whenever(repo.saveOrUpdate(codeDefinitionMock)).thenReturn(persistedCodeDefinitionMock)
        assertThat(service.saveOrUpdate(codeDefinitionMock)).isEqualTo(persistedCodeDefinitionMock)
        verify(repo).saveOrUpdate(codeDefinitionMock)
    }

    @Test
    fun deletingCodeDefinition_delegatesToRepo() {
        val code = "1A"
        val version = 12
        whenever(repo.delete(code, version)).thenReturn(persistedCodeDefinitionMock)
        assertThat(service.delete(code, version)).isEqualTo(persistedCodeDefinitionMock)
        verify(repo).delete(code, version)
    }

    @Test
    fun gettingCodeClass1_delegatesToRepo() {
        val cc1 = CodeClass(1, "cc1", "d1")
        whenever(repo.getCodeClass1("en")).thenReturn(cc1)
        assertThat(service.getCodeClass1("en")).isEqualTo(cc1)
        verify(repo).getCodeClass1("en")
    }
}
