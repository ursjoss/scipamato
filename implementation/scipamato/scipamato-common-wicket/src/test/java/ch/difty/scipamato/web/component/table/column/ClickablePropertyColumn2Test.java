package ch.difty.scipamato.web.component.table.column;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.scipamato.web.component.SerializableBiConsumer;
import ch.difty.scipamato.web.component.SerializableSupplier;

@RunWith(MockitoJUnitRunner.class)
public class ClickablePropertyColumn2Test {

    @Mock
    private SerializableBiConsumer<IModel<String>, Integer> biConsumerMock;
    @Mock
    private SerializableSupplier<Integer> supplierMock;

    private IModel<String> displayModel = new Model<String>("foo");
    private String sort = "sort";
    private String property = "prop";

    private ClickablePropertyColumn2<String, String, Integer> c;
    private final int suppliedValue = 5;
    Model<String> clickModel = Model.of("bar");

    @Test
    public void testOnClick_withSortProperty() {
        when(supplierMock.get()).thenReturn(suppliedValue);

        c = new ClickablePropertyColumn2<>(displayModel, sort, property, biConsumerMock, supplierMock);
        c.onClick(clickModel);

        verify(supplierMock).get();
        verify(biConsumerMock).accept(clickModel, suppliedValue);
    }

    @Test
    public void testOnClick_withoutSortProperty() {
        when(supplierMock.get()).thenReturn(suppliedValue);

        c = new ClickablePropertyColumn2<>(displayModel, property, biConsumerMock, supplierMock);
        c.onClick(clickModel);

        verify(supplierMock).get();
        verify(biConsumerMock).accept(clickModel, suppliedValue);
    }
    
    @Test
    public void gettingProperty() {
        c = new ClickablePropertyColumn2<>(displayModel, property, biConsumerMock, supplierMock);
        assertThat(c.getProperty()).isEqualTo(property);
    }

}
