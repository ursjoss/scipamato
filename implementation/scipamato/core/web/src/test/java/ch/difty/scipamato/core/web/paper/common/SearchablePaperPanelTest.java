package ch.difty.scipamato.core.web.paper.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.Model;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.entity.search.SearchCondition;

@SuppressWarnings("SpellCheckingInspection")
class SearchablePaperPanelTest extends PaperPanelTest<SearchCondition, SearchablePaperPanel> {

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
        sc.setIntern("i");
        sc.setResultMeasuredOutcome("rmo");
        sc.setResultExposureRange("rer");
        sc.setResultEffectEstimate("ree");
        sc.setConclusion("cc");
        sc.setComment("c");

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

            @Override
            protected void restartSearchInPaperSearchPage() {
                // no-op
            }

            @Override
            protected void doOnSubmit() {
                // no-op
            }
        };
    }

    @Override
    protected void tearDownLocalHook() {
        verify(paperServiceMock, times(2)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationContext.class));
        verifyNoMoreInteractions(paperServiceMock);
    }

    @Override
    protected void assertSpecificComponents() {
        String b = "panel";
        getTester().assertComponent(b, SearchablePaperPanel.class);

        assertCommonComponents(b);

        b += ":form";
        assertTextFieldWithLabel(b + ":id", "1", "ID");
        assertTextFieldWithLabel(b + ":number", "100", "SciPaMaTo-Core-No.");
        assertTextFieldWithLabel(b + ":publicationYear", "2017", "Pub. Year");
        assertTextFieldWithLabel(b + ":pmId", "pmid", "PMID");
        getTester().assertLabel(b + ":submit:label", "Search");
        assertTextFieldWithLabel(b + ":createdDisplayValue", "cdv", "Created");
        assertTextFieldWithLabel(b + ":modifiedDisplayValue", "lmdv", "Last Modified");

        getTester().assertComponent(b + ":submit", BootstrapButton.class);
        verifyCodeAndCodeClassCalls(1);
    }

    @Test
    void specificFields_areEnabled() {
        getTester().startComponentInPage(makePanel());
        getTester().isEnabled("panel:form:id");
        getTester().isEnabled("panel:form:number");
        getTester().isEnabled("panel:form:firstAuthorOverridden");
        getTester().isEnabled("panel:form:createdDisplayValue");
        getTester().isEnabled("panel:form:modifiedDisplayValue");
    }

    @Test
    void summary_doesNotExist() {
        getTester().startComponentInPage(makePanel());
        getTester().assertContainsNot("panel:form:summary");
    }

    @Test
    void summaryShort_doesNotExist() {
        getTester().startComponentInPage(makePanel());
        getTester().assertContainsNot("panel:form:summaryShort");
    }

    @Test
    void navigationButtons_andPubmedRetrieval_andBackButton_areInvisible() {
        getTester().startComponentInPage(makePanel());

        getTester().assertInvisible("panel:form:previous");
        getTester().assertInvisible("panel:form:next");

        getTester().assertInvisible("panel:form:pubmedRetrieval");

        getTester().assertInvisible("panel:form:back");
    }

    @Test
    void assertSubmit() {
        getTester().startComponentInPage(makePanel());
        applyTestHackWithNestedMultiPartForms();
        getTester().submitForm("panel:form");
    }

    @Test
    void gettingCallingPage_isNull() {
        SearchablePaperPanel panel = getTester().startComponentInPage(makePanel());
        assertThat(panel.getCallingPage()).isNull();
    }

    @Test
    void isNotAssociatedWithNewsletter() {
        assertThat(makePanel().isAssociatedWithNewsletter()).isFalse();
    }

    @Test
    void isNotAssociatedWithWipNewsletter() {
        assertThat(makePanel().isAssociatedWithWipNewsletter()).isFalse();
    }

    @Test
    void isNotNewsletterInStatusWip() {
        assertThat(makePanel().isaNewsletterInStatusWip()).isFalse();
    }

    @Test
    void modifyNewsletterAssociation_isNoOp() {
        AjaxRequestTarget targetMock = mock(AjaxRequestTarget.class);
        makePanel().modifyNewsletterAssociation(targetMock);
        verifyNoMoreInteractions(targetMock);
    }
}
