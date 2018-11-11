package ch.difty.scipamato.core.web.keyword;

import static org.mockito.Mockito.*;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation;
import ch.difty.scipamato.core.persistence.KeywordService;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.web.common.BasePageTest;

public class KeywordEditPageTest extends BasePageTest<KeywordEditPage> {

    private KeywordDefinition kd;

    @MockBean
    private KeywordService keywordServiceMock;

    @Override
    public void setUpHook() {
        final KeywordTranslation kt_de = new KeywordTranslation(1, "de", "Name1", 1);
        final KeywordTranslation kt_de2 = new KeywordTranslation(1, "de", "Name1a", 1);
        final KeywordTranslation kt_en = new KeywordTranslation(2, "en", "name1", 1);
        final KeywordTranslation kt_fr = new KeywordTranslation(3, "fr", "nom1", 1);
        kd = new KeywordDefinition(1, "de", "thename", 1, kt_de, kt_en, kt_fr, kt_de2);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(keywordServiceMock);
    }

    @Override
    protected KeywordEditPage makePage() {
        return new KeywordEditPage(Model.of(kd), null);
    }

    @Override
    protected Class<KeywordEditPage> getPageClass() {
        return KeywordEditPage.class;
    }

    @Override
    public void assertSpecificComponents() {
        String b = "form";
        getTester().assertComponent(b, Form.class);

        b += ":";
        String bb = b + "header";
        getTester().assertLabel(bb + "Label", "Keyword");

        bb += "Panel:";
        getTester().assertComponent(bb + "back", BootstrapButton.class);
        getTester().assertComponent(bb + "submit", BootstrapButton.class);
        getTester().assertComponent(bb + "delete", BootstrapButton.class);

        bb += "searchOverride";
        getTester().assertLabel(bb + "Label", "Search Override");
        getTester().assertComponent(bb, TextField.class);
        getTester().assertModelValue(bb, "thename");

        bb = "form:translations";
        getTester().assertLabel(bb + "Label", "Keyword Translations");

        bb += "Panel:translations";
        getTester().assertComponent(bb, RefreshingView.class);
        bb += ":";
        assertTranslation(bb, 1, "de", "Name1");
        assertTranslation(bb, 2, "de", "Name1a");
        assertTranslation(bb, 3, "en", "name1");
        assertTranslation(bb, 4, "fr", "nom1");
    }

    private void assertTranslation(final String bb, final int idx, final String langCode, final String name) {
        getTester().assertLabel(bb + idx + ":langCode", langCode);
        getTester().assertComponent(bb + idx + ":name", TextField.class);
        getTester().assertModelValue(bb + idx + ":name", name);
        getTester().assertComponent(bb + idx + ":addTranslation", AjaxLink.class);
        getTester().assertComponent(bb + idx + ":removeTranslation", AjaxLink.class);
    }

    @Test
    public void submitting_withSuccessfulServiceCall_addsInfoMsg() {
        when(keywordServiceMock.saveOrUpdate(isA(KeywordDefinition.class))).thenReturn(kd);

        runSubmitTest();

        getTester().assertInfoMessages(
            "Successfully saved keyword [id 1]: DE: 'foo','Name1a'; EN: 'name1'; FR: 'nom1'.");
        getTester().assertNoErrorMessage();
    }

    private void runSubmitTest() {
        getTester().startPage(new KeywordEditPage(Model.of(kd), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("translationsPanel:translations:1:name", "foo");
        assertTranslation("form:translationsPanel:translations:", 1, "de", "Name1");
        formTester.submit("headerPanel:submit");
        assertTranslation("form:translationsPanel:translations:", 5, "de", "foo");

        verify(keywordServiceMock).saveOrUpdate(isA(KeywordDefinition.class));
    }

    @Test
    public void submitting_withUnsuccessfulServiceCall_addsErrorMsg() {
        when(keywordServiceMock.saveOrUpdate(isA(KeywordDefinition.class))).thenReturn(null);

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("Could not save keyword [id 1].");
    }

    @Test
    public void submitting_withOptimisticLockingException_addsErrorMsg() {
        when(keywordServiceMock.saveOrUpdate(isA(KeywordDefinition.class))).thenThrow(
            new OptimisticLockingException("tblName", "rcd", OptimisticLockingException.Type.UPDATE));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("The tblName with id 1 has been modified concurrently "
                                        + "by another user. Please reload it and apply your changes once more.");
    }

    @Test
    public void submitting_withOtherException_addsErrorMsg() {
        when(keywordServiceMock.saveOrUpdate(isA(KeywordDefinition.class))).thenThrow(new RuntimeException("fooMsg"));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("An unexpected error occurred when trying to save the keyword [id 1]: fooMsg");
    }

}