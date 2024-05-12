package ch.difty.scipamato.publ.entity

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test

internal class PopulationCodeTest {

    @Test
    fun hasAllValues() {
        PopulationCode.entries shouldContainAll listOf(PopulationCode.CHILDREN, PopulationCode.ADULTS)
    }

    @Test
    fun assertIds() {
        PopulationCode.entries.map { it.id } shouldContainAll listOf(1.toShort(), 2.toShort())
    }

    @Test
    fun of_withExistingId() {
        PopulationCode.of(1.toShort()) shouldBeEqualTo PopulationCode.CHILDREN
    }

    @Test
    fun of_withNotExistingId_returnsEmptyNull() {
        PopulationCode.of(0.toShort()).shouldBeNull()
    }
}
