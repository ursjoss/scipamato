package ch.difty.scipamato.core.sync.launcher;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ch.difty.scipamato.core.sync.JooqBaseIntegrationTest;

class UnsynchronizedEntitiesWarnerIntegrationTest extends JooqBaseIntegrationTest {

    @Autowired
    @Qualifier("dslContext")
    protected DSLContext dsl;

    private Warner warner;

    @BeforeEach
    void setUp() {
        warner = new UnsynchronizedEntitiesWarner(dsl);
    }

    @Test
    void findUnsynchronizedPapers() {
        Optional<String> msg = warner.findUnsynchronizedPapers();
        assertThat(msg).isPresent();
        //noinspection OptionalGetWithoutIsPresent
        assertThat(msg.get()).isEqualTo("Papers not synchronized due to missing codes: Number 2, 3, 4, 11.");
    }

}