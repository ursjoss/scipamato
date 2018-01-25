package ch.difty.scipamato.core.entity.search;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.core.entity.search.AuditSearchTerm;
import ch.difty.scipamato.core.entity.search.BooleanSearchTerm;
import ch.difty.scipamato.core.entity.search.IntegerSearchTerm;
import ch.difty.scipamato.core.entity.search.SearchTerm;
import ch.difty.scipamato.core.entity.search.SearchTermType;
import ch.difty.scipamato.core.entity.search.SearchTerms;
import ch.difty.scipamato.core.entity.search.StringSearchTerm;

public class SearchTermsTest {

    @Test
    public void booleanSearchTerm() {
        SearchTerm st = SearchTerms.newSearchTerm(10, 0, 1l, "fn", "true");
        assertThat(st).isInstanceOf(BooleanSearchTerm.class);

        BooleanSearchTerm bst = (BooleanSearchTerm) st;
        assertThat(bst.getId()).isEqualTo(10);
        assertThat(bst.getSearchTermType()).isEqualTo(SearchTermType.BOOLEAN);
        assertThat(bst.getSearchConditionId()).isEqualTo(1l);
        assertThat(bst.getFieldName()).isEqualTo("fn");
        assertThat(bst.getRawSearchTerm()).isEqualTo("true");
        assertThat(bst.getValue()).isTrue();
    }

    @Test
    public void integerSearchTerm() {
        SearchTerm st = SearchTerms.newSearchTerm(11, 1, 2l, "fn2", "5-7");
        assertThat(st).isInstanceOf(IntegerSearchTerm.class);

        IntegerSearchTerm ist = (IntegerSearchTerm) st;
        assertThat(ist.getId()).isEqualTo(11);
        assertThat(ist.getSearchTermType()).isEqualTo(SearchTermType.INTEGER);
        assertThat(ist.getSearchConditionId()).isEqualTo(2l);
        assertThat(ist.getFieldName()).isEqualTo("fn2");
        assertThat(ist.getRawSearchTerm()).isEqualTo("5-7");
        assertThat(ist.getValue()).isEqualTo(5);
        assertThat(ist.getValue2()).isEqualTo(7);
    }

    @Test
    public void stringSearchTerm() {
        SearchTerm st = SearchTerms.newSearchTerm(12, 2, 3l, "fn3", "foo*");
        assertThat(st).isInstanceOf(StringSearchTerm.class);

        StringSearchTerm sst = (StringSearchTerm) st;
        assertThat(sst.getId()).isEqualTo(12);
        assertThat(sst.getSearchTermType()).isEqualTo(SearchTermType.STRING);
        assertThat(sst.getSearchConditionId()).isEqualTo(3l);
        assertThat(sst.getFieldName()).isEqualTo("fn3");
        assertThat(sst.getRawSearchTerm()).isEqualTo("foo*");
        assertThat(sst.getTokens()).hasSize(1);
        assertThat(sst.getTokens()
            .get(0).sqlData).isEqualTo("foo%");
        assertThat(sst.getTokens()
            .get(0).negate).isFalse();
    }

    @Test
    public void auditSearchTerm_forFieldEndingWithUserTag_akaUserField_returnsUserTokenOnly() {
        assertUserFieldEndingWith("_BY");
    }

    @Test
    public void auditSearchTerm_forFieldEndingWithUserTagLC_akaUserField_returnsUserTokenOnly() {
        assertUserFieldEndingWith("_by");
    }

    private void assertUserFieldEndingWith(String userFieldTag) {
        String userFieldName = "fn4" + userFieldTag;
        SearchTerm st = SearchTerms.newSearchTerm(13, 3, 4l, userFieldName, "foo >=\"2017-02-01\"");
        assertThat(st).isInstanceOf(AuditSearchTerm.class);

        AuditSearchTerm ast = (AuditSearchTerm) st;
        assertThat(ast.getId()).isEqualTo(13);
        assertThat(ast.getSearchTermType()).isEqualTo(SearchTermType.AUDIT);
        assertThat(ast.getSearchConditionId()).isEqualTo(4l);
        assertThat(ast.getFieldName()).isEqualTo(userFieldName);
        assertThat(ast.getRawSearchTerm()).isEqualTo("foo >=\"2017-02-01\"");
        assertThat(ast.getTokens()).hasSize(1);
        assertThat(ast.getTokens()
            .get(0)
            .getUserSqlData()).isEqualTo("foo");
        assertThat(ast.getTokens()
            .get(0)
            .getDateSqlData()).isNull();
    }

    @Test
    public void auditSearchTerm_forFieldNotEndingWithUserTag_akaDateField_returnsDateTokenOnly() {
        String userFieldName = "fn4";
        SearchTerm st = SearchTerms.newSearchTerm(13, 3, 4l, userFieldName, "foo >=\"2017-02-01\"");
        assertThat(st).isInstanceOf(AuditSearchTerm.class);

        AuditSearchTerm ast = (AuditSearchTerm) st;
        assertThat(ast.getId()).isEqualTo(13);
        assertThat(ast.getSearchTermType()).isEqualTo(SearchTermType.AUDIT);
        assertThat(ast.getSearchConditionId()).isEqualTo(4l);
        assertThat(ast.getFieldName()).isEqualTo(userFieldName);
        assertThat(ast.getRawSearchTerm()).isEqualTo("foo >=\"2017-02-01\"");
        assertThat(ast.getTokens()).hasSize(1);
        assertThat(ast.getTokens()
            .get(0)
            .getUserSqlData()).isNull();
        assertThat(ast.getTokens()
            .get(0)
            .getDateSqlData()).isEqualTo("2017-02-01 00:00:00");
    }

    @Test
    public void newStringSearchTerm() {
        StringSearchTerm st = SearchTerms.newStringSearchTerm("field", "rawSearchTerm");
        assertThat(st.getFieldName()).isEqualTo("field");
        assertThat(st.getRawSearchTerm()).isEqualTo("rawSearchTerm");
    }

    @Test
    public void newIntegerSearchTerm() {
        IntegerSearchTerm st = SearchTerms.newIntegerSearchTerm("field", "5");
        assertThat(st.getFieldName()).isEqualTo("field");
        assertThat(st.getRawSearchTerm()).isEqualTo("5");
    }

    @Test
    public void newBooleanSearchTerm() {
        BooleanSearchTerm st = SearchTerms.newBooleanSearchTerm("field", "rawSearchTerm");
        assertThat(st.getFieldName()).isEqualTo("field");
        assertThat(st.getRawSearchTerm()).isEqualTo("rawSearchTerm");
    }

    @Test
    public void newAuditSearchTerm() {
        AuditSearchTerm st = SearchTerms.newAuditSearchTerm("field", "rawSearchTerm");
        assertThat(st.getFieldName()).isEqualTo("field");
        assertThat(st.getRawSearchTerm()).isEqualTo("rawSearchTerm");
    }

}
