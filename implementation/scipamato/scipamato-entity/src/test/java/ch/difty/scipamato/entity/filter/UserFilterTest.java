package ch.difty.scipamato.entity.filter;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import ch.difty.scipamato.entity.filter.UserFilter;

public class UserFilterTest {

    @Test
    public void test() {
        UserFilter f = new UserFilter();
        f.setNameMask("name");
        f.setEmailMask("email");
        f.setEnabled(true);

        assertThat(f.getNameMask()).isEqualTo("name");
        assertThat(f.getEmailMask()).isEqualTo("email");
        assertThat(f.getEnabled()).isEqualTo(true);
    }
}
