package ch.difty.scipamato.common.entity.filter

import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

internal class ScipamatoFilterTest {

    @Test
    fun canInstantiate() {
        assertThat(ScipamatoFilter()).isNotNull
    }
}