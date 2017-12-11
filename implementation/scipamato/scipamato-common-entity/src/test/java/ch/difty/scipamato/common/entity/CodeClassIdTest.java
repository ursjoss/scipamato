package ch.difty.scipamato.common.entity;

import static ch.difty.scipamato.common.entity.CodeClassId.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class CodeClassIdTest {

    @Test
    public void values() {
        assertThat(CodeClassId.values()).containsExactly(CC1, CC2, CC3, CC4, CC5, CC6, CC7, CC8);
    }

    @Test
    public void fromId_whenPresent() {
        assertThat(CodeClassId.fromId(1).isPresent()).isTrue();
        assertThat(CodeClassId.fromId(1).get()).isEqualTo(CC1);
        assertThat(CodeClassId.fromId(2).get()).isEqualTo(CC2);
        assertThat(CodeClassId.fromId(3).get()).isEqualTo(CC3);
        assertThat(CodeClassId.fromId(4).get()).isEqualTo(CC4);
        assertThat(CodeClassId.fromId(5).get()).isEqualTo(CC5);
        assertThat(CodeClassId.fromId(6).get()).isEqualTo(CC6);
        assertThat(CodeClassId.fromId(7).get()).isEqualTo(CC7);
        assertThat(CodeClassId.fromId(8).get()).isEqualTo(CC8);
    }

    @Test
    public void fromId_whenNotPresent() {
        assertThat(CodeClassId.fromId(-1).isPresent()).isFalse();
    }

    @Test
    public void getId() {
        assertThat(CodeClassId.CC1.getId()).isEqualTo(1);
    }

}
