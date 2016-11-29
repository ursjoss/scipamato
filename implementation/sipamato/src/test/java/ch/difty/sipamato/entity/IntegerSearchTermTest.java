package ch.difty.sipamato.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import ch.difty.sipamato.entity.IntegerSearchTerm.MatchType;

public class IntegerSearchTermTest {

    private static final String KEY = "k";

    private IntegerSearchTerm st;

    private void assertTerm(MatchType type, int value, String raw) {
        assertTerm(type, value, value, raw);
    }

    private void assertTerm(MatchType type, int value, int value2, String raw) {
        assertThat(st.getKey()).isEqualTo(KEY);
        assertThat(st.getType()).isEqualTo(type);
        assertThat(st.getValue()).isEqualTo(value);
        assertThat(st.getValue2()).isEqualTo(value2);
        assertThat(st.getRawValue()).isEqualTo(raw);
    }

    @Test
    public void exactSearch() {
        final String raw = "2016";
        st = new IntegerSearchTerm(KEY, raw);
        assertTerm(MatchType.EXACT, 2016, raw);
    }

    @Test
    public void exactSearch_withSpaces() {
        final String raw = "   2016 ";
        st = new IntegerSearchTerm(KEY, raw);
        assertTerm(MatchType.EXACT, 2016, raw);
    }

    @Test
    public void exactSearch_withEqual() {
        final String raw = "=2016";
        st = new IntegerSearchTerm(KEY, raw);
        assertTerm(MatchType.EXACT, 2016, raw);
    }

    @Test
    public void exactSearch_withEqualAndSpaces() {
        final String raw = "=    2016";
        st = new IntegerSearchTerm(KEY, raw);
        assertTerm(MatchType.EXACT, 2016, raw);
    }

    @Test
    public void greaterThanOrEqualSearch() {
        final String raw = ">=2016";
        st = new IntegerSearchTerm(KEY, raw);
        assertTerm(MatchType.GREATER_OR_EQUAL, 2016, raw);
    }

    @Test
    public void greaterThanOrEqualSearch_WithSpaces() {
        final String raw = "   >=    2016 ";
        st = new IntegerSearchTerm(KEY, raw);
        assertTerm(MatchType.GREATER_OR_EQUAL, 2016, raw);
    }

    @Test
    public void greaterThanSearch() {
        final String raw = ">2016";
        st = new IntegerSearchTerm(KEY, raw);
        assertTerm(MatchType.GREATER_THAN, 2016, raw);
    }

    @Test
    public void lessThanOrEqualSearch() {
        final String raw = "<=2016";
        st = new IntegerSearchTerm(KEY, raw);
        assertTerm(MatchType.LESS_OR_EQUAL, 2016, raw);
    }

    @Test
    public void lessThanSearch() {
        final String raw = "<2016";
        st = new IntegerSearchTerm(KEY, raw);
        assertTerm(MatchType.LESS_THAN, 2016, raw);
    }

    @Test
    public void rangeSearch() {
        final String raw = "2016-2018";
        st = new IntegerSearchTerm(KEY, raw);
        assertTerm(MatchType.RANGE, 2016, 2018, raw);
    }

    @Test
    public void rangeSearch_withSpaces() {
        final String raw = "    2016     -    2018 ";
        st = new IntegerSearchTerm(KEY, raw);
        assertTerm(MatchType.RANGE, 2016, 2018, raw);
    }

    @Test
    public void invalidSearch_withNonNumericCharacters() {
        try {
            new IntegerSearchTerm(KEY, "2014a");
            fail("Should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NumberFormatException.class).hasMessage("For input string: \"2014a\"");
        }
    }

    @Test
    public void invalidSearch_withInvalidPattern() {
        try {
            new IntegerSearchTerm(KEY, ">>2014");
            fail("Should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class).hasMessage("For input string: \">2014\"");
        }
    }

}
