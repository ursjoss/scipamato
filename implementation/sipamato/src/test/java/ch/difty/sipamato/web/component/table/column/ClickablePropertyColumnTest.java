package ch.difty.sipamato.web.component.table.column;

import static org.mockito.Mockito.verify;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.web.component.SerializableConsumer;

@RunWith(MockitoJUnitRunner.class)
public class ClickablePropertyColumnTest {

    @Mock
    private SerializableConsumer<IModel<String>> consumerMock;

    private IModel<String> displayModel = new Model<String>("foo");
    private String property = "prop";

    @Test
    public void testOnClick() {
        ClickablePropertyColumn<String, String> c = new ClickablePropertyColumn<>(displayModel, property, consumerMock);
        Model<String> clickModel = Model.of("bar");
        c.onClick(clickModel);

        verify(consumerMock).accept(clickModel);
    }

}
