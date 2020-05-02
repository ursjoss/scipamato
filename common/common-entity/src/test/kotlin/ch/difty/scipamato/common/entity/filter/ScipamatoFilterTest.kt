package ch.difty.scipamato.common.entity.filter

import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class ScipamatoFilterTest {

    @Test
    fun canInstantiate() {
        ScipamatoFilter().shouldNotBeNull()
    }
}
