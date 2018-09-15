package ch.difty.scipamato.core.persistence.newsletter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.mockito.Mock;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter;
import ch.difty.scipamato.core.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.core.persistence.AbstractServiceTest;

public class JooqNewsletterServiceTest extends AbstractServiceTest<Integer, Newsletter, NewsletterRepository> {

    private JooqNewsletterService service;

    @Mock
    private NewsletterRepository repoMock;
    @Mock
    private NewsletterFilter     filterMock;

    @Mock
    private PaginationContext paginationContextMock;

    @Mock
    private Newsletter newsletterMock;

    @Mock
    private Newsletter newsletterWipMock;

    private final List<Newsletter> newsletters = new ArrayList<>();

    @Override
    protected NewsletterRepository getRepo() {
        return repoMock;
    }

    @Override
    protected Newsletter getEntity() {
        return newsletterMock;
    }

    @Override
    public void specificSetUp() {
        service = new JooqNewsletterService(repoMock, userRepoMock);

        newsletters.add(newsletterMock);
        newsletters.add(newsletterMock);

        when(newsletterMock.getCreatedBy()).thenReturn(10);
    }

    @Override
    public void specificTearDown() {
        verifyNoMoreInteractions(repoMock, filterMock, paginationContextMock, newsletterMock);
    }

    @Test
    public void findingById_withFoundEntity_returnsOptionalOfIt() {
        Integer id = 7;
        when(repoMock.findById(id)).thenReturn(newsletterMock);

        Optional<Newsletter> optNl = service.findById(id);
        assertThat(optNl.isPresent()).isTrue();
        assertThat(optNl.get()).isEqualTo(newsletterMock);

        verify(repoMock).findById(id);

        verifyAudit(1);
    }

    @Test
    public void findingById_withNotFoundEntity_returnsOptionalEmpty() {
        Integer id = 7;
        when(repoMock.findById(id)).thenReturn(null);

        assertThat(service
            .findById(id)
            .isPresent()).isFalse();

        verify(repoMock).findById(id);
    }

    @Test
    public void findingByFilter_delegatesToRepo() {
        when(repoMock.findPageByFilter(filterMock, paginationContextMock)).thenReturn(newsletters);
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isEqualTo(newsletters);
        verify(repoMock).findPageByFilter(filterMock, paginationContextMock);
        verifyAudit(2);
    }

    @Test
    public void countingByFilter_delegatesToRepo() {
        when(repoMock.countByFilter(filterMock)).thenReturn(3);
        assertThat(service.countByFilter(filterMock)).isEqualTo(3);
        verify(repoMock).countByFilter(filterMock);
    }

    @Test
    public void savingOrUpdating_withUnsavedEntityAndOtherNewsletterInStatusWIP_throws() {
        when(newsletterMock.getId()).thenReturn(null);
        when(newsletterMock.getPublicationStatus()).thenReturn(PublicationStatus.WIP);
        when(repoMock.getNewsletterInStatusWorkInProgress()).thenReturn(Optional.of(newsletterWipMock));
        when(newsletterWipMock.getId()).thenReturn(1);

        try {
            service.saveOrUpdate(newsletterMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("newsletter.onlyOneInStatusWipAllowed");
        }

        verify(newsletterMock).getPublicationStatus();
        verify(newsletterMock).getId();
        verify(repoMock).getNewsletterInStatusWorkInProgress();
        verify(newsletterWipMock).getId();
    }

    @Test
    public void savingOrUpdating_withSavedEntity_butOtherNewsletterInWipStatus_throws() {
        when(newsletterMock.getId()).thenReturn(2);
        when(newsletterMock.getPublicationStatus()).thenReturn(PublicationStatus.WIP);
        when(repoMock.getNewsletterInStatusWorkInProgress()).thenReturn(Optional.of(newsletterWipMock));
        when(newsletterWipMock.getId()).thenReturn(1);

        try {
            service.saveOrUpdate(newsletterMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("newsletter.onlyOneInStatusWipAllowed");
        }

        verify(newsletterMock).getPublicationStatus();
        verify(newsletterMock, times(2)).getId();
        verify(repoMock).getNewsletterInStatusWorkInProgress();
        verify(newsletterWipMock).getId();
    }

    @Test
    public void savingOrUpdating_withPaperWithNullId_hasRepoAddThePaper() {
        when(newsletterMock.getId()).thenReturn(null);
        when(newsletterMock.getPublicationStatus()).thenReturn(PublicationStatus.PUBLISHED);
        when(repoMock.add(newsletterMock)).thenReturn(newsletterMock);
        assertThat(service.saveOrUpdate(newsletterMock)).isEqualTo(newsletterMock);
        verify(repoMock).add(newsletterMock);
        verify(newsletterMock).getId();
        verify(newsletterMock).getPublicationStatus();
        verifyAudit(1);
    }

    @Test
    public void savingOrUpdating_withPaperWithNonNullId_hasRepoUpdateThePaper() {
        when(newsletterMock.getId()).thenReturn(17);
        when(newsletterMock.getPublicationStatus()).thenReturn(PublicationStatus.PUBLISHED);
        when(repoMock.update(newsletterMock)).thenReturn(newsletterMock);
        assertThat(service.saveOrUpdate(newsletterMock)).isEqualTo(newsletterMock);
        verify(repoMock).update(newsletterMock);
        verify(newsletterMock).getId();
        verify(newsletterMock).getPublicationStatus();
        verifyAudit(1);
    }

    @Test
    public void deleting_withNullEntity_doesNothing() {
        service.remove(null);
        verify(repoMock, never()).delete(anyInt(), anyInt());
    }

    @Test
    public void deleting_withEntityWithNullId_doesNothing() {
        when(newsletterMock.getId()).thenReturn(null);

        service.remove(newsletterMock);

        verify(newsletterMock).getId();
        verify(repoMock, never()).delete(anyInt(), anyInt());
    }

    @Test
    public void deleting_withEntityWithNormalId_delegatesToRepo() {
        when(newsletterMock.getId()).thenReturn(3);
        when(newsletterMock.getVersion()).thenReturn(17);

        service.remove(newsletterMock);

        verify(newsletterMock, times(2)).getId();
        verify(newsletterMock, times(1)).getVersion();
        verify(repoMock, times(1)).delete(3, 17);
    }

    @Test
    public void canCreateNewNewsletter_withNoWipNewsletters_isAllowed() {
        when(repoMock.getNewsletterInStatusWorkInProgress()).thenReturn(Optional.empty());
        assertThat(service.canCreateNewsletterInProgress()).isTrue();
        verify(repoMock).getNewsletterInStatusWorkInProgress();
    }

    @Test
    public void canCreateNewNewsletter_withOneWipNewsletters_isNotAllowed() {
        when(repoMock.getNewsletterInStatusWorkInProgress()).thenReturn(Optional.of(new Newsletter()));
        assertThat(service.canCreateNewsletterInProgress()).isFalse();
        verify(repoMock).getNewsletterInStatusWorkInProgress();
    }

    private NewsletterFilter fixtureWithNewNewsletterCount(final int count) {
        NewsletterFilter filter = new NewsletterFilter();
        filter.setPublicationStatus(PublicationStatus.WIP);
        when(repoMock.countByFilter(filter)).thenReturn(count);
        return filter;
    }

    @Test
    public void mergingPaperIntoNewsletter_withWipNewsletterPresent_canMerge() {
        final long paperId = 5;
        final Integer newsletterTopicId = 10;
        final int newsletterId = 1;
        final String langCode = "en";

        Newsletter wip = new Newsletter();
        wip.setId(newsletterId);

        Paper.NewsletterLink nl = mock(Paper.NewsletterLink.class);
        Optional<Paper.NewsletterLink> nlo = Optional.of(nl);

        when(repoMock.getNewsletterInStatusWorkInProgress()).thenReturn(Optional.of(wip));
        when(repoMock.mergePaperIntoNewsletter(newsletterId, paperId, newsletterTopicId, langCode)).thenReturn(nlo);

        service.mergePaperIntoWipNewsletter(paperId, newsletterTopicId);

        verify(repoMock).getNewsletterInStatusWorkInProgress();
        verify(repoMock).mergePaperIntoNewsletter(newsletterId, paperId, newsletterTopicId, langCode);
    }

    @Test
    public void mergingPaperIntoNewsletter_withNoWipNewsletterPresent_cannotMerge() {
        final long paperId = 5;
        final Integer newsletterTopicId = 10;

        when(repoMock.getNewsletterInStatusWorkInProgress()).thenReturn(Optional.empty());

        service.mergePaperIntoWipNewsletter(paperId, newsletterTopicId);

        verify(repoMock).getNewsletterInStatusWorkInProgress();
    }

    @Test
    public void removingPaperFromNewsletter_withWipNewsletterPresentAndRemoveSucceeding_canRemove() {
        final long paperId = 5;
        final int newsletterId = 1;
        Newsletter wip = new Newsletter();
        wip.setId(newsletterId);

        when(repoMock.getNewsletterInStatusWorkInProgress()).thenReturn(Optional.of(wip));
        when(repoMock.removePaperFromNewsletter(newsletterId, paperId)).thenReturn(1);

        assertThat(service.removePaperFromWipNewsletter(paperId)).isTrue();

        verify(repoMock).getNewsletterInStatusWorkInProgress();
        verify(repoMock).removePaperFromNewsletter(newsletterId, paperId);
    }

    @Test
    public void removingPaperFromNewsletter_withWipNewsletterPresentAndRemoveNotSucceeding_cannotRemove() {
        final long paperId = 5;
        final int newsletterId = 1;
        Newsletter wip = new Newsletter();
        wip.setId(newsletterId);

        when(repoMock.getNewsletterInStatusWorkInProgress()).thenReturn(Optional.of(wip));
        when(repoMock.removePaperFromNewsletter(newsletterId, paperId)).thenReturn(0);

        assertThat(service.removePaperFromWipNewsletter(paperId)).isFalse();

        verify(repoMock).getNewsletterInStatusWorkInProgress();
        verify(repoMock).removePaperFromNewsletter(newsletterId, paperId);
    }

    @Test
    public void removingPaperFromNewsletter_withNoWipNewsletterPresent_cannotRemove() {
        final long paperId = 5;

        when(repoMock.getNewsletterInStatusWorkInProgress()).thenReturn(Optional.empty());

        assertThat(service.removePaperFromWipNewsletter(paperId)).isFalse();

        verify(repoMock).getNewsletterInStatusWorkInProgress();
    }

}