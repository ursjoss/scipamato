package ch.difty.scipamato.core.persistence;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@JooqTest
@ActiveProfiles({ "test" })
public abstract class JooqBaseIntegrationTest {

}
