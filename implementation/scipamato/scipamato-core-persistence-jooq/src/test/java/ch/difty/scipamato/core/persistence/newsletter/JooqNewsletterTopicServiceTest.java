package ch.difty.scipamato.core.persistence.newsletter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.common.TestUtils;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.persistence.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class JooqNewsletterTopicServiceTest {

    private JooqNewsletterTopicService service;

    @Mock
    private NewsletterTopicRepository repoMock;
    @Mock
    private UserRepository            userRepoMock;
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

    @Before
    public void setUp() {
        service = new JooqNewsletterTopicService(repoMock, userRepoMock);

        topics.add(topicMock);
        topics.add(topicMock);

        topicDefinitions.add(topicDefinitionMock);
        topicDefinitions.add(topicDefinitionMock);

        //        when(topicMock.getCreatedBy()).thenReturn(10);
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
    public void newUnpersistedNewsletterTopicDefinition_delegatesToRepo() {
        when(repoMock.newUnpersistedNewsletterTopicDefinition()).thenReturn(topicDefinitionMock);
        assertThat(service.newUnpersistedNewsletterTopicDefinition()).isEqualTo(topicDefinitionMock);
        verify(repoMock).newUnpersistedNewsletterTopicDefinition();
    }

    @Test
    public void findingPageOfNewsletterTopicDefinitions_delegatesToRepo() {
        when(repoMock.findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock)).thenReturn(
            topicDefinitions);
        assertThat(service.findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock)).isEqualTo(
            topicDefinitions);
        verify(repoMock).findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock);
    }

    @Test
    public void countingNewsletterTopics_delegatesToRepo() {
        when(repoMock.countByFilter(filterMock)).thenReturn(3);
        assertThat(service.countByFilter(filterMock)).isEqualTo(3);
        verify(repoMock).countByFilter(filterMock);
    }

    @Test
    public void insertingNewsletterTopicDefinition_delegatesToRepo() {
        when(repoMock.insert(topicDefinitionMock)).thenReturn(persistedTopicDefinitionMock);
        assertThat(service.insert(topicDefinitionMock)).isEqualTo(persistedTopicDefinitionMock);
        verify(repoMock).insert(topicDefinitionMock);
    }

    @Test
    public void updatingNewsletterTopicDefinition_delegatesToRepo() {
        when(repoMock.update(topicDefinitionMock)).thenReturn(persistedTopicDefinitionMock);
        assertThat(service.update(topicDefinitionMock)).isEqualTo(persistedTopicDefinitionMock);
        verify(repoMock).update(topicDefinitionMock);
    }

    @Test
    public void savingOrUpdatingNewsletterTopicDefinition_withNullEntity_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> service.saveOrUpdate(null), "entity");
    }

    @Test
    public void savingOrUpdatingNewsletterTopicDefinition_withNullId_callsInsert() {
        when(topicDefinitionMock.getId()).thenReturn(null);
        when(repoMock.insert(topicDefinitionMock)).thenReturn(persistedTopicDefinitionMock);
        assertThat(service.saveOrUpdate(topicDefinitionMock)).isEqualTo(persistedTopicDefinitionMock);
        verify(topicDefinitionMock).getId();
        verify(repoMock).insert(topicDefinitionMock);
    }

    @Test
    public void savingOrUpdatingNewsletterTopicDefinition_withNonNullId_callsUpdate() {
        when(topicDefinitionMock.getId()).thenReturn(1);
        when(repoMock.update(topicDefinitionMock)).thenReturn(persistedTopicDefinitionMock);
        assertThat(service.saveOrUpdate(topicDefinitionMock)).isEqualTo(persistedTopicDefinitionMock);
        verify(topicDefinitionMock).getId();
        verify(repoMock).update(topicDefinitionMock);
    }

    @Test
    public void deletingNewsletterTopicDefinition_delegatesToRepo() {
        int id = 11;
        int version = 12;
        when(repoMock.delete(id, version)).thenReturn(persistedTopicDefinitionMock);
        assertThat(service.delete(id, version)).isEqualTo(persistedTopicDefinitionMock);
        verify(repoMock).delete(id, version);
    }

    @Test
    public void gettingSortedNewsletterTopicsForNewsletter_withNoAssignedTopics_isEmpty() {
        final int newsletterId = 12;

        when(repoMock.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(
            Lists.emptyList());
        when(repoMock.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(Lists.emptyList());

        assertThat(service.getSortedNewsletterTopicsForNewsletter(newsletterId)).isEmpty();

        verify(repoMock).findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId);
        verify(repoMock).findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId);
    }

    @Test
    public void gettingSortedNewsletterTopicsForNewsletter_withNoPersistedTopics_resortsAllUnpersisted() {
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

        verify(repoMock).findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId);
        verify(repoMock).findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId);
    }

    @Test
    public void gettingSortedNewsletterTopicsForNewsletter_withAllPersistedTopics_returnsThoseOnly() {
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

        verify(repoMock).findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId);
        verify(repoMock).findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId);
    }

    @Test
    public void gettingSortedNewsletterTopicsForNewsletter_withPersistedAndUnpersistedTopics_returnsMergedLists() {
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

        verify(repoMock).findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId);
        verify(repoMock).findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId);
    }

    @Test
    public void savingSortedNewsletterTopics_withTopics_delegatesSaveToRepo() {
        final int newsletterId = 1;
        final List<NewsletterNewsletterTopic> sortedTopics = List.of(
            new NewsletterNewsletterTopic(newsletterId, 1, 0, "topic1"),
            new NewsletterNewsletterTopic(newsletterId, 2, 1, "topic2"));
        service.saveSortedNewsletterTopics(newsletterId, sortedTopics);

        verify(repoMock).saveSortedNewsletterTopics(newsletterId, sortedTopics);
    }

    @Test
    public void savingSortedNewsletterTopics_withNoTopics_skipsSaving() {
        service.saveSortedNewsletterTopics(1, Lists.emptyList());
    }

    @Test
    public void savingSortedNewsletterTopics_withNullTopics_skipsSaving() {
        service.saveSortedNewsletterTopics(1, null);
    }
}