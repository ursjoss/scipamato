package ch.difty.scipamato.core.entity.search;

import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class SearchOrderFilterTest {

    @Test
    public void canInstantiate() {
        SearchOrderFilter f = new SearchOrderFilter();
        f.setNameMask("foo");
        f.setOwner(3);
        f.setOwnerIncludingGlobal(4);
        f.setGlobal(true);

        assertThat(f.getNameMask()).isEqualTo("foo");
        assertThat(f.getOwner()).isEqualTo(3);
        assertThat(f.getOwnerIncludingGlobal()).isEqualTo(4);
        assertThat(f.getGlobal()).isEqualTo(true);

        assertThat(f.toString()).isEqualTo(
            "SearchOrderFilter(nameMask=foo, owner=3, global=true, ownerIncludingGlobal=4)");
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(SearchOrderFilter.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }
}
