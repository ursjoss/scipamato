package ch.difty.scipamato.web.pages.portal;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import ch.difty.scipamato.web.filter.PaperFilter;
import ch.difty.scipamato.web.pages.BasePage;
import ch.difty.scipamato.web.provider.PaperProvider;

@MountPath("/")
@WicketHomePage
public class PublicPage extends BasePage<Void> {

    private static final long serialVersionUID = 1L;

    private static final int RESULT_PAGE_SIZE = 20;

    private PaperFilter filter;
    private PaperProvider dataProvider;

    public PublicPage(PageParameters parameters) {
        super(parameters);
        initFilterAndProvider();
    }

    private void initFilterAndProvider() {
        filter = new PaperFilter();
        dataProvider = new PaperProvider(filter, RESULT_PAGE_SIZE);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        makeAndQueueFilterForm("searchForm");
    }

    private void makeAndQueueFilterForm(final String id) {
        queue(new FilterForm<PaperFilter>(id, dataProvider) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                super.onSubmit();
                System.out.println("whooo - submitted");
            }
        });

        queueFieldAndLabel(new TextField<String>("methodsSearch", PropertyModel.of(filter, PaperFilter.METHODS_MASK)));
        queueFieldAndLabel(new TextField<String>("authorsSearch", PropertyModel.of(filter, PaperFilter.AUTHOR_MASK)));
        queueFieldAndLabel(new TextField<String>("pubYearFrom", PropertyModel.of(filter, PaperFilter.PUB_YEAR_FROM)));
        queueFieldAndLabel(new TextField<String>("pubYearUntil", PropertyModel.of(filter, PaperFilter.PUB_YEAR_UNTIL)));
        queueFieldAndLabel(new TextField<String>("number", PropertyModel.of(filter, PaperFilter.NUMBER)));
    }
}
