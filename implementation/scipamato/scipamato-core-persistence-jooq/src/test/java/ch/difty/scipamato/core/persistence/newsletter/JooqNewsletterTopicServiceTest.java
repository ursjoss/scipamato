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
    private NewsletterTopicDefinition topicDefinitionMock, persistendTopicDefinitionMock;

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

    //    @Test
    //    public void findingById_withFoundEntity_returnsOptionalOfIt() {
    //        Integer id = 7;
    //        when(repoMock.findById(id)).thenReturn(topicMock);
    //
    //        Optional<NewsletterTopic> optNl = service.findById(id);
    //        assertThat(optNl.isPresent()).isTrue();
    //        assertThat(optNl.get()).isEqualTo(topicMock);
    //
    //        verify(repoMock).findById(id);
    //
    //        verifyAudit(1);
    //    }
    //
    //    @Test
    //    public void findingById_withNotFoundEntity_returnsOptionalEmpty() {
    //        Integer id = 7;
    //        when(repoMock.findById(id)).thenReturn(null);
    //
    //        assertThat(service
    //            .findById(id)
    //            .isPresent()).isFalse();
    //
    //        verify(repoMock).findById(id);
    //    }
    //
    //    @Test
    //    public void findingByFilter_delegatesToRepo() {
    //        when(repoMock.findPageByFilter(filterMock, paginationContextMock)).thenReturn(topics);
    //        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isEqualTo(topics);
    //        verify(repoMock).findPageByFilter(filterMock, paginationContextMock);
    //        verifyAudit(2);
    //    }
    //
    //    @Test
    //    public void countingByFilter_delegatesToRepo() {
    //        when(repoMock.countByFilter(filterMock)).thenReturn(3);
    //        assertThat(service.countByFilter(filterMock)).isEqualTo(3);
    //        verify(repoMock).countByFilter(filterMock);
    //    }
    //
    //    @Test
    //    public void savingOrUpdating_withPaperWithNullId_hasRepoAddThePaper() {
    //        when(topicMock.getId()).thenReturn(null);
    //        when(repoMock.add(topicMock)).thenReturn(topicMock);
    //        assertThat(service.saveOrUpdate(topicMock)).isEqualTo(topicMock);
    //        verify(repoMock).add(topicMock);
    //        verify(topicMock).getId();
    //        verifyAudit(1);
    //    }
    //
    //    @Test
    //    public void savingOrUpdating_withPaperWithNonNullId_hasRepoUpdateThePaper() {
    //        when(topicMock.getId()).thenReturn(17);
    //        when(repoMock.update(topicMock)).thenReturn(topicMock);
    //        assertThat(service.saveOrUpdate(topicMock)).isEqualTo(topicMock);
    //        verify(repoMock).update(topicMock);
    //        verify(topicMock).getId();
    //        verifyAudit(1);
    //    }
    //
    //    @Test
    //    public void deleting_withNullEntity_doesNothing() {
    //        service.remove(null);
    //        verify(repoMock, never()).delete(anyInt(), anyInt());
    //    }
    //
    //    @Test
    //    public void deleting_withEntityWithNullId_doesNothing() {
    //        when(topicMock.getId()).thenReturn(null);
    //
    //        service.remove(topicMock);
    //
    //        verify(topicMock).getId();
    //        verify(repoMock, never()).delete(anyInt(), anyInt());
    //    }
    //
    //    @Test
    //    public void deleting_withEntityWithNormalId_delegatesToRepo() {
    //        when(topicMock.getId()).thenReturn(3);
    //        when(topicMock.getVersion()).thenReturn(17);
    //
    //        service.remove(topicMock);
    //
    //        verify(topicMock, times(2)).getId();
    //        verify(topicMock, times(1)).getVersion();
    //        verify(repoMock, times(1)).delete(3, 17);
    //    }

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
    public void addingNewsletterTopicDefinition_delegatesToRepo() {
        when(repoMock.add(topicDefinitionMock)).thenReturn(persistendTopicDefinitionMock);
        assertThat(service.add(topicDefinitionMock)).isEqualTo(persistendTopicDefinitionMock);
        verify(repoMock).add(topicDefinitionMock);
    }

    @Test
    public void updatingNewsletterTopicDefinition_delegatesToRepo() {
        when(repoMock.update(topicDefinitionMock)).thenReturn(persistendTopicDefinitionMock);
        assertThat(service.update(topicDefinitionMock)).isEqualTo(persistendTopicDefinitionMock);
        verify(repoMock).update(topicDefinitionMock);
    }
}