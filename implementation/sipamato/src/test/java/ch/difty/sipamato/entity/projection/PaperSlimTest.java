package ch.difty.sipamato.entity.projection;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class PaperSlimTest {

    private final PaperSlim ps = new PaperSlim();

    @Before
    public void setUp() {
        ps.setId(1l);
        ps.setFirstAuthor("firstAuthor");
        ps.setTitle("title");
        ps.setPublicationYear(2016);
    }

    @Test
    public void getting() {
        assertThat(ps.getId()).isEqualTo(1l);
        assertThat(ps.getPublicationYear()).isEqualTo(2016);
        assertThat(ps.getTitle()).isEqualTo("title");
        assertThat(ps.getFirstAuthor()).isEqualTo("firstAuthor");
    }

    @Test
    public void displayValue() {
        assertThat(ps.getDisplayValue()).isEqualTo("firstAuthor (2016): title.");
    }
}
