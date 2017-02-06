package ch.difty.sipamato.web.panel.paper;

import java.time.LocalDateTime;

import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.Model;
import org.junit.Test;

import ch.difty.sipamato.entity.Paper;

public class EditablePaperPanelTest extends PaperPanelTest<Paper, EditablePaperPanel> {

    @Override
    protected EditablePaperPanel makePanel() {
        Paper p = new Paper();
        p.setId(1l);
        p.setAuthors("a");
        p.setFirstAuthor("fa");
        p.setFirstAuthorOverridden(false);

        p.setTitle("t");
        p.setLocation("l");
        p.setPublicationYear(2017);

        p.setPmId(1234);
        p.setDoi("doi");
        p.setCreated(LocalDateTime.parse("2017-02-01T13:34:45"));
        p.setCreatedByName("u1");
        p.setLastModified(LocalDateTime.parse("2017-03-01T13:34:45"));
        p.setLastModifiedByName("u2");

        p.setGoals("g");
        p.setPopulation("p");
        p.setMethods("m");

        p.setPopulationPlace("ppl");
        p.setPopulationParticipants("ppa");
        p.setPopulationDuration("pd");
        p.setExposurePollutant("ep");
        p.setExposureAssessment("ea");
        p.setMethodStudyDesign("msd");
        p.setMethodOutcome("mo");
        p.setMethodStatistics("ms");
        p.setMethodConfounders("mc");

        p.setResult("r");
        p.setComment("c");
        p.setIntern("i");
        p.setResultMeasuredOutcome("rmo");
        p.setResultExposureRange("rer");
        p.setResultEffectEstimate("ree");

        p.addCode(newC(1, "F"));
        p.setMainCodeOfCodeclass1("mcocc1");
        p.addCode(newC(2, "A"));
        p.addCode(newC(3, "A"));
        p.addCode(newC(4, "A"));
        p.addCode(newC(5, "A"));
        p.addCode(newC(6, "A"));
        p.addCode(newC(7, "A"));
        p.addCode(newC(8, "A"));

        p.setOriginalAbstract("oa");

        return new EditablePaperPanel("panel", Model.of(p)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onFormSubmit() {
            }
        };
    }

    @Override
    protected void assertSpecificComponents() {
        String b = "panel";
        getTester().assertComponent(b, EditablePaperPanel.class);

        assertCommonComponents(b);

        b += ":form";
        assertTextFieldWithLabel(b + ":id", 1l, "ID");
        assertTextFieldWithLabel(b + ":publicationYear", 2017, "Pub. Year");
        assertTextFieldWithLabel(b + ":pmId", 1234, "PMID");
        getTester().assertLabel(b + ":submit:label", "Save");
        assertTextFieldWithLabel(b + ":createdDisplayValue", "u1 (2017-02-01 13:34:45)", "Created");
        assertTextFieldWithLabel(b + ":modifiedDisplayValue", "u2 (2017-03-01 13:34:45)", "Last Modified");
    }

    @Test
    public void specificFields_areDisabled() {
        getTester().startComponentInPage(makePanel());
        getTester().isDisabled("panel:form:id");
        getTester().isDisabled("panel:form:firstAuthorOverridden");
        getTester().isDisabled("panel:form:createdDisplayValue");
        getTester().isDisabled("panel:form:modifiedDisplayValue");
        verifyCodeAndCodeClassCalls(1);
    }

    @Test
    public void assertSummaryLink() {
        getTester().startComponentInPage(makePanel());
        getTester().assertComponent("panel:form:summaryLink", ResourceLink.class);
        verifyCodeAndCodeClassCalls(1);
    }

    @Test
    public void assertSubmit() {
        getTester().startComponentInPage(makePanel());
        getTester().submitForm("panel:form");
        verifyCodeAndCodeClassCalls(2);
    }
}
