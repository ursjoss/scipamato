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
    private Keyword topicMock;

    private final List<Keyword> topics = new ArrayList<>();

    protected KeywordRepository getRepo() {
        return repoMock;
    }

    protected Keyword getEntity() {
        return topicMock;
    }

    private final List<KeywordDefinition> topicDefinitions = new ArrayList<>();

    @Mock
    private KeywordDefinition topicDefinitionMock, persistedTopicDefinitionMock;

    @Before
    public void setUp() {
        service = new JooqKeywordService(repoMock);

        topics.add(topicMock);
        topics.add(topicMock);

        topicDefinitions.add(topicDefinitionMock);
        topicDefinitions.add(topicDefinitionMock);
    }

    @After
    public void specificTearDown() {
        verifyNoMoreInteractions(repoMock, filterMock, paginationContextMock, topicMock, topicDefinitionMock);
    }

    @Test
    public void findingAll_delegatesToRepo() {
        final String langCode = "en";
        when(repoMock.findAll(langCode)).thenReturn(topics);
        assertThat(service.findAll(langCode)).isEqualTo(topics);
        verify(repoMock).findAll(langCode);
    }

    @Test
    public void newUnpersistedKeywordDefinition_delegatesToRepo() {
        when(repoMock.newUnpersistedKeywordDefinition()).thenReturn(topicDefinitionMock);
        assertThat(service.newUnpersistedKeywordDefinition()).isEqualTo(topicDefinitionMock);
        verify(repoMock).newUnpersistedKeywordDefinition();
    }

    @Test
    public void findingPageOfKeywordDefinitions_delegatesToRepo() {
        when(repoMock.findPageOfKeywordDefinitions(filterMock, paginationContextMock)).thenReturn(topicDefinitions);
        assertThat(service.findPageOfKeywordDefinitions(filterMock, paginationContextMock)).isEqualTo(topicDefinitions);
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
        when(repoMock.insert(topicDefinitionMock)).thenReturn(persistedTopicDefinitionMock);
        assertThat(service.insert(topicDefinitionMock)).isEqualTo(persistedTopicDefinitionMock);
        verify(repoMock).insert(topicDefinitionMock);
    }

    @Test
    public void updatingKeywordDefinition_delegatesToRepo() {
        when(repoMock.update(topicDefinitionMock)).thenReturn(persistedTopicDefinitionMock);
        assertThat(service.update(topicDefinitionMock)).isEqualTo(persistedTopicDefinitionMock);
        verify(repoMock).update(topicDefinitionMock);
    }

    @Test
    public void savingOrUpdatingKeywordDefinition_withNullEntity_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> service.saveOrUpdate(null), "entity");
    }

    @Test
    public void savingOrUpdatingKeywordDefinition_withNullId_callsInsert() {
        when(topicDefinitionMock.getId()).thenReturn(null);
        when(repoMock.insert(topicDefinitionMock)).thenReturn(persistedTopicDefinitionMock);
        assertThat(service.saveOrUpdate(topicDefinitionMock)).isEqualTo(persistedTopicDefinitionMock);
        verify(topicDefinitionMock).getId();
        verify(repoMock).insert(topicDefinitionMock);
    }

    @Test
    public void savingOrUpdatingKeywordDefinition_withNonNullId_callsUpdate() {
        when(topicDefinitionMock.getId()).thenReturn(1);
        when(repoMock.update(topicDefinitionMock)).thenReturn(persistedTopicDefinitionMock);
        assertThat(service.saveOrUpdate(topicDefinitionMock)).isEqualTo(persistedTopicDefinitionMock);
        verify(topicDefinitionMock).getId();
        verify(repoMock).update(topicDefinitionMock);
    }

    @Test
    public void deletingKeywordDefinition_delegatesToRepo() {
        int id = 11;
        int version = 12;
        when(repoMock.delete(id, version)).thenReturn(persistedTopicDefinitionMock);
        assertThat(service.delete(id, version)).isEqualTo(persistedTopicDefinitionMock);
        verify(repoMock).delete(id, version);
    }

}