package ch.difty.scipamato.core.sync;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.core.io.ResourceLoader;

@ExtendWith(MockitoExtension.class)
public class BatchConfigurationTest {

    @Mock
    private BatchProperties batchProperties;

    @Mock
    private DataSource dataSource;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private DSLContext jooqCore;

    @Test
    public void instantiate() {
        BatchConfiguration bc = new BatchConfiguration(batchProperties);
        assertThat(bc.batchDataSourceInitializer(dataSource, resourceLoader)).isNotNull();
        verifyNoMoreInteractions(batchProperties, dataSource, resourceLoader, jooqCore);
    }
}