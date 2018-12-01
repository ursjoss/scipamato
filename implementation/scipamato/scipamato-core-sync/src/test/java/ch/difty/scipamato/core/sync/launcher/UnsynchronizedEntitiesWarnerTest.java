package ch.difty.scipamato.core.sync.launcher;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.common.TestUtils;

@RunWith(MockitoJUnitRunner.class)
public class UnsynchronizedEntitiesWarnerTest {

    private UnsynchronizedEntitiesWarner warner;

    @Mock
    private DSLContext jooqCore;

    @Before
    public void setUp() {
        warner = new UnsynchronizedEntitiesWarner(jooqCore);
    }

    @Test
    public void degenerateConstruction() {
        TestUtils.assertDegenerateSupplierParameter(() -> new UnsynchronizedEntitiesWarner(null), "jooqCore");
    }

}