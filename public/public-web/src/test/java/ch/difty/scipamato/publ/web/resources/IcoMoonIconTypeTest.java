package ch.difty.scipamato.publ.web.resources;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class IcoMoonIconTypeTest {

    @Test
    void arrow_right() {
        assertThat(IcoMoonIconType.arrow_right.cssClassName()).isEqualTo("icon-arrow-right");
    }

    @Test
    void link() {
        assertThat(IcoMoonIconType.link.cssClassName()).isEqualTo("icon-link");
    }
}