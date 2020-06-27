package ch.difty.scipamato.publ.entity

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.extractProperty
import org.junit.jupiter.api.Test

internal class PopulationCodeTest {

    @Test
    fun hasAllValues() {
        assertThat(PopulationCode.values()).containsExactly(PopulationCode.CHILDREN, PopulationCode.ADULTS)
    }

    @Test
    fun assertIds() {
        assertThat(extractProperty("id").from(PopulationCode.values())).containsExactly(1.toShort(), 2.toShort())
    }

    @Test
    fun of_withExistingId() {
        assertThat(PopulationCode.of(1.toShort())).hasValue(PopulationCode.CHILDREN)
    }

    @Test
    fun of_withNotExistingId_returnsEmptyOptional() {
        assertThat(PopulationCode.of(0.toShort())).isEmpty
    }
}
