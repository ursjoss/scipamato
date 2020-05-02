@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.persistence.keyword

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.keyword.Keyword
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition
import ch.difty.scipamato.core.entity.keyword.KeywordFilter
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class JooqKeywordServiceTest {

    private val repo = mockk<KeywordRepository>()
    private val filterMock = mockk<KeywordFilter>()
    private val paginationContextMock = mockk<PaginationContext>()
    private val entity = mockk<Keyword>()
    private val keywordDefinitionMock = mockk<KeywordDefinition>()
    private val persistedKeywordDefinitionMock = mockk<KeywordDefinition>()

    private var service = JooqKeywordService(repo)

    private val keywords = listOf(entity, entity)
    private val keywordDefinitions = listOf(keywordDefinitionMock, keywordDefinitionMock)

    @AfterEach
    fun specificTearDown() {
        confirmVerified(repo, filterMock, paginationContextMock, entity, keywordDefinitionMock)
    }

    @Test
    fun findingAll_delegatesToRepo() {
        val langCode = "en"
        every { repo.findAll(langCode) } returns keywords
        service.findAll(langCode) shouldBeEqualTo keywords
        verify { repo.findAll(langCode) }
    }

    @Test
    fun newUnpersistedKeywordDefinition_delegatesToRepo() {
        every { repo.newUnpersistedKeywordDefinition() } returns keywordDefinitionMock
        service.newUnpersistedKeywordDefinition() shouldBeEqualTo keywordDefinitionMock
        verify { keywordDefinitionMock == keywordDefinitionMock}
        verify { repo.newUnpersistedKeywordDefinition() }
    }

    @Test
    fun findingPageOfKeywordDefinitions_delegatesToRepo() {
        every { repo.findPageOfKeywordDefinitions(filterMock, paginationContextMock) } returns keywordDefinitions
        service.findPageOfKeywordDefinitions(filterMock, paginationContextMock) shouldBeEqualTo keywordDefinitions
        verify { repo.findPageOfKeywordDefinitions(filterMock, paginationContextMock) }
    }

    @Test
    fun findingPageOfEntityDefinitions_delegatesToRepo() {
        every { repo.findPageOfKeywordDefinitions(filterMock, paginationContextMock) } returns keywordDefinitions
        service.findPageOfEntityDefinitions(filterMock, paginationContextMock).asSequence() shouldContainSame
            keywordDefinitions.asSequence()
        verify { keywordDefinitionMock == keywordDefinitionMock}
        verify { repo.findPageOfKeywordDefinitions(filterMock, paginationContextMock) }
    }

    @Test
    fun countingKeywords_delegatesToRepo() {
        every { repo.countByFilter(filterMock) } returns 3
        service.countByFilter(filterMock) shouldBeEqualTo 3
        verify { repo.countByFilter(filterMock) }
    }

    @Test
    fun insertingKeywordDefinition_delegatesToRepo() {
        every { repo.insert(keywordDefinitionMock) } returns persistedKeywordDefinitionMock
        service.insert(keywordDefinitionMock) shouldBeEqualTo persistedKeywordDefinitionMock
        verify { repo.insert(keywordDefinitionMock) }
    }

    @Test
    fun updatingKeywordDefinition_delegatesToRepo() {
        every { repo.update(keywordDefinitionMock) } returns persistedKeywordDefinitionMock
        service.update(keywordDefinitionMock) shouldBeEqualTo persistedKeywordDefinitionMock
        verify { repo.update(keywordDefinitionMock) }
    }

    @Test
    fun savingOrUpdatingKeywordDefinition_withNullId_callsInsert() {
        every { keywordDefinitionMock.id } returns null
        every { repo.insert(keywordDefinitionMock) } returns persistedKeywordDefinitionMock
        service.saveOrUpdate(keywordDefinitionMock) shouldBeEqualTo persistedKeywordDefinitionMock
        verify { keywordDefinitionMock.id }
        verify { repo.insert(keywordDefinitionMock) }
    }

    @Test
    fun savingOrUpdatingKeywordDefinition_withNonNullId_callsUpdate() {
        every { keywordDefinitionMock.id } returns 1
        every { repo.update(keywordDefinitionMock) } returns persistedKeywordDefinitionMock
        service.saveOrUpdate(keywordDefinitionMock) shouldBeEqualTo persistedKeywordDefinitionMock
        verify { keywordDefinitionMock.id }
        verify { repo.update(keywordDefinitionMock) }
    }

    @Test
    fun deletingKeywordDefinition_delegatesToRepo() {
        val id = 11
        val version = 12
        every { repo.delete(id, version) } returns persistedKeywordDefinitionMock
        service.delete(id, version) shouldBeEqualTo persistedKeywordDefinitionMock
        verify { repo.delete(id, version) }
    }
}
