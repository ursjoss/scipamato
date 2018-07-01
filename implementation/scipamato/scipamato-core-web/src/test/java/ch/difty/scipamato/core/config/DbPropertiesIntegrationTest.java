package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.scipamato.core.ScipamatoCoreApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScipamatoCoreApplication.class)
public class DbPropertiesIntegrationTest {

    @Autowired
    private DbProperties dbProperties;

    @Test
    public void schema_hasDefaultValuePublic() {
        assertThat(dbProperties.getSchema()).isEqualTo("public");
    }
}
