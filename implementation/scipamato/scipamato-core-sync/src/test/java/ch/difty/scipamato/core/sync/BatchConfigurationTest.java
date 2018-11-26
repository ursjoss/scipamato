package ch.difty.scipamato.core.sync;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.core.io.ResourceLoader;

@RunWith(MockitoJUnitRunner.class)
public class BatchConfigurationTest {

    @Mock
    private BatchProperties batchProperties;

    @Mock
    private DataSource dataSource;

    @Mock
    private ResourceLoader resourceLoader;

    @Test
    public void instantiate() {
        BatchConfiguration bc = new BatchConfiguration(batchProperties);
        BatchDataSourceInitializer bdsi = bc.batchDataSourceInitializer(dataSource, resourceLoader);

        assertThat(bdsi).isNotNull();

        Mockito.verifyNoMoreInteractions(batchProperties, dataSource, resourceLoader);
    }
}