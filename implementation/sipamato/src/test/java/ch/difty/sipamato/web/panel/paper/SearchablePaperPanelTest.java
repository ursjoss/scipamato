package ch.difty.sipamato.web.panel.paper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.paging.PaginationContext;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.sipamato.service.PaperService;

public class SearchablePaperPanelTest extends PaperPanelTest<SearchCondition, SearchablePaperPanel> {

    @MockBean
    private PaperService paperServiceMock;

    @Override
    protected SearchablePaperPanel makePanel() {
        SearchCondition sc = new SearchCondition();
        sc.setId("1");
        sc.setNumber("100");
        sc.setAuthors("a");
        sc.setFirstAuthor("fa");
        sc.setFirstAuthorOverridden(false);

        sc.setTitle("t");
        sc.setLocation("l");
        sc.setPublicationYear("2017");

        sc.setPmId("pmid");
        sc.setDoi("doi");
        sc.setCreatedDisplayValue("cdv");
        sc.setModifiedDisplayValue("lmdv");

        sc.setGoals("g");
        sc.setPopulation("p");
        sc.setMethods("m");

        sc.setPopulationPlace("ppl");
        sc.setPopulationParticipants("ppa");
        sc.setPopulationDuration("pd");
        sc.setExposurePollutant("ep");
        sc.setExposureAssessment("ea");
        sc.setMethodStudyDesign("msd");
        sc.setMethodOutcome("mo");
        sc.setMethodStatistics("ms");
        sc.setMethodConfounders("mc");

        sc.setResult("r");
        sc.setComment("c");
        sc.setIntern("i");
        sc.setResultMeasuredOutcome("rmo");
        sc.setResultExposureRange("rer");
        sc.setResultEffectEstimate("ree");

        sc.addCode(newC(1, "F"));
        sc.setMainCodeOfCodeclass1("mcocc1");
        sc.addCode(newC(2, "A"));
        sc.addCode(newC(3, "A"));
        sc.addCode(newC(4, "A"));
        sc.addCode(newC(5, "A"));
        sc.addCode(newC(6, "A"));
        sc.addCode(newC(7, "A"));
        sc.addCode(newC(8, "A"));

        sc.setOriginalAbstract("oa");

        return new SearchablePaperPanel("panel", Model.of(sc)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onFormSubmit() {
                // no-op
            }
        };
    }

    @Override
    protected void tearDownLocalHook() {
        verify(paperServiceMock).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
        verifyNoMoreInteractions(paperServiceMock);
    }

    @Override
    protected void assertSpecificComponents() {
        String b = "panel";
        getTester().assertComponent(b, SearchablePaperPanel.class);

        assertCommonComponents(b);

        b += ":form";
        assertTextFieldWithLabel(b + ":id", "1", "ID");
        assertTextFieldWithLabel(b + ":number", "100", "No.");
        assertTextFieldWithLabel(b + ":publicationYear", "2017", "Pub. Year");
        assertTextFieldWithLabel(b + ":pmId", "pmid", "PMID");
        getTester().assertLabel(b + ":submit:label", "Search");
        assertTextFieldWithLabel(b + ":createdDisplayValue", "cdv", "Created");
        assertTextFieldWithLabel(b + ":modifiedDisplayValue", "lmdv", "Last Modified");

        String bb = b + ":tabs:panelsContainer:panels:11:tab6Form";
        getTester().assertInvisible(bb + ":bootstrapFileinput");
        getTester().assertComponent(bb, Form.class);
    }

    @Test
    public void specificFields_areEnabled() {
        getTester().startComponentInPage(makePanel());
        getTester().isEnabled("panel:form:id");
        getTester().isEnabled("panel:form:number");
        getTester().isEnabled("panel:form:firstAuthorOverridden");
        getTester().isEnabled("panel:form:createdDisplayValue");
        getTester().isEnabled("panel:form:modifiedDisplayValue");
        verifyCodeAndCodeClassCalls(1);
    }

    @Test
    public void summaryLink_doesNotExist() {
        getTester().startComponentInPage(makePanel());
        getTester().assertContainsNot("panel:form:summaryLink");
        verifyCodeAndCodeClassCalls(1);
    }

    @Test
    public void navigationButtons_andPubmedRetrieval_andBackButton_areInvisible() {
        getTester().startComponentInPage(makePanel());

        getTester().assertInvisible("panel:form:previous");
        getTester().assertInvisible("panel:form:next");

        getTester().assertInvisible("panel:form:pubmedRetrieval");

        getTester().assertInvisible("panel:form:back");

        verifyCodeAndCodeClassCalls(1);
    }

    @Test
    public void assertSubmit() {
        getTester().startComponentInPage(makePanel());
        applyTestHackWithNstedMultiPartForms();
        getTester().submitForm("panel:form");
        verifyCodeAndCodeClassCalls(2);
    }

    @Test
    public void gettingCallingPage_isNull() {
        SearchablePaperPanel panel = getTester().startComponentInPage(makePanel());
        assertThat(panel.getCallingPage()).isNull();
        verifyCodeAndCodeClassCalls(1);
    }

}
