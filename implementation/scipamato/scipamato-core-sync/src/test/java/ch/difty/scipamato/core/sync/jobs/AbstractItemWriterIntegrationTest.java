package ch.difty.scipamato.core.sync.jobs;

import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ch.difty.scipamato.core.sync.JooqTransactionalIntegrationTest;

public abstract class AbstractItemWriterIntegrationTest<E, W extends ScipamatoItemWriter<E>>
    extends JooqTransactionalIntegrationTest {

    @Autowired
    @Qualifier("publicDslContext")
    protected DSLContext dsl;

    private W writer;

    protected abstract W newWriter();

    protected W getWriter() {
        return writer;
    }

    @BeforeEach
    public final void setUp() {
        writer = newWriter();
        setUpEntities();
    }

    protected void setUpEntities() {
    }

}
