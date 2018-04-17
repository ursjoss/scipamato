package ch.difty.scipamato.core.entity.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import ch.difty.scipamato.core.entity.search.IntegerSearchTerm.MatchType;

public class IntegerSearchTermTest {

    private static final long   CONDITION_ID = 7;
    private static final String FIELD_NAME   = "fn";

    private IntegerSearchTerm st;

    private void assertTerm(MatchType type, int value, String raw) {
        assertTerm(type, value, value, raw);
    }

    private void assertTerm(MatchType type, int value, int value2, String raw) {
        assertThat(st.getSearchTermType()).isEqualTo(SearchTermType.INTEGER);
        assertThat(st.getSearchConditionId()).isEqualTo(CONDITION_ID);
        assertThat(st.getFieldName()).isEqualTo(FIELD_NAME);
        assertThat(st.getType()).isEqualTo(type);
        assertThat(st.getValue()).isEqualTo(value);
        assertThat(st.getValue2()).isEqualTo(value2);
        assertThat(st.getRawSearchTerm()).isEqualTo(raw);
        assertThat(st.getDisplayValue()).isEqualTo(raw);
    }

    @Test
    public void exactSearch() {
        final String raw = "2016";
        st = new IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw);
        assertTerm(MatchType.EXACT, 2016, raw);
    }

    @Test
    public void exactSearch_withSpaces() {
        final String raw = "   2016 ";
        st = new IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw);
        assertTerm(MatchType.EXACT, 2016, raw);
    }

    @Test
    public void exactSearch_withEqual() {
        final String raw = "=2016";
        st = new IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw);
        assertTerm(MatchType.EXACT, 2016, raw);
    }

    @Test
    public void exactSearch_withEqualAndSpaces() {
        final String raw = "=    2016";
        st = new IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw);
        assertTerm(MatchType.EXACT, 2016, raw);
    }

    @Test
    public void greaterThanOrEqualSearch() {
        final String raw = ">=2016";
        st = new IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw);
        assertTerm(MatchType.GREATER_OR_EQUAL, 2016, raw);
    }

    @Test
    public void greaterThanOrEqualSearch_WithSpaces() {
        final String raw = "   >=    2016 ";
        st = new IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw);
        assertTerm(MatchType.GREATER_OR_EQUAL, 2016, raw);
    }

    @Test
    public void greaterThanSearch() {
        final String raw = ">2016";
        st = new IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw);
        assertTerm(MatchType.GREATER_THAN, 2016, raw);
    }

    @Test
    public void lessThanOrEqualSearch() {
        final String raw = "<=2016";
        st = new IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw);
        assertTerm(MatchType.LESS_OR_EQUAL, 2016, raw);
    }

    @Test
    public void lessThanSearch() {
        final String raw = "<2016";
        st = new IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw);
        assertTerm(MatchType.LESS_THAN, 2016, raw);
    }

    @Test
    public void rangeSearch() {
        final String raw = "2016-2018";
        st = new IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw);
        assertTerm(MatchType.RANGE, 2016, 2018, raw);
    }

    @Test
    public void rangeSearch_withSpaces() {
        final String raw = "    2016     -    2018 ";
        st = new IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw);
        assertTerm(MatchType.RANGE, 2016, 2018, raw);
    }

    @Test
    public void hasNoValue() {
        final String raw = "=\"\"";
        st = new IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw);
        assertTerm(MatchType.MISSING, 0, 0, raw);
    }

    @Test
    public void hasAnyValue() {
        final String raw = ">\"\"";
        st = new IntegerSearchTerm(CONDITION_ID, FIELD_NAME, raw);
        assertTerm(MatchType.PRESENT, 0, 0, raw);
    }

    @Test
    public void invalidSearch_withNonNumericCharacters() {
        try {
            new IntegerSearchTerm(CONDITION_ID, FIELD_NAME, "2014a");
            fail("Should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NumberFormatException.class)
                .hasMessage("For input string: \"2014a\"");
        }
    }

    @Test
    public void invalidSearch_withInvalidPattern() {
        try {
            new IntegerSearchTerm(CONDITION_ID, FIELD_NAME, ">>2014");
            fail("Should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("For input string: \">2014\"");
        }
    }

}
