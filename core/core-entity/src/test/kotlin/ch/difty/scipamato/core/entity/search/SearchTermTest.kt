package ch.difty.scipamato.core.entity.search

import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.junit.jupiter.api.Test

internal class SearchTermTest {

    @Test
    fun booleanSearchTerm() {
        val st = SearchTerm.newSearchTerm(10, SearchTermType.BOOLEAN.id, 1L, "fn", "true")
        st shouldBeInstanceOf BooleanSearchTerm::class

        val bst = st as BooleanSearchTerm
        bst.id shouldBeEqualTo 10
        bst.searchTermType shouldBeEqualTo SearchTermType.BOOLEAN
        bst.searchConditionId shouldBeEqualTo 1L
        bst.fieldName shouldBeEqualTo "fn"
        bst.rawSearchTerm shouldBeEqualTo "true"
        bst.value.shouldBeTrue()
    }

    @Test
    fun integerSearchTerm() {
        val st = SearchTerm.newSearchTerm(11, SearchTermType.INTEGER.id, 2L, "fn2", "5-7")
        st shouldBeInstanceOf IntegerSearchTerm::class

        val ist = st as IntegerSearchTerm
        ist.id shouldBeEqualTo 11
        ist.searchTermType shouldBeEqualTo SearchTermType.INTEGER
        ist.searchConditionId shouldBeEqualTo 2L
        ist.fieldName shouldBeEqualTo "fn2"
        ist.rawSearchTerm shouldBeEqualTo "5-7"
        ist.value shouldBeEqualTo 5
        ist.value2 shouldBeEqualTo 7
    }

    @Test
    fun stringSearchTerm() {
        val st = SearchTerm.newSearchTerm(12, SearchTermType.STRING.id, 3L, "fn3", "foo*")
        st shouldBeInstanceOf StringSearchTerm::class

        verify(st)
    }

    private fun verify(st: SearchTerm) {
        val sst = st as StringSearchTerm
        sst.id shouldBeEqualTo 12
        sst.searchTermType shouldBeEqualTo SearchTermType.STRING
        sst.searchConditionId shouldBeEqualTo 3L
        sst.fieldName shouldBeEqualTo "fn3"
        sst.rawSearchTerm shouldBeEqualTo "foo*"
        sst.tokens shouldHaveSize 1
        sst.tokens[0].sqlData shouldBeEqualTo "foo%"
        sst.tokens[0].negate.shouldBeFalse()
    }

    @Test
    fun stringSearchTerm2() {
        val st = SearchTerm.newSearchTerm(12, SearchTermType.STRING, 3L, "fn3", "foo*")
        st shouldBeInstanceOf StringSearchTerm::class
        verify(st)
    }

    @Test
    fun undefinedSearchTerm_throws() {
        invoking {
            SearchTerm.newSearchTerm(13, SearchTermType.UNSUPPORTED, 4L, "fn4", "whatever")
        } shouldThrow AssertionError::class withMessage "SearchTermType.UNSUPPORTED is not supported"
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
        val st = SearchTerm.newSearchTerm(13, 3, 4L, userFieldName, """foo >="2017-02-01"""")
        st shouldBeInstanceOf AuditSearchTerm::class

        val ast = st as AuditSearchTerm
        ast.id shouldBeEqualTo 13
        ast.searchTermType shouldBeEqualTo SearchTermType.AUDIT
        ast.searchConditionId shouldBeEqualTo 4L
        ast.fieldName shouldBeEqualTo userFieldName
        ast.rawSearchTerm shouldBeEqualTo """foo >="2017-02-01""""
        ast.tokens shouldHaveSize 1
        ast.tokens[0].userSqlData shouldBeEqualTo "foo"
        ast.tokens[0].dateSqlData.shouldBeNull()
    }

    @Test
    fun auditSearchTerm_forFieldNotEndingWithUserTag_akaDateField_returnsDateTokenOnly() {
        val userFieldName = "fn4"
        val st = SearchTerm.newSearchTerm(13, 3, 4L, userFieldName, "foo >=\"2017-02-01\"")
        st shouldBeInstanceOf AuditSearchTerm::class

        val ast = st as AuditSearchTerm
        ast.id shouldBeEqualTo 13
        ast.searchTermType shouldBeEqualTo SearchTermType.AUDIT
        ast.searchConditionId shouldBeEqualTo 4L
        ast.fieldName shouldBeEqualTo userFieldName
        ast.rawSearchTerm shouldBeEqualTo "foo >=\"2017-02-01\""
        ast.tokens shouldHaveSize 1
        ast.tokens[0].userSqlData.shouldBeNull()
        ast.tokens[0].dateSqlData shouldBeEqualTo "2017-02-01 00:00:00"
    }

    @Test
    fun newStringSearchTerm() {
        val st = SearchTerm.newStringSearchTerm("field", "rawSearchTerm")
        st.fieldName shouldBeEqualTo "field"
        st.rawSearchTerm shouldBeEqualTo "rawSearchTerm"
    }

    @Test
    fun newIntegerSearchTerm() {
        val st = SearchTerm.newIntegerSearchTerm("field", "5")
        st.fieldName shouldBeEqualTo "field"
        st.rawSearchTerm shouldBeEqualTo "5"
    }

    @Test
    fun newBooleanSearchTerm() {
        val st = SearchTerm.newBooleanSearchTerm("field", "rawSearchTerm")
        st.fieldName shouldBeEqualTo "field"
        st.rawSearchTerm shouldBeEqualTo "rawSearchTerm"
    }

    @Test
    fun newAuditSearchTerm() {
        val st = SearchTerm.newAuditSearchTerm("field", "rawSearchTerm")
        st.fieldName shouldBeEqualTo "field"
        st.rawSearchTerm shouldBeEqualTo "rawSearchTerm"
    }
}
