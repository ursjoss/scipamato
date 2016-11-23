package ch.difty.sipamato.web.pages.paper.search;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperSlimService;
import ch.difty.sipamato.web.pages.BasePageTest;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;

public class PaperSearchPageTest extends BasePageTest<PaperSearchPage> {

    @MockBean
    private PaperSlimService mockPaperSlimService;

    @Mock
    private Paper mockPaper;

    private final List<PaperSlim> papers = Arrays.asList(new PaperSlim(1l, null, null, null), new PaperSlim(2l, null, null, null));

    @Override
    protected PaperSearchPage makePage() {
        return new PaperSearchPage(new PageParameters());
    }

    @Override
    protected Class<PaperSearchPage> getPageClass() {
        return PaperSearchPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        String b = "form";
        getTester().assertComponent(b, Form.class);
        getTester().assertComponent(b + ":addSearch", BootstrapAjaxButton.class);
    }

    @Test
    public void clickingAddSearch_forwardsToPaperSearchCriteriaPage() {
        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("addSearch");

        getTester().assertRenderedPage(PaperSearchCriteriaPage.class);
    }

    @Test
    public void startingPageWithDefaultConstructor_initiatesPageWith0Papers() {
        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());
        getTester().assertComponent("form:paperCount", Label.class);
        getTester().assertModelValue("form:paperCount", 0);
    }

    @Test
    public void startingPageWithModelConstructor_withTwoPaperSlims_initiatesPageWith2Papers() {
        final Map<Long, PaperSlim> map = papers.stream().collect(Collectors.toMap(PaperSlim::getId, Function.identity()));
        getTester().startPage(new PaperSearchPage(Model.ofMap(map)));
        getTester().assertRenderedPage(getPageClass());
        getTester().assertComponent("form:paperCount", Label.class);
        getTester().assertModelValue("form:paperCount", 2);
    }

}
