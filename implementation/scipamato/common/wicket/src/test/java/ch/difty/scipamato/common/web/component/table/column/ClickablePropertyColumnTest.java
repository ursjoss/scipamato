package ch.difty.scipamato.common.web.component.table.column;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import ch.difty.scipamato.common.web.TestRecord;
import ch.difty.scipamato.common.web.WicketBaseTest;
import ch.difty.scipamato.common.web.component.SerializableConsumer;

class ClickablePropertyColumnTest extends WicketBaseTest {

    @Mock
    private SerializableConsumer<IModel<String>> consumerMock;

    private final IModel<String> displayModel = new Model<>("foo");

    private String clickPerformed = null;

    @Test
    void testOnClick() {
        String property = "prop";
        ClickablePropertyColumn<String, String> c = new ClickablePropertyColumn<>(displayModel, property, consumerMock);
        Model<String> clickModel = Model.of("bar");
        c.onClick(clickModel);
        verify(consumerMock).accept(clickModel);
    }

    @Test
    void testOnClick_withSort() {
        String property = "prop";
        ClickablePropertyColumn<String, String> c = new ClickablePropertyColumn<>(displayModel, property, property,
            consumerMock);
        Model<String> clickModel = Model.of("bar");
        c.onClick(clickModel);
        verify(consumerMock).accept(clickModel);
    }

    @Test
    void testOnClick_inNewTab() {
        String property = "prop";
        ClickablePropertyColumn<String, String> c = new ClickablePropertyColumn<>(displayModel, property, property,
            consumerMock, true);
        Model<String> clickModel = Model.of("bar");
        c.onClick(clickModel);
        verify(consumerMock).accept(clickModel);
    }

    @Test
    void testPanel() {
        getTester().startComponentInPage(new ClickablePropertyColumnTestPanel("panel", this::setVariable, false));
        assertComponents();
        assertThat(clickPerformed).isNull();
    }

    @Test
    void clickLink() {
        getTester().startComponentInPage(new ClickablePropertyColumnTestPanel("panel", this::setVariable, false));
        getTester().clickLink("panel:table:body:rows:1:cells:2:cell:link");
        assertThat(clickPerformed).isEqualTo("TestRecord(1, foo)");
    }

    @Test
    void clickLink_inNewTab() {
        getTester().startComponentInPage(new ClickablePropertyColumnTestPanel("panel", this::setVariable, true));
        getTester().clickLink("panel:table:body:rows:1:cells:2:cell:link");
        assertThat(clickPerformed).isEqualTo("TestRecord(1, foo)");
    }

    private void setVariable(IModel<TestRecord> trModel) {
        clickPerformed = trModel
            .getObject()
            .toString();
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
