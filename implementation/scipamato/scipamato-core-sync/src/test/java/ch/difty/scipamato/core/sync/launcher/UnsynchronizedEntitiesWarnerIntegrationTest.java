package ch.difty.scipamato.core.sync.launcher;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ch.difty.scipamato.core.sync.JooqBaseIntegrationTest;

public class UnsynchronizedEntitiesWarnerIntegrationTest extends JooqBaseIntegrationTest {

    @Autowired
    @Qualifier("dslContext")
    protected DSLContext dsl;

    private Warner warner;

    @Before
    public void setUp() {
        warner = new UnsynchronizedEntitiesWarner(dsl);
    }

    @Test
    public void findUnsynchronizedPapers() {
        Optional<String> msg = warner.findUnsynchronizedPapers();
        assertThat(msg).isPresent();
        //noinspection OptionalGetWithoutIsPresent
        assertThat(msg.get()).isEqualTo("Papers not synchronized due to missing codes: Number 2, 3, 4, 11.");
    }

}