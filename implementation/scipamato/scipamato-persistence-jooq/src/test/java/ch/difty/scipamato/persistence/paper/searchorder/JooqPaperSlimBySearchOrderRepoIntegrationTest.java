package ch.difty.scipamato.persistence.paper.searchorder;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.entity.projection.PaperSlim;
import ch.difty.scipamato.persistence.JooqTransactionalIntegrationTest;
import ch.difty.scipamato.persistence.paging.PaginationContext;
import ch.difty.scipamato.persistence.paging.PaginationRequest;

public class JooqPaperSlimBySearchOrderRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    @Autowired
    private JooqPaperSlimBySearchOrderRepo repo;

    @Test
    public void finding() {
        SearchOrder so = new SearchOrder();
        so.setGlobal(true);

        List<PaperSlim> papers = repo.findBySearchOrder(so);
        assertThat(papers).isNotNull();
    }

    @Test
    public void findingPaged() {
        SearchOrder so = new SearchOrder();
        so.setGlobal(true);

        PaginationContext pc = new PaginationRequest(0, 10);
        List<PaperSlim> papers = repo.findPageBySearchOrder(so, pc);

        assertThat(papers).isNotNull();
    }

    @Test
    public void counting() {
        SearchOrder so = new SearchOrder();
        so.setGlobal(true);

        int count = repo.countBySearchOrder(so);
        assertThat(count).isGreaterThanOrEqualTo(0);
    }

}
