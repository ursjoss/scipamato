package ch.difty.scipamato.core.persistence;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@JooqTest
@ActiveProfiles({ "test" })
public abstract class JooqBaseIntegrationTest {

}
