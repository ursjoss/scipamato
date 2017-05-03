package ch.difty.sipamato.web.panel.paper;

import org.apache.wicket.model.Model;
import org.junit.Test;

import ch.difty.sipamato.entity.filter.SearchCondition;

public class SearchablePaperPanelTest extends PaperPanelTest<SearchCondition, SearchablePaperPanel> {

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

        getTester().assertInvisible(b + ":pubmedRetrieval");
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
    public void assertSubmit() {
        getTester().startComponentInPage(makePanel());
        getTester().submitForm("panel:form");
        verifyCodeAndCodeClassCalls(2);
    }

}
