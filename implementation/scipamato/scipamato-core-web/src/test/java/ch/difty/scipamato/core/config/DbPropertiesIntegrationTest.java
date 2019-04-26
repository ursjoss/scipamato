package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ch.difty.scipamato.core.ScipamatoCoreApplication;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ScipamatoCoreApplication.class)
class DbPropertiesIntegrationTest {

    @Autowired
    private DbProperties dbProperties;

    @Test
    void schema_hasDefaultValuePublic() {
        assertThat(dbProperties.getSchema()).isEqualTo("public");
    }
}
