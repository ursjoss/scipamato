package ch.difty.scipamato.core.entity.search

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail

import org.junit.jupiter.api.Test

internal class SearchTermTest {

    @Test
    fun booleanSearchTerm() {
        val st = SearchTerm.newSearchTerm(10, SearchTermType.BOOLEAN.id, 1L, "fn", "true")
        assertThat(st).isInstanceOf(BooleanSearchTerm::class.java)

        val bst = st as BooleanSearchTerm
        assertThat(bst.id).isEqualTo(10)
        assertThat(bst.searchTermType).isEqualTo(SearchTermType.BOOLEAN)
        assertThat(bst.searchConditionId).isEqualTo(1L)
        assertThat(bst.fieldName).isEqualTo("fn")
        assertThat(bst.rawSearchTerm).isEqualTo("true")
        assertThat(bst.value).isTrue()
    }

    @Test
    fun integerSearchTerm() {
        val st = SearchTerm.newSearchTerm(11, SearchTermType.INTEGER.id, 2L, "fn2", "5-7")
        assertThat(st).isInstanceOf(IntegerSearchTerm::class.java)

        val ist = st as IntegerSearchTerm
        assertThat(ist.id).isEqualTo(11)
        assertThat(ist.searchTermType).isEqualTo(SearchTermType.INTEGER)
        assertThat(ist.searchConditionId).isEqualTo(2L)
        assertThat(ist.fieldName).isEqualTo("fn2")
        assertThat(ist.rawSearchTerm).isEqualTo("5-7")
        assertThat(ist.value).isEqualTo(5)
        assertThat(ist.value2).isEqualTo(7)
    }

    @Test
    fun stringSearchTerm() {
        val st = SearchTerm.newSearchTerm(12, SearchTermType.STRING.id, 3L, "fn3", "foo*")
        assertThat(st).isInstanceOf(StringSearchTerm::class.java)

        verify(st)
    }

    private fun verify(st: SearchTerm) {
        val sst = st as StringSearchTerm
        assertThat(sst.id).isEqualTo(12)
        assertThat(sst.searchTermType).isEqualTo(SearchTermType.STRING)
        assertThat(sst.searchConditionId).isEqualTo(3L)
        assertThat(sst.fieldName).isEqualTo("fn3")
        assertThat(sst.rawSearchTerm).isEqualTo("foo*")
        assertThat(sst.tokens).hasSize(1)
        assertThat(sst.tokens[0].sqlData).isEqualTo("foo%")
        assertThat(sst.tokens[0].negate).isFalse()
    }

    @Test
    fun stringSearchTerm2() {
        val st = SearchTerm.newSearchTerm(12, SearchTermType.STRING, 3L, "fn3", "foo*")
        assertThat(st).isInstanceOf(StringSearchTerm::class.java)
        verify(st)
    }

    @Test
    fun undefinedSearchTerm_throws() {
        try {
            SearchTerm.newSearchTerm(13, SearchTermType.UNSUPPORTED, 4L, "fn4", "whatever")
            fail<Any>("should have thrown exception")
        } catch (ex: Error) {
            assertThat(ex)
                    .isInstanceOf(AssertionError::class.java)
                    .hasMessage("SearchTermType.UNSUPPORTED is not supported")
        }

    }

    @Test
    fun auditSearchTerm_forFieldEndingWithUserTag_akaUserField_returnsUserTokenOnly() {
        assertUserFieldEndingWith("_BY")
    }

    @Test
    fun auditSearchTerm_forFieldEndingWithUserTagLC_akaUserField_returnsUserTokenOnly() {
        assertUserFieldEndingWith("_by")
    }

    private fun assertUserFieldEndingWith(userFieldTag: String) {
        val userFieldName = "fn4$userFieldTag"
        val st = SearchTerm.newSearchTerm(13, 3, 4L, userFieldName, "foo >=\"2017-02-01\"")
        assertThat(st).isInstanceOf(AuditSearchTerm::class.java)

        val ast = st as AuditSearchTerm
        assertThat(ast.id).isEqualTo(13)
        assertThat(ast.searchTermType).isEqualTo(SearchTermType.AUDIT)
        assertThat(ast.searchConditionId).isEqualTo(4L)
        assertThat(ast.fieldName).isEqualTo(userFieldName)
        assertThat(ast.rawSearchTerm).isEqualTo("foo >=\"2017-02-01\"")
        assertThat(ast.tokens).hasSize(1)
        assertThat(ast.tokens[0].userSqlData).isEqualTo("foo")
        assertThat(ast.tokens[0].dateSqlData).isNull()
    }

    @Test
    fun auditSearchTerm_forFieldNotEndingWithUserTag_akaDateField_returnsDateTokenOnly() {
        val userFieldName = "fn4"
        val st = SearchTerm.newSearchTerm(13, 3, 4L, userFieldName, "foo >=\"2017-02-01\"")
        assertThat(st).isInstanceOf(AuditSearchTerm::class.java)

        val ast = st as AuditSearchTerm
        assertThat(ast.id).isEqualTo(13)
        assertThat(ast.searchTermType).isEqualTo(SearchTermType.AUDIT)
        assertThat(ast.searchConditionId).isEqualTo(4L)
        assertThat(ast.fieldName).isEqualTo(userFieldName)
        assertThat(ast.rawSearchTerm).isEqualTo("foo >=\"2017-02-01\"")
        assertThat(ast.tokens).hasSize(1)
        assertThat(ast.tokens[0].userSqlData).isNull()
        assertThat(ast.tokens[0].dateSqlData).isEqualTo("2017-02-01 00:00:00")
    }

    @Test
    fun newStringSearchTerm() {
        val st = SearchTerm.newStringSearchTerm("field", "rawSearchTerm")
        assertThat(st.fieldName).isEqualTo("field")
        assertThat(st.rawSearchTerm).isEqualTo("rawSearchTerm")
    }

    @Test
    fun newIntegerSearchTerm() {
        val st = SearchTerm.newIntegerSearchTerm("field", "5")
        assertThat(st.fieldName).isEqualTo("field")
        assertThat(st.rawSearchTerm).isEqualTo("5")
    }

    @Test
    fun newBooleanSearchTerm() {
        val st = SearchTerm.newBooleanSearchTerm("field", "rawSearchTerm")
        assertThat(st.fieldName).isEqualTo("field")
        assertThat(st.rawSearchTerm).isEqualTo("rawSearchTerm")
    }

    @Test
    fun newAuditSearchTerm() {
        val st = SearchTerm.newAuditSearchTerm("field", "rawSearchTerm")
        assertThat(st.fieldName).isEqualTo("field")
        assertThat(st.rawSearchTerm).isEqualTo("rawSearchTerm")
    }

}
