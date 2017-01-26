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
    public void degenerateConstruction_withInvalidPageNumber() {
        new SipamatoPageRequest(-1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void degenerateConstruction_withInvalidSize() {
        new SipamatoPageRequest(0, 0);
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
    public void pageRequestWithSort_withPage0_andPageSize10() {
        sort = "foo: DESC";
        pr = new SipamatoPageRequest(0, 10, Direction.DESC, "foo");

        assertPageRequest(pr, 0, 0, 10, sort);
        assertThat(pr.toString()).isEqualTo("Page request [number: 0, size 10, sort: foo: DESC]");

        assertThat(pr.hasPrevious()).isFalse();
        assertPageRequest(pr.first(), 0, 0, 10, sort);
        assertPageRequest(pr.previous(), 0, 0, 10, sort);
        assertPageRequest(pr.previousOrFirst(), 0, 0, 10, sort);
        assertPageRequest(pr.next(), 1, 10, 10, sort);
        assertPageRequest(pr.next().next(), 2, 20, 10, sort);
    }

    @Test
    public void pageRequestWithSort_withPage2_andPageSize12() {
        sort = "foo: ASC";
        pr = new SipamatoPageRequest(2, 12, Direction.ASC, "foo");

        assertPageRequest(pr, 2, 24, 12, sort);
        assertThat(pr.toString()).isEqualTo("Page request [number: 2, size 12, sort: foo: ASC]");

        assertThat(pr.hasPrevious()).isTrue();
        assertPageRequest(pr.first(), 0, 0, 12, sort);
        assertPageRequest(pr.previous(), 1, 12, 12, sort);
        assertPageRequest(pr.previousOrFirst(), 1, 12, 12, sort);
        assertPageRequest(pr.previous().previousOrFirst(), 0, 0, 12, sort);
        assertPageRequest(pr.next(), 3, 36, 12, sort);
        assertPageRequest(pr.next().next(), 4, 48, 12, sort);
    }

    @Test
    public void pageRequestWithoutSort_withPage3_andPageSize2() {
        sort = null;
        pr = new SipamatoPageRequest(3, 2);

        assertPageRequest(pr, 3, 6, 2, sort);
        assertThat(pr.toString()).isEqualTo("Page request [number: 3, size 2, sort: null]");

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
        pr = new SipamatoPageRequest(5, 6);
        assertThat(pr.equals(pr)).isTrue();
    }

    @Test
    public void inequality_ofDifferentClass() {
        pr = new SipamatoPageRequest(5, 6);
        assertThat(pr.equals("foo")).isFalse();
    }

    @Test
    public void inequality_ofPageRequestsWithDifferentSorts() {
        pr = new SipamatoPageRequest(5, 6);
        assertThat(pr.equals(new SipamatoPageRequest(5, 6, Direction.ASC, "foo"))).isFalse();
    }

    @Test
    public void inequality_ofPageRequestsWithDifferentSorts2() {
        pr = new SipamatoPageRequest(5, 6, Direction.ASC, "bar");
        assertThat(pr.equals(new SipamatoPageRequest(5, 6, Direction.ASC, "foo"))).isFalse();
    }

    @Test
    public void inequality_ofPageRequestsWithDifferentSorts3() {
        pr = new SipamatoPageRequest(5, 6, Direction.DESC, "foo");
        assertThat(pr.equals(new SipamatoPageRequest(5, 6, Direction.ASC, "foo"))).isFalse();
    }

    @Test
    public void inequality_ofPageRequestsWithNonSortAttributes1() {
        pr = new SipamatoPageRequest(5, 6, Direction.ASC, "foo");
        assertThat(pr.equals(new SipamatoPageRequest(6, 6, Direction.ASC, "foo"))).isFalse();
    }

    @Test
    public void inequality_ofPageRequestsWithNonSortAttributes2() {
        pr = new SipamatoPageRequest(5, 6, Direction.ASC, "foo");
        assertThat(pr.equals(new SipamatoPageRequest(5, 7, Direction.ASC, "foo"))).isFalse();
    }

    @Test
    public void equality_ofPageRequestsWithSameAttributes_withSort() {
        pr = new SipamatoPageRequest(5, 6, Direction.ASC, "foo");
        assertThat(pr.equals(new SipamatoPageRequest(5, 6, Direction.ASC, "foo"))).isTrue();
    }

    @Test
    public void equality_ofPageRequestsWithSameAttributes_withOutSort() {
        pr = new SipamatoPageRequest(5, 6);
        assertThat(pr.equals(new SipamatoPageRequest(5, 6))).isTrue();
    }

}
