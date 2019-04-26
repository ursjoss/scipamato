package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DbPropertiesTest {

    private final DbProperties dbProperties = new DbProperties();

    @Test
    void schema_hasDefaultValuePublic() {
        assertThat(dbProperties.getSchema()).isEqualTo("public");
    }
}
