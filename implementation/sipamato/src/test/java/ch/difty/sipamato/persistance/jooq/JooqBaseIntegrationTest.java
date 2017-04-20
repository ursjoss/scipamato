package ch.difty.sipamato.persistance.jooq;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.sipamato.SipamatoApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SipamatoApplication.class)
// Running the test from maven requires the profile -Dspring.profiles.active=h2|postgres
// Running the test from Eclipse requres to active the appropriate annotation, matching the profile 
// h2|postgres that was used to compile the project with maven
@ActiveProfiles({ "DB_JOOQ", "test", "postgres" })
//@ActiveProfiles({ "DB_JOOQ", "test", "h2" })
public abstract class JooqBaseIntegrationTest {

}
