package ch.difty.sipamato.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import ch.difty.sipamato.entity.IntegerSearchTerm.IntegerSearchTermType;

public class IntegerSearchTermTest {

    private static final String KEY = "k";

    @Test
    public void exactSearch() {
        IntegerSearchTerm st = new IntegerSearchTerm(KEY, "2016");
        assertThat(st.key).isEqualTo(KEY);
        assertThat(st.rawValue).isEqualTo("2016");
        assertThat(st.type).isEqualTo(IntegerSearchTermType.EXACT);
        assertThat(st.value).isEqualTo(2016);
        assertThat(st.value2).isEqualTo(2016);
    }

    @Test
    public void exactSearch_withEqual() {
        IntegerSearchTerm st = new IntegerSearchTerm(KEY, "=2016");
        assertThat(st.key).isEqualTo(KEY);
        assertThat(st.rawValue).isEqualTo("=2016");
        assertThat(st.type).isEqualTo(IntegerSearchTermType.EXACT);
        assertThat(st.value).isEqualTo(2016);
        assertThat(st.value2).isEqualTo(2016);
    }

    @Test
    public void greaterThanOrEqualSearch() {
        IntegerSearchTerm st = new IntegerSearchTerm(KEY, ">=2016");
        assertThat(st.key).isEqualTo(KEY);
        assertThat(st.rawValue).isEqualTo(">=2016");
        assertThat(st.type).isEqualTo(IntegerSearchTermType.GREATER_OR_EQUAL);
        assertThat(st.value).isEqualTo(2016);
        assertThat(st.value2).isEqualTo(2016);
    }

    @Test
    public void greaterThanSearch() {
        IntegerSearchTerm st = new IntegerSearchTerm(KEY, ">  2016");
        assertThat(st.key).isEqualTo(KEY);
        assertThat(st.rawValue).isEqualTo(">  2016");
        assertThat(st.type).isEqualTo(IntegerSearchTermType.GREATER_THAN);
        assertThat(st.value).isEqualTo(2016);
        assertThat(st.value2).isEqualTo(2016);
    }

    @Test
    public void lessThanOrEqualSearch() {
        IntegerSearchTerm st = new IntegerSearchTerm(KEY, "<= 2016");
        assertThat(st.key).isEqualTo(KEY);
        assertThat(st.rawValue).isEqualTo("<= 2016");
        assertThat(st.type).isEqualTo(IntegerSearchTermType.LESS_OR_EQUAL);
        assertThat(st.value).isEqualTo(2016);
        assertThat(st.value2).isEqualTo(2016);
    }

    @Test
    public void lessThanSearch() {
        IntegerSearchTerm st = new IntegerSearchTerm(KEY, "<2016");
        assertThat(st.key).isEqualTo(KEY);
        assertThat(st.rawValue).isEqualTo("<2016");
        assertThat(st.type).isEqualTo(IntegerSearchTermType.LESS_THAN);
        assertThat(st.value).isEqualTo(2016);
        assertThat(st.value2).isEqualTo(2016);
    }

    @Test
    public void rangeSearch() {
        IntegerSearchTerm st = new IntegerSearchTerm(KEY, "2016-2018");
        assertThat(st.key).isEqualTo(KEY);
        assertThat(st.rawValue).isEqualTo("2016-2018");
        assertThat(st.type).isEqualTo(IntegerSearchTermType.RANGE);
        assertThat(st.value).isEqualTo(2016);
        assertThat(st.value2).isEqualTo(2018);
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
