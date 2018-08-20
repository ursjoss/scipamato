package ch.difty.scipamato.core.persistence.newsletter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
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
    public void deletingNewsletterTopicDefinition_delegatesToRepo() {
        int id = 11;
        int version = 12;
        when(repoMock.delete(id, version)).thenReturn(persistedTopicDefinitionMock);
        assertThat(service.delete(id, version)).isEqualTo(persistedTopicDefinitionMock);
        verify(repoMock).delete(id, version);
    }
}