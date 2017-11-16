package ch.difty.scipamato.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.scipamato.entity.PublicPaper;
import ch.difty.scipamato.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.persistence.paging.PaginationContext;

@RunWith(MockitoJUnitRunner.class)
public class JooqPublicPaperServiceTest {

    private static final long ID = 5;

    private JooqPublicPaperService service;

    private PublicPaper publicPaper;

    @Mock
    private PublicPaperRepository mockRepo;

    @Mock
    private PublicPaperFilter filterMock;

    @Mock
    private PaginationContext paginationContextMock;

    private final List<PublicPaper> papers = new ArrayList<>();

    @Before
    public void setUp() {
        service = new JooqPublicPaperService();
        service.setRepository(mockRepo);

        publicPaper = PublicPaper.builder().id(ID).build();

        papers.add(publicPaper);
        papers.add(PublicPaper.builder().id(ID + 1).build());
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(mockRepo);
    }

    @Test
    public void findingById_withRepoFindingRecord_returnsItWrappedAsOptional() {
        when(mockRepo.findById(ID)).thenReturn(publicPaper);

        Optional<PublicPaper> paperOp = service.findById(ID);

        assertThat(paperOp).isPresent();
        assertThat(paperOp).hasValue(publicPaper);
        verify(mockRepo).findById(ID);
    }

    @Test
    public void findingById_withRepoNotFindingRecord_returnsEmptyOptional() {
        when(mockRepo.findById(ID)).thenReturn(null);

        Optional<PublicPaper> paperOp = service.findById(ID);

        assertThat(paperOp).isNotPresent();
        verify(mockRepo).findById(ID);
    }

    @Test
    public void findingPageByFilter_delegatesToRepo() {
        when(mockRepo.findPageByFilter(filterMock, paginationContextMock)).thenReturn(papers);
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isNotNull().isNotEmpty().hasSize(2);
        verify(mockRepo).findPageByFilter(filterMock, paginationContextMock);
    }

    @Test
    public void countingByFilter_delegatesToRepo() {
        when(mockRepo.countByFilter(filterMock)).thenReturn(2);
        assertThat(service.countByFilter(filterMock)).isEqualTo(2);
        verify(mockRepo).countByFilter(filterMock);
    }
}
