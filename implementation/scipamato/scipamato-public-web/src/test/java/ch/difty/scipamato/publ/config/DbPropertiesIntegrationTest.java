package ch.difty.scipamato.publ.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ch.difty.scipamato.publ.ScipamatoPublicApplication;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ScipamatoPublicApplication.class)
public class DbPropertiesIntegrationTest {

    @Autowired
    private DbProperties dbProperties;

    @Test
    public void schema_hasValuePublic() {
        assertThat(dbProperties.getSchema()).isEqualTo("public");
    }
}
