package ch.difty.scipamato.core.persistence.keyword

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.keyword.Keyword
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition
import ch.difty.scipamato.core.entity.keyword.KeywordFilter
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

internal class JooqKeywordServiceTest {

    private val repo = mock<KeywordRepository>()
    private val filterMock = mock<KeywordFilter>()
    private val paginationContextMock = mock<PaginationContext>()
    private val entity = mock<Keyword>()
    private val keywordDefinitionMock = mock<KeywordDefinition>()
    private val persistedKeywordDefinitionMock = mock<KeywordDefinition>()

    private var service = JooqKeywordService(repo)

    private val keywords = listOf(entity, entity)
    private val keywordDefinitions = listOf(keywordDefinitionMock, keywordDefinitionMock)

    @AfterEach
    fun specificTearDown() {
        verifyNoMoreInteractions(repo, filterMock, paginationContextMock, entity, keywordDefinitionMock)
    }

    @Test
    fun findingAll_delegatesToRepo() {
        val langCode = "en"
        whenever(repo.findAll(langCode)).thenReturn(keywords)
        assertThat(service.findAll(langCode)).isEqualTo(keywords)
        verify(repo).findAll(langCode)
    }

    @Test
    fun newUnpersistedKeywordDefinition_delegatesToRepo() {
        whenever(repo.newUnpersistedKeywordDefinition()).thenReturn(keywordDefinitionMock)
        assertThat(service.newUnpersistedKeywordDefinition()).isEqualTo(keywordDefinitionMock)
        verify(repo).newUnpersistedKeywordDefinition()
    }

    @Test
    fun findingPageOfKeywordDefinitions_delegatesToRepo() {
        whenever(repo.findPageOfKeywordDefinitions(filterMock, paginationContextMock)).thenReturn(keywordDefinitions)
        assertThat(service.findPageOfKeywordDefinitions(filterMock, paginationContextMock)).isEqualTo(
            keywordDefinitions
        )
        verify(repo).findPageOfKeywordDefinitions(filterMock, paginationContextMock)
    }

    @Test
    fun findingPageOfEntityDefinitions_delegatesToRepo() {
        whenever(repo.findPageOfKeywordDefinitions(filterMock, paginationContextMock)).thenReturn(keywordDefinitions)
        assertThat(service.findPageOfEntityDefinitions(filterMock, paginationContextMock)).toIterable()
            .hasSameElementsAs(keywordDefinitions)
        verify(repo).findPageOfKeywordDefinitions(filterMock, paginationContextMock)
    }

    @Test
    fun countingKeywords_delegatesToRepo() {
        whenever(repo.countByFilter(filterMock)).thenReturn(3)
        assertThat(service.countByFilter(filterMock)).isEqualTo(3)
        verify(repo).countByFilter(filterMock)
    }

    @Test
    fun insertingKeywordDefinition_delegatesToRepo() {
        whenever(repo.insert(keywordDefinitionMock)).thenReturn(persistedKeywordDefinitionMock)
        assertThat(service.insert(keywordDefinitionMock)).isEqualTo(persistedKeywordDefinitionMock)
        verify(repo).insert(keywordDefinitionMock)
    }

    @Test
    fun updatingKeywordDefinition_delegatesToRepo() {
        whenever(repo.update(keywordDefinitionMock)).thenReturn(persistedKeywordDefinitionMock)
        assertThat(service.update(keywordDefinitionMock)).isEqualTo(persistedKeywordDefinitionMock)
        verify(repo).update(keywordDefinitionMock)
    }

    @Test
    fun savingOrUpdatingKeywordDefinition_withNullId_callsInsert() {
        whenever(keywordDefinitionMock.id).thenReturn(null)
        whenever(repo.insert(keywordDefinitionMock)).thenReturn(persistedKeywordDefinitionMock)
        assertThat(service.saveOrUpdate(keywordDefinitionMock)).isEqualTo(persistedKeywordDefinitionMock)
        verify(keywordDefinitionMock).id
        verify(repo).insert(keywordDefinitionMock)
    }

    @Test
    fun savingOrUpdatingKeywordDefinition_withNonNullId_callsUpdate() {
        whenever(keywordDefinitionMock.id).thenReturn(1)
        whenever(repo.update(keywordDefinitionMock)).thenReturn(persistedKeywordDefinitionMock)
        assertThat(service.saveOrUpdate(keywordDefinitionMock)).isEqualTo(persistedKeywordDefinitionMock)
        verify(keywordDefinitionMock).id
        verify(repo).update(keywordDefinitionMock)
    }

    @Test
    fun deletingKeywordDefinition_delegatesToRepo() {
        val id = 11
        val version = 12
        whenever(repo.delete(id, version)).thenReturn(persistedKeywordDefinitionMock)
        assertThat(service.delete(id, version)).isEqualTo(persistedKeywordDefinitionMock)
        verify(repo).delete(id, version)
    }
}
