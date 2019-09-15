package ch.difty.scipamato.core.entity.search

import ch.difty.scipamato.core.entity.Code
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SearchConditionCodeBoxTest {

    private val cb = SearchConditionCodeBox()

    @Test
    fun emptyCodeBox_resultsInEmptyString() {
        assertThat(cb.toString()).isEmpty()
    }

    @Test
    fun codeBoxWithOneMember_resultsInCode() {
        cb.addCode(Code("1F", "c1f", "comm", false, 1, "CC1", "", 0))
        assertThat(cb.toString()).isEqualTo("1F")
    }

    @Test
    fun codeBoxWithMultipleMembers_resultsInConcatenatedCodes() {
        cb.addCode(Code("1F", "c1f", "comm1", false, 1, "CC1", "", 0))
        cb.addCode(Code("1G", "c1g", "comm2", true, 1, "CC1", "", 2))
        cb.addCode(Code("2A", "c2a", "comm3", false, 2, "CC2", "", 1))
        assertThat(cb.toString()).isEqualTo("1F&1G&2A")
    }
}
