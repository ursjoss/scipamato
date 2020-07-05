package ch.difty.scipamato.publ.entity

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test

internal class PopulationCodeTest {

    @Test
    fun hasAllValues() {
        PopulationCode.values() shouldContainAll listOf(PopulationCode.CHILDREN, PopulationCode.ADULTS)
    }

    @Test
    fun assertIds() {
        PopulationCode.values().map { it.id } shouldContainAll listOf(1.toShort(), 2.toShort())
    }

    @Test
    fun of_withExistingId() {
        PopulationCode.of(1.toShort()).get() shouldBeEqualTo PopulationCode.CHILDREN
    }

    @Test
    fun of_withNotExistingId_returnsEmptyOptional() {
        PopulationCode.of(0.toShort()).isEmpty.shouldBeTrue()
    }
}
