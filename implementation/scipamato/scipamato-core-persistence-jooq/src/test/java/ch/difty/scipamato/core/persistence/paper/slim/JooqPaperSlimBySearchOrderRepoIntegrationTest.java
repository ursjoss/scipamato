package ch.difty.scipamato.core.persistence.paper.slim;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.core.entity.SearchOrder;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.persistence.JooqTransactionalIntegrationTest;

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
