package ch.difty.scipamato.web.component.table.column;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.scipamato.web.TestRecord;
import ch.difty.scipamato.web.WicketTest;
import ch.difty.scipamato.web.component.SerializableConsumer;

public class ClickablePropertyColumnTest extends WicketTest {

    @Mock
    private SerializableConsumer<IModel<String>> consumerMock;

    private IModel<String> displayModel = new Model<String>("foo");
    private String property = "prop";

    private String clickPerformed = null;

    @Test
    public void testOnClick() {
        ClickablePropertyColumn<String, String> c = new ClickablePropertyColumn<>(displayModel, property, consumerMock);
        Model<String> clickModel = Model.of("bar");
        c.onClick(clickModel);
        verify(consumerMock).accept(clickModel);
    }

    @Test
    public void testPanel() {
        getTester().startComponentInPage(new ClickablePropertyColumnTestPanel("panel", this::setVariable));
        assertComponents();
        assertThat(clickPerformed).isNull();
    }

    @Test
    public void clickLink() {
        getTester().startComponentInPage(new ClickablePropertyColumnTestPanel("panel", this::setVariable));
        getTester().clickLink("panel:table:body:rows:1:cells:2:cell:link");
        assertThat(clickPerformed).isEqualTo("TestRecord(1, foo)");
    }

    private void setVariable(IModel<TestRecord> trModel) {
        clickPerformed = trModel.getObject().toString();
    }

    private void assertComponents() {
        getTester().assertComponent("panel", ClickablePropertyColumnTestPanel.class);
        getTester().assertComponent("panel:table", DefaultDataTable.class);
        getTester().assertComponent("panel:table:body:rows", DataGridView.class);
        getTester().assertLabel("panel:table:body:rows:1:cells:1:cell", "foo");
        getTester().assertComponent("panel:table:body:rows:1:cells:2:cell", Panel.class);
        getTester().assertComponent("panel:table:body:rows:1:cells:2:cell:link", Link.class);
        getTester().assertModelValue("panel:table:body:rows:1:cells:2:cell:link", new TestRecord(1, "foo"));
        getTester().assertModelValue("panel:table:body:rows:1:cells:2:cell:link:label", "foo");
    }

}
