package ch.difty.scipamato.persistance;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.scipamato.ScipamatoApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScipamatoApplication.class)
@ActiveProfiles({ "DB_JOOQ", "test" })
public abstract class JooqBaseIntegrationTest {

}
