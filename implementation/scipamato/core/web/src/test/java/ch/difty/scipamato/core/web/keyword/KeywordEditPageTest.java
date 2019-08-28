package ch.difty.scipamato.core.web.keyword;

import static org.mockito.Mockito.*;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.web.authentication.LogoutPage;
import ch.difty.scipamato.core.web.common.BasePageTest;

@SuppressWarnings("SpellCheckingInspection")
class KeywordEditPageTest extends BasePageTest<KeywordEditPage> {

    private KeywordDefinition kd;

    @Mock
    private KeywordDefinition keywordDefinitionDummy;

    @Override
    public void setUpHook() {
        final KeywordTranslation kt_de = new KeywordTranslation(1, "de", "Name1", 1);
        final KeywordTranslation kt_de2 = new KeywordTranslation(1, "de", "Name1a", 1);
        final KeywordTranslation kt_en = new KeywordTranslation(2, "en", "name1", 1);
        final KeywordTranslation kt_fr = new KeywordTranslation(3, "fr", "nom1", 1);
        kd = new KeywordDefinition(1, "de", "thename", 1, kt_de, kt_en, kt_fr, kt_de2);
    }

    @AfterEach
    void tearDown() {
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
    void submitting_withSuccessfulServiceCall_addsInfoMsg() {
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
    void submitting_withUnsuccessfulServiceCall_addsErrorMsg() {
        when(keywordServiceMock.saveOrUpdate(isA(KeywordDefinition.class))).thenReturn(null);

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("Could not save keyword [id 1].");
    }

    @Test
    void submitting_withOptimisticLockingException_addsErrorMsg() {
        when(keywordServiceMock.saveOrUpdate(isA(KeywordDefinition.class))).thenThrow(
            new OptimisticLockingException("tblName", "rcd", OptimisticLockingException.Type.UPDATE));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("The tblName with id 1 has been modified concurrently "
                                        + "by another user. Please reload it and apply your changes once more.");
    }

    @Test
    void submitting_withDuplicateKeyException_addsErrorMsg() {
        when(keywordServiceMock.saveOrUpdate(isA(KeywordDefinition.class))).thenThrow(
            new DuplicateKeyException("boom"));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("boom");
    }

    @Test
    void submitting_withDuplicateKeyException_withNullMsg_addsStandardErrorMsg() {
        //noinspection ConstantConditions
        when(keywordServiceMock.saveOrUpdate(isA(KeywordDefinition.class))).thenThrow(new DuplicateKeyException(null));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("Unexpected DuplicateKeyConstraintViolation");
    }

    @Test
    void submitting_withOtherException_addsErrorMsg() {
        when(keywordServiceMock.saveOrUpdate(isA(KeywordDefinition.class))).thenThrow(new RuntimeException("fooMsg"));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("An unexpected error occurred when trying to save the keyword [id 1]: fooMsg");
    }

    @Test
    void submittingDelete_delegatesDeleteToService() {
        when(keywordServiceMock.delete(anyInt(), anyInt())).thenReturn(keywordDefinitionDummy);

        getTester().startPage(new KeywordEditPage(Model.of(kd), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("headerPanel:delete");

        verify(keywordServiceMock).delete(1, 1);
        verify(keywordServiceMock, never()).saveOrUpdate(any());

        verify(keywordServiceMock).countByFilter(isA(KeywordFilter.class));

        getTester().assertNoInfoMessage();
        getTester().assertNoErrorMessage();
    }

    @Test
    void submittingDelete_withForeignKeyConstraintViolationException_addsErrorMsg() {
        String msg = "... is still referenced from table \"paper_code\".; nested exception is org.postgresql.util.PSQLException...";
        when(keywordServiceMock.delete(anyInt(), anyInt())).thenThrow(new DataIntegrityViolationException(msg));

        getTester().startPage(new KeywordEditPage(Model.of(kd), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("headerPanel:delete");

        verify(keywordServiceMock).delete(1, 1);

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("Unable to delete this record as it is still used in other places.");
    }

    @Test
    void submittingDelete_withOptimisticLockingException_addsErrorMsg() {
        when(keywordServiceMock.delete(anyInt(), anyInt())).thenThrow(
            new OptimisticLockingException("keyword", OptimisticLockingException.Type.DELETE));

        getTester().startPage(new KeywordEditPage(Model.of(kd), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("headerPanel:delete");

        verify(keywordServiceMock).delete(1, 1);

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages(
            "The keyword with id 1 has been modified concurrently by another user. Please reload it and apply your changes once more.");
    }

    @Test
    void submittingDelete_withException_addsErrorMsg() {
        when(keywordServiceMock.delete(anyInt(), anyInt())).thenThrow(new RuntimeException("boom"));

        getTester().startPage(new KeywordEditPage(Model.of(kd), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("headerPanel:delete");

        verify(keywordServiceMock).delete(1, 1);

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("An unexpected error occurred when trying to delete keyword [id 1]: boom");
    }

    @Test
    void clickingBackButton_withPageWithoutCallingPageRef_forwardsToKeywordListPage() {
        getTester().startPage(new KeywordEditPage(Model.of(kd), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("headerPanel:back");

        getTester().assertRenderedPage(KeywordListPage.class);

        // from CodeListPage
        verify(keywordServiceMock).countByFilter(isA(KeywordFilter.class));
    }

    @Test
    void clickingBackButton_withPageWithCallingPageRef_forwardsToThat() {
        getTester().startPage(
            new KeywordEditPage(Model.of(kd), new LogoutPage(new PageParameters()).getPageReference()));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("headerPanel:back");

        getTester().assertRenderedPage(LogoutPage.class);
    }

    @Test
    void clickingAddNewKeyword_addsTranslationInRequestedLanguage_andRefreshesForm() {
        getTester().startPage(
            new KeywordEditPage(Model.of(kd), new LogoutPage(new PageParameters()).getPageReference()));

        int next = assertTranslationsInLanguages(1, "de", "de", "en", "fr");

        getTester().clickLink("form:translationsPanel:translations:3:addTranslation");

        assertTranslationsInLanguages(next, "de", "de", "en", "en", "fr");

        getTester().assertComponentOnAjaxResponse("form");
    }

    private int assertTranslationsInLanguages(final int startIndex, final String... languages) {
        int i = startIndex;
        for (final String lang : languages)
            getTester().assertModelValue("form:translationsPanel:translations:" + i++ + ":langCode", lang);
        return i;
    }

    @Test
    void clickingRemoveKeyword_removesTranslation_andRefreshesForm() {
        getTester().startPage(
            new KeywordEditPage(Model.of(kd), new LogoutPage(new PageParameters()).getPageReference()));

        int next = assertTranslationsInLanguages(1, "de", "de", "en", "fr");

        getTester().clickLink("form:translationsPanel:translations:2:removeTranslation");

        assertTranslationsInLanguages(next, "de", "en", "fr");

        getTester().assertComponentOnAjaxResponse("form");
    }
}