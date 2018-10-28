package ch.difty.scipamato.core.persistence.keyword;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.common.TestUtils;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.keyword.Keyword;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("ResultOfMethodCallIgnored")
public class JooqKeywordServiceTest {

    private JooqKeywordService service;

    @Mock
    private KeywordRepository repoMock;
    @Mock
    private KeywordFilter     filterMock;

    @Mock
    private PaginationContext paginationContextMock;

    @Mock
    private Keyword keywordMock;

    private final List<Keyword> keywords = new ArrayList<>();

    protected KeywordRepository getRepo() {
        return repoMock;
    }

    protected Keyword getEntity() {
        return keywordMock;
    }

    private final List<KeywordDefinition> keywordDefinitions = new ArrayList<>();

    @Mock
    private KeywordDefinition keywordDefinitionMock, persistedKeywordDefinitionMock;

    @Before
    public void setUp() {
        service = new JooqKeywordService(repoMock);

        keywords.add(keywordMock);
        keywords.add(keywordMock);

        keywordDefinitions.add(keywordDefinitionMock);
        keywordDefinitions.add(keywordDefinitionMock);
    }

    @After
    public void specificTearDown() {
        verifyNoMoreInteractions(repoMock, filterMock, paginationContextMock, keywordMock, keywordDefinitionMock);
    }

    @Test
    public void findingAll_delegatesToRepo() {
        final String langCode = "en";
        when(repoMock.findAll(langCode)).thenReturn(keywords);
        assertThat(service.findAll(langCode)).isEqualTo(keywords);
        verify(repoMock).findAll(langCode);
    }

    @Test
    public void newUnpersistedKeywordDefinition_delegatesToRepo() {
        when(repoMock.newUnpersistedKeywordDefinition()).thenReturn(keywordDefinitionMock);
        assertThat(service.newUnpersistedKeywordDefinition()).isEqualTo(keywordDefinitionMock);
        verify(repoMock).newUnpersistedKeywordDefinition();
    }

    @Test
    public void findingPageOfKeywordDefinitions_delegatesToRepo() {
        when(repoMock.findPageOfKeywordDefinitions(filterMock, paginationContextMock)).thenReturn(keywordDefinitions);
        assertThat(service.findPageOfKeywordDefinitions(filterMock, paginationContextMock)).isEqualTo(
            keywordDefinitions);
        verify(repoMock).findPageOfKeywordDefinitions(filterMock, paginationContextMock);
    }

    @Test
    public void countingKeywords_delegatesToRepo() {
        when(repoMock.countByFilter(filterMock)).thenReturn(3);
        assertThat(service.countByFilter(filterMock)).isEqualTo(3);
        verify(repoMock).countByFilter(filterMock);
    }

    @Test
    public void insertingKeywordDefinition_delegatesToRepo() {
        when(repoMock.insert(keywordDefinitionMock)).thenReturn(persistedKeywordDefinitionMock);
        assertThat(service.insert(keywordDefinitionMock)).isEqualTo(persistedKeywordDefinitionMock);
        verify(repoMock).insert(keywordDefinitionMock);
    }

    @Test
    public void updatingKeywordDefinition_delegatesToRepo() {
        when(repoMock.update(keywordDefinitionMock)).thenReturn(persistedKeywordDefinitionMock);
        assertThat(service.update(keywordDefinitionMock)).isEqualTo(persistedKeywordDefinitionMock);
        verify(repoMock).update(keywordDefinitionMock);
    }

    @Test
    public void savingOrUpdatingKeywordDefinition_withNullEntity_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> service.saveOrUpdate(null), "entity");
    }

    @Test
    public void savingOrUpdatingKeywordDefinition_withNullId_callsInsert() {
        when(keywordDefinitionMock.getId()).thenReturn(null);
        when(repoMock.insert(keywordDefinitionMock)).thenReturn(persistedKeywordDefinitionMock);
        assertThat(service.saveOrUpdate(keywordDefinitionMock)).isEqualTo(persistedKeywordDefinitionMock);
        verify(keywordDefinitionMock).getId();
        verify(repoMock).insert(keywordDefinitionMock);
    }

    @Test
    public void savingOrUpdatingKeywordDefinition_withNonNullId_callsUpdate() {
        when(keywordDefinitionMock.getId()).thenReturn(1);
        when(repoMock.update(keywordDefinitionMock)).thenReturn(persistedKeywordDefinitionMock);
        assertThat(service.saveOrUpdate(keywordDefinitionMock)).isEqualTo(persistedKeywordDefinitionMock);
        verify(keywordDefinitionMock).getId();
        verify(repoMock).update(keywordDefinitionMock);
    }

    @Test
    public void deletingKeywordDefinition_delegatesToRepo() {
        final int id = 11;
        final int version = 12;
        when(repoMock.delete(id, version)).thenReturn(persistedKeywordDefinitionMock);
        assertThat(service.delete(id, version)).isEqualTo(persistedKeywordDefinitionMock);
        verify(repoMock).delete(id, version);
    }
}