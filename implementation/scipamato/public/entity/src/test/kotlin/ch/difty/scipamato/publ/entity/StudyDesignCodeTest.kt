package ch.difty.scipamato.publ.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StudyDesignCodeTest {

    @Test
    fun hasAllValues() {
        assertThat(StudyDesignCode.values()).containsExactly(
            StudyDesignCode.EXPERIMENTAL,
            StudyDesignCode.EPIDEMIOLOGICAL,
            StudyDesignCode.OVERVIEW_METHODOLOGY
        )
    }

    @Test
    fun assertIds() {
        assertThat(StudyDesignCode.values().map { it.id }).containsExactly(1.toShort(), 2.toShort(), 3.toShort()
        )
    }

    @Test
    fun of_withExistingId() {
        assertThat(StudyDesignCode.of(1.toShort())).hasValue(StudyDesignCode.EXPERIMENTAL)
    }

    @Test
    fun of_withNotExistingId_returnsEmptyOptional() {
        assertThat(StudyDesignCode.of(0.toShort())).isEmpty
    }
}
