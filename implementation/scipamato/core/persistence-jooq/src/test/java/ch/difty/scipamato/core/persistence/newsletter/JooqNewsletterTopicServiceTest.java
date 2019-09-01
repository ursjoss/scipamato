package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("ResultOfMethodCallIgnored")
class JooqNewsletterTopicServiceTest {

    private JooqNewsletterTopicService service;

    @Mock
    private NewsletterTopicRepository repoMock;
    @Mock
    private NewsletterTopicFilter     filterMock;

    @Mock
    private PaginationContext paginationContextMock;

    @Mock
    private NewsletterTopic topicMock;

    private final List<NewsletterTopic> topics = new ArrayList<>();

    protected NewsletterTopicRepository getRepo() {
        return repoMock;
    }

    protected NewsletterTopic getEntity() {
        return topicMock;
    }

    private final List<NewsletterTopicDefinition> topicDefinitions = new ArrayList<>();

    @Mock
    private NewsletterTopicDefinition topicDefinitionMock, persistedTopicDefinitionMock;

    @BeforeEach
    void setUp() {
        service = new JooqNewsletterTopicService(repoMock);

        topics.add(topicMock);
        topics.add(topicMock);

        topicDefinitions.add(topicDefinitionMock);
        topicDefinitions.add(topicDefinitionMock);

        //        when(topicMock.getCreatedBy()).thenReturn(10);
    }

    @AfterEach
    void specificTearDown() {
        verifyNoMoreInteractions(repoMock, filterMock, paginationContextMock, topicMock, topicDefinitionMock);
    }

    @Test
    void findingAll_delegatesToRepo() {
        final String langCode = "en";
        when(repoMock.findAll(langCode)).thenReturn(topics);
        assertThat(service.findAll(langCode)).isEqualTo(topics);
        verify(repoMock).findAll(langCode);
    }

    @Test
    void newUnpersistedNewsletterTopicDefinition_delegatesToRepo() {
        when(repoMock.newUnpersistedNewsletterTopicDefinition()).thenReturn(topicDefinitionMock);
        assertThat(service.newUnpersistedNewsletterTopicDefinition()).isEqualTo(topicDefinitionMock);
        verify(repoMock).newUnpersistedNewsletterTopicDefinition();
    }

    @Test
    void findingPageOfNewsletterTopicDefinitions_delegatesToRepo() {
        when(repoMock.findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock)).thenReturn(
            topicDefinitions);
        assertThat(service.findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock)).isEqualTo(
            topicDefinitions);
        verify(repoMock).findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock);
    }

    @Test
    void findingPageOfEntityDefinitions_delegatesToRepo() {
        when(repoMock.findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock)).thenReturn(
            topicDefinitions);
        assertThat(service.findPageOfEntityDefinitions(filterMock, paginationContextMock)).hasSameElementsAs(
            topicDefinitions);
        verify(repoMock).findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock);
    }

    @Test
    void countingNewsletterTopics_delegatesToRepo() {
        when(repoMock.countByFilter(filterMock)).thenReturn(3);
        assertThat(service.countByFilter(filterMock)).isEqualTo(3);
        verify(repoMock).countByFilter(filterMock);
    }

    @Test
    void insertingNewsletterTopicDefinition_delegatesToRepo() {
        when(repoMock.insert(topicDefinitionMock)).thenReturn(persistedTopicDefinitionMock);
        assertThat(service.insert(topicDefinitionMock)).isEqualTo(persistedTopicDefinitionMock);
        verify(repoMock).insert(topicDefinitionMock);
    }

    @Test
    void updatingNewsletterTopicDefinition_delegatesToRepo() {
        when(repoMock.update(topicDefinitionMock)).thenReturn(persistedTopicDefinitionMock);
        assertThat(service.update(topicDefinitionMock)).isEqualTo(persistedTopicDefinitionMock);
        verify(repoMock).update(topicDefinitionMock);
    }

    @Test
    void savingOrUpdatingNewsletterTopicDefinition_withNullEntity_throws() {
        assertDegenerateSupplierParameter(() -> service.saveOrUpdate(null), "entity");
    }

    @Test
    void savingOrUpdatingNewsletterTopicDefinition_withNullId_callsInsert() {
        when(topicDefinitionMock.getId()).thenReturn(null);
        when(repoMock.insert(topicDefinitionMock)).thenReturn(persistedTopicDefinitionMock);
        assertThat(service.saveOrUpdate(topicDefinitionMock)).isEqualTo(persistedTopicDefinitionMock);
        verify(topicDefinitionMock).getId();
        verify(repoMock).insert(topicDefinitionMock);
    }

    @Test
    void savingOrUpdatingNewsletterTopicDefinition_withNonNullId_callsUpdate() {
        when(topicDefinitionMock.getId()).thenReturn(1);
        when(repoMock.update(topicDefinitionMock)).thenReturn(persistedTopicDefinitionMock);
        assertThat(service.saveOrUpdate(topicDefinitionMock)).isEqualTo(persistedTopicDefinitionMock);
        verify(topicDefinitionMock).getId();
        verify(repoMock).update(topicDefinitionMock);
    }

    @Test
    void deletingNewsletterTopicDefinition_delegatesToRepo() {
        int id = 11;
        int version = 12;
        when(repoMock.delete(id, version)).thenReturn(persistedTopicDefinitionMock);
        assertThat(service.delete(id, version)).isEqualTo(persistedTopicDefinitionMock);
        verify(repoMock).delete(id, version);
    }

    @Test
    void gettingSortedNewsletterTopicsForNewsletter_withNoAssignedTopics_isEmpty() {
        final int newsletterId = 12;

        when(repoMock.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(
            Lists.emptyList());
        when(repoMock.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(Lists.emptyList());

        assertThat(service.getSortedNewsletterTopicsForNewsletter(newsletterId)).isEmpty();

        verify(repoMock).removeObsoleteNewsletterTopicsFromSort(newsletterId);
        verify(repoMock).findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId);
        verify(repoMock).findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId);
    }

    @Test
    void gettingSortedNewsletterTopicsForNewsletter_withNoPersistedTopics_resortsAllUnpersisted() {
        final int newsletterId = 12;
        final List<NewsletterNewsletterTopic> persisted = Lists.emptyList();
        final List<NewsletterNewsletterTopic> all = List.of(
            new NewsletterNewsletterTopic(newsletterId, 1, Integer.MAX_VALUE, "topic1"),
            new NewsletterNewsletterTopic(newsletterId, 2, Integer.MAX_VALUE, "topic2"));

        when(repoMock.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(persisted);
        when(repoMock.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(all);

        final List<NewsletterNewsletterTopic> topics = service.getSortedNewsletterTopicsForNewsletter(newsletterId);

        assertThat(topics).hasSize(2);
        assertThat(topics)
            .extracting("newsletterTopicId")
            .containsExactly(1, 2);
        assertThat(topics)
            .extracting("sort")
            .containsExactly(0, 1);
        assertThat(topics)
            .extracting("title")
            .containsExactly("topic1", "topic2");

        verify(repoMock).removeObsoleteNewsletterTopicsFromSort(newsletterId);
        verify(repoMock).findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId);
        verify(repoMock).findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId);
    }

    @Test
    void gettingSortedNewsletterTopicsForNewsletter_withAllPersistedTopics_returnsThoseOnly() {
        final int newsletterId = 12;
        final List<NewsletterNewsletterTopic> persisted = List.of(
            new NewsletterNewsletterTopic(newsletterId, 1, 0, "topic1"),
            new NewsletterNewsletterTopic(newsletterId, 2, 1, "topic2"));
        final List<NewsletterNewsletterTopic> all = new ArrayList<>(persisted);

        when(repoMock.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(persisted);
        when(repoMock.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(all);

        final List<NewsletterNewsletterTopic> topics = service.getSortedNewsletterTopicsForNewsletter(newsletterId);

        assertThat(topics).hasSize(2);
        assertThat(topics)
            .extracting("newsletterTopicId")
            .containsExactly(1, 2);
        assertThat(topics)
            .extracting("sort")
            .containsExactly(0, 1);
        assertThat(topics)
            .extracting("title")
            .containsExactly("topic1", "topic2");

        verify(repoMock).removeObsoleteNewsletterTopicsFromSort(newsletterId);
        verify(repoMock).findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId);
        verify(repoMock).findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId);
    }

    @Test
    void gettingSortedNewsletterTopicsForNewsletter_withPersistedAndUnpersistedTopics_returnsMergedLists() {
        final int newsletterId = 12;
        final List<NewsletterNewsletterTopic> persisted = List.of(
            new NewsletterNewsletterTopic(newsletterId, 1, 0, "topic1"),
            new NewsletterNewsletterTopic(newsletterId, 2, 1, "topic2"));
        final List<NewsletterNewsletterTopic> all = List.of(
            new NewsletterNewsletterTopic(newsletterId, 1, Integer.MAX_VALUE, "topic1"),
            new NewsletterNewsletterTopic(newsletterId, 2, Integer.MAX_VALUE, "topic2"),
            new NewsletterNewsletterTopic(newsletterId, 3, Integer.MAX_VALUE, "topic3"));

        when(repoMock.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(persisted);
        when(repoMock.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(all);

        final List<NewsletterNewsletterTopic> topics = service.getSortedNewsletterTopicsForNewsletter(newsletterId);

        assertThat(topics).hasSize(3);
        assertThat(topics)
            .extracting("newsletterTopicId")
            .containsExactly(1, 2, 3);
        assertThat(topics)
            .extracting("sort")
            .containsExactly(0, 1, 2);
        assertThat(topics)
            .extracting("title")
            .containsExactly("topic1", "topic2", "topic3");

        verify(repoMock).removeObsoleteNewsletterTopicsFromSort(newsletterId);
        verify(repoMock).findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId);
        verify(repoMock).findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId);
    }

    @Test
    void savingSortedNewsletterTopics_withTopics_delegatesSaveToRepo() {
        final int newsletterId = 1;
        final List<NewsletterNewsletterTopic> sortedTopics = List.of(
            new NewsletterNewsletterTopic(newsletterId, 1, 0, "topic1"),
            new NewsletterNewsletterTopic(newsletterId, 2, 1, "topic2"));
        service.saveSortedNewsletterTopics(newsletterId, sortedTopics);

        verify(repoMock).saveSortedNewsletterTopics(newsletterId, sortedTopics);
    }

    @Test
    void savingSortedNewsletterTopics_withNoTopics_skipsSaving() {
        service.saveSortedNewsletterTopics(1, Lists.emptyList());
    }

    @Test
    void savingSortedNewsletterTopics_withNullTopics_skipsSaving() {
        service.saveSortedNewsletterTopics(1, null);
    }
}