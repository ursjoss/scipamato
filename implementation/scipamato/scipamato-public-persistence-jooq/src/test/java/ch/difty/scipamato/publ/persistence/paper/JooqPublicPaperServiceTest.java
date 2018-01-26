package ch.difty.scipamato.publ.persistence.paper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.publ.entity.PublicPaper;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.publ.persistence.paper.JooqPublicPaperService;
import ch.difty.scipamato.publ.persistence.paper.PublicPaperRepository;

@RunWith(MockitoJUnitRunner.class)
public class JooqPublicPaperServiceTest {

    private static final long ID     = 5;
    private static final long NUMBER = 15;

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
        service = new JooqPublicPaperService(mockRepo);

        publicPaper = PublicPaper.builder()
            .id(ID)
            .build();

        papers.add(publicPaper);
        papers.add(PublicPaper.builder()
            .id(ID + 1)
            .build());
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(mockRepo);
    }

    @Test
    public void findingByNumber_withRepoFindingRecord_returnsItWrappedAsOptional() {
        when(mockRepo.findByNumber(NUMBER)).thenReturn(publicPaper);

        Optional<PublicPaper> paperOp = service.findByNumber(NUMBER);

        assertThat(paperOp).isPresent();
        assertThat(paperOp).hasValue(publicPaper);
        verify(mockRepo).findByNumber(NUMBER);
    }

    @Test
    public void findingByNumber_withRepoNotFindingRecord_returnsEmptyOptional() {
        when(mockRepo.findByNumber(NUMBER)).thenReturn(null);

        Optional<PublicPaper> paperOp = service.findByNumber(NUMBER);

        assertThat(paperOp).isNotPresent();
        verify(mockRepo).findByNumber(NUMBER);
    }

    @Test
    public void findingPageByFilter_delegatesToRepo() {
        when(mockRepo.findPageByFilter(filterMock, paginationContextMock)).thenReturn(papers);
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isNotNull()
            .isNotEmpty()
            .hasSize(2);
        verify(mockRepo).findPageByFilter(filterMock, paginationContextMock);
    }

    @Test
    public void countingByFilter_delegatesToRepo() {
        when(mockRepo.countByFilter(filterMock)).thenReturn(2);
        assertThat(service.countByFilter(filterMock)).isEqualTo(2);
        verify(mockRepo).countByFilter(filterMock);
    }

    @Test
    public void findingPageOfIdsByFilter_delegatesToRepo() {
        List<Long> idList = Arrays.asList(3l, 5l);
        when(mockRepo.findPageOfNumbersByFilter(filterMock, paginationContextMock)).thenReturn(idList);
        assertThat(service.findPageOfNumbersByFilter(filterMock, paginationContextMock)).isEqualTo(idList);
        verify(mockRepo).findPageOfNumbersByFilter(filterMock, paginationContextMock);
    }
}
