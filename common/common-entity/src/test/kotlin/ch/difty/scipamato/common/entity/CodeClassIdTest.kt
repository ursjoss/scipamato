package ch.difty.scipamato.common.entity

import ch.difty.scipamato.common.entity.CodeClassId.CC1
import ch.difty.scipamato.common.entity.CodeClassId.CC2
import ch.difty.scipamato.common.entity.CodeClassId.CC3
import ch.difty.scipamato.common.entity.CodeClassId.CC4
import ch.difty.scipamato.common.entity.CodeClassId.CC5
import ch.difty.scipamato.common.entity.CodeClassId.CC6
import ch.difty.scipamato.common.entity.CodeClassId.CC7
import ch.difty.scipamato.common.entity.CodeClassId.CC8
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

class CodeClassIdTest {

    @Test
    fun values() {
        CodeClassId.entries shouldContainSame listOf(CC1, CC2, CC3, CC4, CC5, CC6, CC7, CC8)
    }

    @Test
    fun fromId_whenPresent() {
        CodeClassId.fromId(1).isPresent.shouldBeTrue()
        CodeClassId.fromId(1).get() shouldBeEqualTo CC1
        CodeClassId.fromId(2).get() shouldBeEqualTo CC2
        CodeClassId.fromId(3).get() shouldBeEqualTo CC3
        CodeClassId.fromId(4).get() shouldBeEqualTo CC4
        CodeClassId.fromId(5).get() shouldBeEqualTo CC5
        CodeClassId.fromId(6).get() shouldBeEqualTo CC6
        CodeClassId.fromId(7).get() shouldBeEqualTo CC7
        CodeClassId.fromId(8).get() shouldBeEqualTo CC8
    }

    @Test
    fun fromId_whenNotPresent() {
        CodeClassId.fromId(-1).isPresent.shouldBeFalse()
    }

    @Test
    fun getId() {
        CC1.id shouldBeEqualTo 1
    }
}
