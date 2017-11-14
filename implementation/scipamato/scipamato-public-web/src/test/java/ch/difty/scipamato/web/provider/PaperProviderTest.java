package ch.difty.scipamato.web.provider;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import ch.difty.scipamato.NullArgumentException;
import ch.difty.scipamato.web.filter.PaperFilter;

public class PaperProviderTest {

    @Test
    public void degenerateConstruction() {
        String fieldName = "paperFilter";
        try {
            new PaperProvider(null, 20);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage(fieldName + " must not be null.");
        }
    }

    @Test
    public void construct() {
        PaperFilter filter = new PaperFilter();
        PaperProvider provider = new PaperProvider(filter, 20);
        assertThat(provider).isNotNull();
    }
}
