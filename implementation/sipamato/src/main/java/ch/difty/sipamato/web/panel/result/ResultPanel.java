package ch.difty.sipamato.web.panel.result;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SipamatoFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.web.component.SerializableConsumer;
import ch.difty.sipamato.web.component.table.column.ClickablePropertyColumn;
import ch.difty.sipamato.web.pages.paper.entry.PaperEntryPage;
import ch.difty.sipamato.web.pages.paper.provider.SortablePaperSlimProvider;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

public class ResultPanel extends GenericPanel<Void> {
    private static final long serialVersionUID = 1L;

    @SpringBean
    private PaperService paperService;

    private final SortablePaperSlimProvider<? extends SipamatoFilter> dataProvider;

    public ResultPanel(String id, SortablePaperSlimProvider<? extends SipamatoFilter> dataProvider) {
        super(id);
        this.dataProvider = dataProvider;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final DataTable<PaperSlim, String> table = new BootstrapDefaultDataTable<>("table", makeTableColumns(), dataProvider, 20);
        table.setOutputMarkupId(true);
        table.add(new TableBehavior().striped().hover());
        queue(table);

    }

    private List<IColumn<PaperSlim, String>> makeTableColumns() {
        final List<IColumn<PaperSlim, String>> columns = new ArrayList<>();
        columns.add(makePropertyColumn(Paper.ID, Paper.FLD_ID));
        columns.add(makePropertyColumn(Paper.FIRST_AUTHOR, Paper.FLD_FIRST_AUTHOR));
        columns.add(makePropertyColumn(Paper.PUBL_YEAR, Paper.FLD_PUBL_YEAR));
        columns.add(makeClickableColumn(Paper.TITLE, Paper.FLD_TITLE,
                (IModel<PaperSlim> m) -> setResponsePage(new PaperEntryPage(Model.of(paperService.findById(m.getObject().getId()).orElse(new Paper()))))));
        return columns;
    }

    private PropertyColumn<PaperSlim, String> makePropertyColumn(String propExpression, String sortProperty) {
        return new PropertyColumn<PaperSlim, String>(new StringResourceModel("column.header." + propExpression, this, null), sortProperty, propExpression);
    }

    private ClickablePropertyColumn<PaperSlim, String> makeClickableColumn(String propExpression, String sortProperty, SerializableConsumer<IModel<PaperSlim>> consumer) {
        return new ClickablePropertyColumn<PaperSlim, String>(new StringResourceModel("column.header." + propExpression, this, null), sortProperty, propExpression, consumer);
    }
}
