package ch.difty.scipamato.publ.entity

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test

internal class StudyDesignCodeTest {

    @Test
    fun hasAllValues() {
        StudyDesignCode.entries shouldContainAll listOf(
            StudyDesignCode.EXPERIMENTAL,
            StudyDesignCode.EPIDEMIOLOGICAL,
            StudyDesignCode.OVERVIEW_METHODOLOGY
        )
    }

    @Test
    fun assertIds() {
        StudyDesignCode.entries.map { it.id } shouldContainAll listOf(1.toShort(), 2.toShort(), 3.toShort())
    }

    @Test
    fun of_withExistingId() {
        StudyDesignCode.of(1.toShort()) shouldBeEqualTo StudyDesignCode.EXPERIMENTAL
    }

    @Test
    fun of_withNotExistingId_returnsEmptyOptional() {
        StudyDesignCode.of(0.toShort()).shouldBeNull()
    }
}
