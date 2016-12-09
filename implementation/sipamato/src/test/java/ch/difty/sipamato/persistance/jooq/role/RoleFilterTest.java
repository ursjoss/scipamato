package ch.difty.sipamato.persistance.jooq.role;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class RoleFilterTest {

    @Test
    public void test() {
        RoleFilter f = new RoleFilter();
        f.setNameMask("name");
        f.setCommentMask("comment");

        assertThat(f.getNameMask()).isEqualTo("name");
        assertThat(f.getCommentMask()).isEqualTo("comment");
    }
}
