package ch.difty.sipamato.persistance.jooq;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.sipamato.SipamatoApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SipamatoApplication.class)
@ActiveProfiles({ "DB_JOOQ", "test" })
public abstract class JooqBaseIntegrationTest {

}
