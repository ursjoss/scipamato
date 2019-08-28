package ch.difty.scipamato.core.persistence.paper.slim;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;

@JooqTest
@Testcontainers
class JooqPaperSlimBySearchOrderRepoIntegrationTest {

    private SearchOrder       so = new SearchOrder();
    private SearchCondition   sc = new SearchCondition();
    private PaginationContext pc = new PaginationRequest(0, 10);

    @Autowired
    private JooqPaperSlimBySearchOrderRepo repo;

    @Test
    void finding() {
        so.setGlobal(true);
        assertThat(repo.findBySearchOrder(so)).isNotNull();
    }

    @Test
    void findingPaged_withNonMatchingCondition_findsNoRecords() {
        so.setGlobal(true);
        assertThat(repo.findPageBySearchOrder(so, pc)).isEmpty();
    }

    @Test
    void findingPaged_withMatchingSearchCondition() {
        sc.setAuthors("Turner");
        so.add(sc);
        assertThat(repo.findPageBySearchOrder(so, pc)).isNotEmpty();
    }

    @Test
    void counting() {
        so.setGlobal(true);
        assertThat(repo.countBySearchOrder(so)).isGreaterThanOrEqualTo(0);
    }

    @Test
    void findingPaged_withSearchConditionUsingIdRange() {
        sc.setId("10-15");
        so.add(sc);
        assertThat(repo.findPageBySearchOrder(so, pc)).hasSize(6);
    }

    @Test
    void findingPaged_withSearchConditionUsingIdLessThan() {
        sc.setId("<11");
        so.add(sc);
        assertThat(repo.findPageBySearchOrder(so, pc)).hasSize(5);
    }

    @Test
    void findingPaged_withSearchConditionUsingNumberGreaterThan() {
        sc.setNumber("23");
        so.add(sc);
        assertThat(repo.findPageBySearchOrder(so, pc)).hasSize(1);
    }

    @Test
    void findingPaged_withSearchConditionUsingPublicationYearRange() {
        sc.setPublicationYear("<2015");
        so.add(sc);
        assertThat(repo.findPageBySearchOrder(so, pc)).hasSize(6);
    }

    @Test
    void findingPaged_withSearchConditionUsingMethods_withPositiveConditionOnly() {
        sc.setMethods("querschnitt");
        so.add(sc);
        assertThat(repo.findPageBySearchOrder(so, pc))
            .extracting(Paper.PaperFields.NUMBER.getName())
            .containsExactly(15L, 18L, 25L, 29L);
    }

    @Test
    void findingPaged_withSearchConditionUsingMethods_withPositiveAndNegativeCondition() {
        sc.setMethods("querschnitt -regression");
        so.add(sc);
        assertThat(repo.findPageBySearchOrder(so, pc))
            .extracting(Paper.PaperFields.NUMBER.getName())
            .containsExactly(15L);
    }

    @Test
    void findingPaged_withSearchConditionUsingMethods_withPositiveAndNegativeCondition2() {
        sc.setMethods("querschnitt -schweiz");
        so.add(sc);
        assertThat(repo.findPageBySearchOrder(so, pc))
            .extracting(Paper.PaperFields.NUMBER.getName())
            .containsExactly(15L, 18L, 25L);
    }

}
