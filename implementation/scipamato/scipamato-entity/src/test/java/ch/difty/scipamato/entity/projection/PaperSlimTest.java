package ch.difty.scipamato.entity.projection;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

public class PaperSlimTest {

    private PaperSlim ps = new PaperSlim();

    @Before
    public void setUp() {
        ps.setId(1l);
        ps.setNumber(10l);
        ps.setFirstAuthor("firstAuthor");
        ps.setTitle("title");
        ps.setPublicationYear(2016);
    }

    @Test
    public void getting() {
        assertThat(ps.getId()).isEqualTo(1l);
        assertThat(ps.getNumber()).isEqualTo(10l);
        assertThat(ps.getPublicationYear()).isEqualTo(2016);
        assertThat(ps.getTitle()).isEqualTo("title");
        assertThat(ps.getFirstAuthor()).isEqualTo("firstAuthor");
    }

    @Test
    public void displayValue() {
        assertThat(ps.getDisplayValue()).isEqualTo("firstAuthor (2016): title.");
    }

    @Test
    public void alternativeConstructor() {
        ps = new PaperSlim(1l, 10l, "firstAuthor", 2016, "title");
        getting();
    }
}
