package ch.difty.scipamato.core.persistence.paper.slim;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.JooqBaseIntegrationTest;

class JooqPaperSlimBySearchOrderRepoIntegrationTest extends JooqBaseIntegrationTest {

    @Autowired
    private JooqPaperSlimBySearchOrderRepo repo;

    @Test
    void finding() {
        SearchOrder so = new SearchOrder();
        so.setGlobal(true);

        List<PaperSlim> papers = repo.findBySearchOrder(so);
        assertThat(papers).isNotNull();
    }

    @Test
    void findingPaged_withNonMatchingCondition_findsNoRecords() {
        SearchOrder so = new SearchOrder();
        so.setGlobal(true);

        PaginationContext pc = new PaginationRequest(0, 10);
        List<PaperSlim> papers = repo.findPageBySearchOrder(so, pc);

        assertThat(papers).isEmpty();
    }

    @Test
    void findingPaged_withMatchingSearchCondition() {
        SearchOrder so = new SearchOrder();
        SearchCondition sc = new SearchCondition();
        sc.setAuthors("Turner");
        so.add(sc);

        PaginationContext pc = new PaginationRequest(0, 10);
        List<PaperSlim> papers = repo.findPageBySearchOrder(so, pc);

        assertThat(papers).isNotEmpty();
    }

    @Test
    void counting() {
        SearchOrder so = new SearchOrder();
        so.setGlobal(true);

        int count = repo.countBySearchOrder(so);
        assertThat(count).isGreaterThanOrEqualTo(0);
    }

}
