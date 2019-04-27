package ch.difty.scipamato.publ.persistence.paper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.publ.entity.PublicPaper;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;

@ExtendWith(MockitoExtension.class)
class JooqPublicPaperServiceTest {

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

    @BeforeEach
    void setUp() {
        service = new JooqPublicPaperService(mockRepo);

        publicPaper = PublicPaper
            .builder()
            .id(ID)
            .build();

        papers.add(publicPaper);
        papers.add(PublicPaper
            .builder()
            .id(ID + 1)
            .build());
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(mockRepo);
    }

    @Test
    void findingByNumber_withRepoFindingRecord_returnsItWrappedAsOptional() {
        when(mockRepo.findByNumber(NUMBER)).thenReturn(publicPaper);

        Optional<PublicPaper> paperOp = service.findByNumber(NUMBER);

        assertThat(paperOp).isPresent();
        assertThat(paperOp).hasValue(publicPaper);
        verify(mockRepo).findByNumber(NUMBER);
    }

    @Test
    void findingByNumber_withRepoNotFindingRecord_returnsEmptyOptional() {
        when(mockRepo.findByNumber(NUMBER)).thenReturn(null);

        Optional<PublicPaper> paperOp = service.findByNumber(NUMBER);

        assertThat(paperOp).isNotPresent();
        verify(mockRepo).findByNumber(NUMBER);
    }

    @Test
    void findingPageByFilter_delegatesToRepo() {
        when(mockRepo.findPageByFilter(filterMock, paginationContextMock)).thenReturn(papers);
        assertThat(service.findPageByFilter(filterMock, paginationContextMock))
            .isNotNull()
            .isNotEmpty()
            .hasSize(2);
        verify(mockRepo).findPageByFilter(filterMock, paginationContextMock);
    }

    @Test
    void countingByFilter_delegatesToRepo() {
        when(mockRepo.countByFilter(filterMock)).thenReturn(2);
        assertThat(service.countByFilter(filterMock)).isEqualTo(2);
        verify(mockRepo).countByFilter(filterMock);
    }

    @Test
    void findingPageOfIdsByFilter_delegatesToRepo() {
        List<Long> idList = Arrays.asList(3L, 5L);
        when(mockRepo.findPageOfNumbersByFilter(filterMock, paginationContextMock)).thenReturn(idList);
        assertThat(service.findPageOfNumbersByFilter(filterMock, paginationContextMock)).isEqualTo(idList);
        verify(mockRepo).findPageOfNumbersByFilter(filterMock, paginationContextMock);
    }
}
