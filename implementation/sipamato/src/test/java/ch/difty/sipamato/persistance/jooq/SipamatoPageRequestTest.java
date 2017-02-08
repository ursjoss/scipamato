package ch.difty.sipamato.persistance.jooq;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class SipamatoPageRequestTest {

    private SipamatoPageRequest pr;
    private String sort;

    @Test(expected = IllegalArgumentException.class)
    public void degenerateConstruction_withInvalidOffset() {
        new SipamatoPageRequest(-1, 1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void degenerateConstruction_withInvalidPageSize() {
        new SipamatoPageRequest(0, 0, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void degenerateConstruction_withInvalidRecordCountVsPageSize() {
        new SipamatoPageRequest(0, 10, 11);
    }

    private void assertPageRequest(Pageable p, int pageNumber, int offSet, int pageSize, String fooSort) {
        assertThat(p.getPageNumber()).isEqualTo(pageNumber);
        assertThat(p.getOffset()).isEqualTo(offSet);
        assertThat(p.getPageSize()).isEqualTo(pageSize);
        if (fooSort != null) {
            final Sort sort = p.getSort();
            assertThat(sort.getOrderFor("foo").toString()).isEqualTo(fooSort);
            assertThat(sort.getOrderFor("bar")).isNull();
        } else {
            assertThat(p.getSort()).isNull();
        }
    }

    @Test
    public void pageRequestWithSortButNoPaging() {
        sort = "foo: DESC";
        pr = new SipamatoPageRequest(Direction.DESC, "foo");

        assertPageRequest(pr, 0, 0, Integer.MAX_VALUE, sort);
        assertThat(pr.toString()).isEqualTo("Page request [offset: 0, size 2147483647, records 2147483647, sort: foo: DESC]");

        assertThat(pr.hasPrevious()).isFalse();
        assertPageRequest(pr.first(), 0, 0, Integer.MAX_VALUE, sort);
        assertPageRequest(pr.previous(), 0, 0, Integer.MAX_VALUE, sort);
        assertPageRequest(pr.previousOrFirst(), 0, 0, Integer.MAX_VALUE, sort);
        assertPageRequest(pr.next(), 1, Integer.MAX_VALUE, Integer.MAX_VALUE, sort);
    }

    @Test
    public void pageRequestWithSort_withOffset0_andPageSize10_andRecordCount10() {
        sort = "foo: DESC";
        pr = new SipamatoPageRequest(0, 10, 10, Direction.DESC, "foo");

        assertPageRequest(pr, 0, 0, 10, sort);
        assertThat(pr.toString()).isEqualTo("Page request [offset: 0, size 10, records 10, sort: foo: DESC]");

        assertThat(pr.hasPrevious()).isFalse();
        assertPageRequest(pr.first(), 0, 0, 10, sort);
        assertPageRequest(pr.previous(), 0, 0, 10, sort);
        assertPageRequest(pr.previousOrFirst(), 0, 0, 10, sort);
        assertPageRequest(pr.next(), 1, 10, 10, sort);
        assertPageRequest(pr.next().next(), 2, 20, 10, sort);
    }

    @Test
    public void pageRequestWithSort_withOffset24_andPageSize12() {
        sort = "foo: ASC";
        pr = new SipamatoPageRequest(24, 12, 12, Direction.ASC, "foo");

        assertPageRequest(pr, 2, 24, 12, sort);
        assertThat(pr.toString()).isEqualTo("Page request [offset: 24, size 12, records 12, sort: foo: ASC]");

        assertThat(pr.hasPrevious()).isTrue();
        assertPageRequest(pr.first(), 0, 0, 12, sort);
        assertPageRequest(pr.previous(), 1, 12, 12, sort);
        assertPageRequest(pr.previousOrFirst(), 1, 12, 12, sort);
        assertPageRequest(pr.previous().previousOrFirst(), 0, 0, 12, sort);
        assertPageRequest(pr.next(), 3, 36, 12, sort);
        assertPageRequest(pr.next().next(), 4, 48, 12, sort);
    }

    @Test
    public void pageRequestWithoutSort_withOffset6_andPageSize2() {
        sort = null;
        pr = new SipamatoPageRequest(6, 2, 2);

        assertPageRequest(pr, 3, 6, 2, sort);
        assertThat(pr.toString()).isEqualTo("Page request [offset: 6, size 2, records 2, sort: null]");

        assertThat(pr.hasPrevious()).isTrue();
        assertPageRequest(pr.first(), 0, 0, 2, sort);
        assertPageRequest(pr.previous(), 2, 4, 2, sort);
        assertPageRequest(pr.previousOrFirst(), 2, 4, 2, sort);
        assertPageRequest(pr.previous().previousOrFirst(), 1, 2, 2, sort);
        assertPageRequest(pr.next(), 4, 8, 2, sort);
        assertPageRequest(pr.next().next(), 5, 10, 2, sort);
    }

    @Test
    public void equality_ofSameObjectInstance() {
        pr = new SipamatoPageRequest(5, 5, 5);
        assertEquality(pr, pr);
    }

    private void assertEquality(SipamatoPageRequest pr1, SipamatoPageRequest pr2) {
        assertThat(pr1.equals(pr2)).isTrue();
        assertThat(pr1.hashCode()).isEqualTo(pr2.hashCode());
    }

    @Test
    public void inequality_ofNull() {
        pr = new SipamatoPageRequest(5, 5, 5);
        assertThat(pr.equals(null)).isFalse();
    }

    @Test
    public void inequality_ofDifferentClass() {
        pr = new SipamatoPageRequest(5, 5, 5);
        assertThat("foo".equals(pr)).isFalse();
    }

    @Test
    public void inequality_ofPageRequestsWithDifferentSorts() {
        pr = new SipamatoPageRequest(5, 5, 5);
        assertInequality(pr, new SipamatoPageRequest(5, 5, 5, Direction.ASC, "foo"));
    }

    private void assertInequality(SipamatoPageRequest pr1, SipamatoPageRequest pr2) {
        assertThat(pr1.equals(pr2)).isFalse();
        assertThat(pr1.hashCode()).isNotEqualTo(pr2.hashCode());
    }

    @Test
    public void inequality_ofPageRequestsWithDifferentSorts2() {
        pr = new SipamatoPageRequest(5, 6, 6, Direction.ASC, "bar");
        assertInequality(pr, new SipamatoPageRequest(5, 6, 6, Direction.ASC, "foo"));
    }

    @Test
    public void inequality_ofPageRequestsWithDifferentSorts3() {
        pr = new SipamatoPageRequest(5, 6, 6, Direction.DESC, "foo");
        assertInequality(pr, new SipamatoPageRequest(5, 6, 6, Direction.ASC, "foo"));
    }

    @Test
    public void inequality_ofPageRequestsWithNonSortAttributes1() {
        pr = new SipamatoPageRequest(5, 6, 6, Direction.ASC, "foo");
        assertInequality(pr, new SipamatoPageRequest(6, 6, 6, Direction.ASC, "foo"));
    }

    @Test
    public void inequality_ofPageRequestsWithNonSortAttributes2() {
        pr = new SipamatoPageRequest(5, 6, 6, Direction.ASC, "foo");
        assertInequality(pr, new SipamatoPageRequest(5, 7, 6, Direction.ASC, "foo"));
    }

    @Test
    public void inequality_ofPageRequestsWithNonSortAttributes3() {
        pr = new SipamatoPageRequest(5, 6, 6, Direction.ASC, "foo");
        assertInequality(pr, new SipamatoPageRequest(5, 6, 5, Direction.ASC, "foo"));
    }

    @Test
    public void equality_ofPageRequestsWithSameAttributes_withSort() {
        pr = new SipamatoPageRequest(5, 6, 6, Direction.ASC, "foo");
        assertEquality(pr, new SipamatoPageRequest(5, 6, 6, Direction.ASC, "foo"));
    }

    @Test
    public void equality_ofPageRequestsWithSameAttributes_withoutSort() {
        pr = new SipamatoPageRequest(5, 6, 6);
        SipamatoPageRequest pr2 = new SipamatoPageRequest(5, 6, 6);
        assertEquality(pr, pr2);
    }

}
