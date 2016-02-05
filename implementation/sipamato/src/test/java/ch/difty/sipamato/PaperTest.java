package ch.difty.sipamato;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class PaperTest {

    @Test
    public void gettingFirstAuthor() {
        Paper p = new Paper();
        assertThat(p.getFirstAuthor()).isEmpty();

        p.setAuthor("LastName");
        assertThat(p.getFirstAuthor()).isEqualTo("LastName");

        p.setAuthor("LastName FirstName");
        assertThat(p.getFirstAuthor()).isEqualTo("LastName");
    }
}
