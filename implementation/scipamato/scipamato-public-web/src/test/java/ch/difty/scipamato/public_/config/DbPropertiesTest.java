package ch.difty.scipamato.public_.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.public_.config.DbProperties;

public class DbPropertiesTest {

    private final DbProperties dbProperties = new DbProperties();

    @Test
    public void schema_hasDefaultValuePublic() {
        assertThat(dbProperties.getSchema()).isEqualTo("public");
    }
}
