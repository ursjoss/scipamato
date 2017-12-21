package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class DbPropertiesTest {

    private final DbProperties dbProperties = new DbProperties();

    @Test
    public void schema_hasDefaultValuePublic() {
        assertThat(dbProperties.getSchema()).isEqualTo("public");
    }
}
