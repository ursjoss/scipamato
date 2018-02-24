package ch.difty.scipamato.publ.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.scipamato.publ.ScipamatoPublicApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScipamatoPublicApplication.class)
public class DbPropertiesIntegrationTest {

    @Autowired
    private DbProperties dbProperties;

    @Test
    public void schema_hasValuePublic() {
        assertThat(dbProperties.getSchema()).isEqualTo("public");
    }
}
