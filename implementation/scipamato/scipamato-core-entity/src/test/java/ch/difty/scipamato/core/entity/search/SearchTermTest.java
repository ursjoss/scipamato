package ch.difty.scipamato.core.entity.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class SearchTermTest {

    @Test
    void booleanSearchTerm() {
        SearchTerm st = SearchTerm.newSearchTerm(10, SearchTermType.BOOLEAN.getId(), 1L, "fn", "true");
        assertThat(st).isInstanceOf(BooleanSearchTerm.class);

        BooleanSearchTerm bst = (BooleanSearchTerm) st;
        assertThat(bst.getId()).isEqualTo(10);
        assertThat(bst.getSearchTermType()).isEqualTo(SearchTermType.BOOLEAN);
        assertThat(bst.getSearchConditionId()).isEqualTo(1L);
        assertThat(bst.getFieldName()).isEqualTo("fn");
        assertThat(bst.getRawSearchTerm()).isEqualTo("true");
        assertThat(bst.getValue()).isTrue();
    }

    @Test
    void integerSearchTerm() {
        SearchTerm st = SearchTerm.newSearchTerm(11, SearchTermType.INTEGER.getId(), 2L, "fn2", "5-7");
        assertThat(st).isInstanceOf(IntegerSearchTerm.class);

        IntegerSearchTerm ist = (IntegerSearchTerm) st;
        assertThat(ist.getId()).isEqualTo(11);
        assertThat(ist.getSearchTermType()).isEqualTo(SearchTermType.INTEGER);
        assertThat(ist.getSearchConditionId()).isEqualTo(2L);
        assertThat(ist.getFieldName()).isEqualTo("fn2");
        assertThat(ist.getRawSearchTerm()).isEqualTo("5-7");
        assertThat(ist.getValue()).isEqualTo(5);
        assertThat(ist.getValue2()).isEqualTo(7);
    }

    @Test
    void stringSearchTerm() {
        SearchTerm st = SearchTerm.newSearchTerm(12, SearchTermType.STRING.getId(), 3L, "fn3", "foo*");
        assertThat(st).isInstanceOf(StringSearchTerm.class);

        verify(st);
    }

    private void verify(SearchTerm st) {
        StringSearchTerm sst = (StringSearchTerm) st;
        assertThat(sst.getId()).isEqualTo(12);
        assertThat(sst.getSearchTermType()).isEqualTo(SearchTermType.STRING);
        assertThat(sst.getSearchConditionId()).isEqualTo(3L);
        assertThat(sst.getFieldName()).isEqualTo("fn3");
        assertThat(sst.getRawSearchTerm()).isEqualTo("foo*");
        assertThat(sst.getTokens()).hasSize(1);
        assertThat(sst
            .getTokens()
            .get(0).sqlData).isEqualTo("foo%");
        assertThat(sst
            .getTokens()
            .get(0).negate).isFalse();
    }

    @Test
    void stringSearchTerm2() {
        SearchTerm st = SearchTerm.newSearchTerm(12, SearchTermType.STRING, 3L, "fn3", "foo*");
        assertThat(st).isInstanceOf(StringSearchTerm.class);

        verify(st);
    }

    @Test
    void undefinedSearchTerm_throws() {
        try {
            SearchTerm.newSearchTerm(13, SearchTermType.UNSUPPORTED, 4L, "fn4", "whatever");
            fail("should have thrown exception");
        } catch (Error ex) {
            assertThat(ex)
                .isInstanceOf(AssertionError.class)
                .hasMessage("SearchTermType.UNSUPPORTED is not supported");
        }
    }

    @Test
    void auditSearchTerm_forFieldEndingWithUserTag_akaUserField_returnsUserTokenOnly() {
        assertUserFieldEndingWith("_BY");
    }

    @Test
    void auditSearchTerm_forFieldEndingWithUserTagLC_akaUserField_returnsUserTokenOnly() {
        assertUserFieldEndingWith("_by");
    }

    private void assertUserFieldEndingWith(String userFieldTag) {
        String userFieldName = "fn4" + userFieldTag;
        SearchTerm st = SearchTerm.newSearchTerm(13, 3, 4L, userFieldName, "foo >=\"2017-02-01\"");
        assertThat(st).isInstanceOf(AuditSearchTerm.class);

        AuditSearchTerm ast = (AuditSearchTerm) st;
        assertThat(ast.getId()).isEqualTo(13);
        assertThat(ast.getSearchTermType()).isEqualTo(SearchTermType.AUDIT);
        assertThat(ast.getSearchConditionId()).isEqualTo(4L);
        assertThat(ast.getFieldName()).isEqualTo(userFieldName);
        assertThat(ast.getRawSearchTerm()).isEqualTo("foo >=\"2017-02-01\"");
        assertThat(ast.getTokens()).hasSize(1);
        assertThat(ast
            .getTokens()
            .get(0)
            .getUserSqlData()).isEqualTo("foo");
        assertThat(ast
            .getTokens()
            .get(0)
            .getDateSqlData()).isNull();
    }

    @Test
    void auditSearchTerm_forFieldNotEndingWithUserTag_akaDateField_returnsDateTokenOnly() {
        String userFieldName = "fn4";
        SearchTerm st = SearchTerm.newSearchTerm(13, 3, 4L, userFieldName, "foo >=\"2017-02-01\"");
        assertThat(st).isInstanceOf(AuditSearchTerm.class);

        AuditSearchTerm ast = (AuditSearchTerm) st;
        assertThat(ast.getId()).isEqualTo(13);
        assertThat(ast.getSearchTermType()).isEqualTo(SearchTermType.AUDIT);
        assertThat(ast.getSearchConditionId()).isEqualTo(4L);
        assertThat(ast.getFieldName()).isEqualTo(userFieldName);
        assertThat(ast.getRawSearchTerm()).isEqualTo("foo >=\"2017-02-01\"");
        assertThat(ast.getTokens()).hasSize(1);
        assertThat(ast
            .getTokens()
            .get(0)
            .getUserSqlData()).isNull();
        assertThat(ast
            .getTokens()
            .get(0)
            .getDateSqlData()).isEqualTo("2017-02-01 00:00:00");
    }

    @Test
    void newStringSearchTerm() {
        StringSearchTerm st = SearchTerm.newStringSearchTerm("field", "rawSearchTerm");
        assertThat(st.getFieldName()).isEqualTo("field");
        assertThat(st.getRawSearchTerm()).isEqualTo("rawSearchTerm");
    }

    @Test
    void newIntegerSearchTerm() {
        IntegerSearchTerm st = SearchTerm.newIntegerSearchTerm("field", "5");
        assertThat(st.getFieldName()).isEqualTo("field");
        assertThat(st.getRawSearchTerm()).isEqualTo("5");
    }

    @Test
    void newBooleanSearchTerm() {
        BooleanSearchTerm st = SearchTerm.newBooleanSearchTerm("field", "rawSearchTerm");
        assertThat(st.getFieldName()).isEqualTo("field");
        assertThat(st.getRawSearchTerm()).isEqualTo("rawSearchTerm");
    }

    @Test
    void newAuditSearchTerm() {
        AuditSearchTerm st = SearchTerm.newAuditSearchTerm("field", "rawSearchTerm");
        assertThat(st.getFieldName()).isEqualTo("field");
        assertThat(st.getRawSearchTerm()).isEqualTo("rawSearchTerm");
    }

}
