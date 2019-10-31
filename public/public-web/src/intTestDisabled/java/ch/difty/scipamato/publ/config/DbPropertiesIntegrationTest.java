package ch.difty.scipamato.publ.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.difty.scipamato.publ.ScipamatoPublicApplication;

@SpringBootTest(classes = ScipamatoPublicApplication.class)
class DbPropertiesIntegrationTest {

    @Autowired
    private DbProperties dbProperties;

    @Test
    void schema_hasValuePublic() {
        assertThat(dbProperties.getSchema()).isEqualTo("public");
    }
}
