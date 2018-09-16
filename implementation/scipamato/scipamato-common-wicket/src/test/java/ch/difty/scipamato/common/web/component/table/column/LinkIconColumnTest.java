package ch.difty.scipamato.common.web.component.table.column;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.TagTester;
import org.junit.Test;

import ch.difty.scipamato.common.web.TestRecord;
import ch.difty.scipamato.common.web.WicketBaseTest;

public class LinkIconColumnTest extends WicketBaseTest {

    private static final String ID = "panel";

    private String clickPerformed = null;

    private LinkIconColumnTestPanel newPanelWithTitle(String title) {
        return new LinkIconColumnTestPanel(ID, title != null ? Model.of(title) : null) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onClickPerformed(IModel<TestRecord> rowModel, AjaxLink<Void> link) {
                clickPerformed = rowModel
                    .getObject()
                    .toString();
            }
        };
    }

    @Test
    public void testPanel_withTitle() {
        getTester().startComponentInPage(newPanelWithTitle("the title"));
        assertComponents();
        assertImageTitle();
        assertThat(clickPerformed).isNull();
    }

    private void assertComponents() {
        getTester().assertComponent("panel", LinkIconColumnTestPanel.class);
        getTester().assertComponent("panel:table", DefaultDataTable.class);
        getTester().assertComponent("panel:table:body:rows", DataGridView.class);
        getTester().assertLabel("panel:table:body:rows:1:cells:1:cell", "foo");
        getTester().assertComponent("panel:table:body:rows:1:cells:2:cell", LinkIconPanel.class);
        getTester().assertModelValue("panel:table:body:rows:1:cells:2:cell", "fa fa-fw fa-filter");
        getTester().assertLabel("panel:table:topToolbars:toolbars:2:headers:2:header:label", "linkIconColumnLabel");
    }

    private void assertImageTitle() {
        String responseTxt = getTester()
            .getLastResponse()
            .getDocument();
        TagTester tagTester = TagTester.createTagByName(responseTxt, "i");
        assertThat(tagTester).isNotNull();
        assertThat(tagTester.getAttribute("title")).isEqualTo("the title");
    }

    @Test
    public void testPanel_withoutTitle() {
        getTester().startComponentInPage(newPanelWithTitle(null));
        assertComponents();
        assertNoImageTitle();
        assertThat(clickPerformed).isNull();
    }

    private void assertNoImageTitle() {
        String responseTxt = getTester()
            .getLastResponse()
            .getDocument();
        TagTester tagTester = TagTester.createTagByName(responseTxt, "i");
        assertThat(tagTester).isNotNull();
        assertThat(tagTester.getAttribute("title")).isNull();
    }

    @Test
    public void clickingLink() {
        getTester().startComponentInPage(newPanelWithTitle("foo"));
        getTester().clickLink("panel:table:body:rows:1:cells:2:cell:link");
        assertThat(clickPerformed).isEqualTo("TestRecord(1, foo)");
    }

    private final LinkIconColumn<TestRecord> lc = new LinkIconColumn<>(Model.of("headerText")) {
        private static final long serialVersionUID = 1L;

        @Override
        protected IModel<String> createIconModel(IModel<TestRecord> rowModel) {
            return Model.of("the iconModel");
        }

        @Override
        protected void onClickPerformed(AjaxRequestTarget target, IModel<TestRecord> rowModel, AjaxLink<Void> link) {
            clickPerformed = rowModel
                .getObject()
                .toString();
        }
    };

    @Test
    public void cssClassIsNull_canSetLater() {
        assertThat(lc
            .getDisplayModel()
            .getObject()).isEqualTo("headerText");
    }

    @Test
    public void creatingTitleModel_returnsNull() {
        assertThat(lc.createTitleModel(Model.of(new TestRecord(1, "foo")))).isNull();
    }
}
