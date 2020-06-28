package ch.difty.scipamato.common.entity

import ch.difty.scipamato.common.entity.CodeClassId.CC1
import ch.difty.scipamato.common.entity.CodeClassId.CC2
import ch.difty.scipamato.common.entity.CodeClassId.CC3
import ch.difty.scipamato.common.entity.CodeClassId.CC4
import ch.difty.scipamato.common.entity.CodeClassId.CC5
import ch.difty.scipamato.common.entity.CodeClassId.CC6
import ch.difty.scipamato.common.entity.CodeClassId.CC7
import ch.difty.scipamato.common.entity.CodeClassId.CC8
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CodeClassIdTest {

    @Test
    fun values() {
        assertThat(CodeClassId.values()).containsExactly(CC1, CC2, CC3, CC4, CC5, CC6, CC7, CC8)
    }

    @Test
    fun fromId_whenPresent() {
        assertThat(CodeClassId.fromId(1).isPresent).isTrue()
        assertThat(CodeClassId.fromId(1).get()).isEqualTo(CC1)
        assertThat(CodeClassId.fromId(2).get()).isEqualTo(CC2)
        assertThat(CodeClassId.fromId(3).get()).isEqualTo(CC3)
        assertThat(CodeClassId.fromId(4).get()).isEqualTo(CC4)
        assertThat(CodeClassId.fromId(5).get()).isEqualTo(CC5)
        assertThat(CodeClassId.fromId(6).get()).isEqualTo(CC6)
        assertThat(CodeClassId.fromId(7).get()).isEqualTo(CC7)
        assertThat(CodeClassId.fromId(8).get()).isEqualTo(CC8)
    }

    @Test
    fun fromId_whenNotPresent() {
        assertThat(CodeClassId.fromId(-1).isPresent).isFalse()
    }

    @Test
    fun getId() {
        assertThat(CC1.id).isEqualTo(1)
    }
}
