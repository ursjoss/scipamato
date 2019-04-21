package ch.difty.scipamato.core.web.codeclass;

import static org.mockito.Mockito.*;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;

import ch.difty.scipamato.core.entity.code_class.CodeClassDefinition;
import ch.difty.scipamato.core.entity.code_class.CodeClassTranslation;
import ch.difty.scipamato.core.persistence.CodeClassService;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.web.authentication.LogoutPage;
import ch.difty.scipamato.core.web.code.CodeListPage;
import ch.difty.scipamato.core.web.common.BasePageTest;

public class CodeClassEditPageTest extends BasePageTest<CodeClassEditPage> {

    private CodeClassDefinition ccd;

    @MockBean
    private CodeClassService codeClassServiceMock;

    @Override
    public void setUpHook() {
        final CodeClassTranslation cct_de = new CodeClassTranslation(1, "de", "Name1", "some description", 1);
        final CodeClassTranslation cct_de2 = new CodeClassTranslation(1, "de", "Name1a", null, 1);
        final CodeClassTranslation cct_en = new CodeClassTranslation(2, "en", "name1", null, 1);
        final CodeClassTranslation cct_fr = new CodeClassTranslation(3, "fr", "nom1", null, 1);
        ccd = new CodeClassDefinition(1, "de", 1, cct_de, cct_en, cct_fr, cct_de2);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(codeClassServiceMock);
    }

    @Override
    protected CodeClassEditPage makePage() {
        return new CodeClassEditPage(Model.of(ccd), null);
    }

    @Override
    protected Class<CodeClassEditPage> getPageClass() {
        return CodeClassEditPage.class;
    }

    @Override
    public void assertSpecificComponents() {
        String b = "form";
        getTester().assertComponent(b, Form.class);

        b += ":headerPanel:";
        String bb = b + "id";
        getTester().assertLabel(bb + "Label", "ID");
        getTester().assertComponent(bb, TextField.class);
        getTester().assertModelValue(bb, 1);
        getTester().assertDisabled(bb);

        getTester().assertComponent(b + "back", BootstrapButton.class);
        getTester().assertComponent(b + "submit", BootstrapButton.class);

        bb = "form:translations";
        getTester().assertLabel(bb + "Label", "Code Class Translations and Descriptions");

        bb += "Panel:translations";
        getTester().assertComponent(bb, RefreshingView.class);
        bb += ":";
        assertTranslation(bb, 1, "de", "Name1", "some description");
        assertTranslation(bb, 2, "de", "Name1a", null);
        assertTranslation(bb, 3, "en", "name1", null);
        assertTranslation(bb, 4, "fr", "nom1", null);
    }

    private void assertTranslation(final String bb, final int idx, final String langCode, final String name,
        final String description) {
        getTester().assertLabel(bb + idx + ":langCode", langCode);
        getTester().assertComponent(bb + idx + ":name", TextField.class);
        getTester().assertModelValue(bb + idx + ":name", name);
        getTester().assertComponent(bb + idx + ":description", TextField.class);
        getTester().assertModelValue(bb + idx + ":description", description);
    }

    @Test
    public void submitting_withSuccessfulServiceCall_addsInfoMsg() {
        when(codeClassServiceMock.saveOrUpdate(isA(CodeClassDefinition.class))).thenReturn(ccd);

        runSubmitTest();

        getTester().assertInfoMessages("Successfully saved code class 1: DE: 'foo','Name1a'; EN: 'name1'; FR: 'nom1'.");
        getTester().assertNoErrorMessage();
    }

    private void runSubmitTest() {
        getTester().startPage(new CodeClassEditPage(Model.of(ccd), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("translationsPanel:translations:1:name", "foo");
        assertTranslation("form:translationsPanel:translations:", 1, "de", "Name1", "some description");
        formTester.submit("headerPanel:submit");
        assertTranslation("form:translationsPanel:translations:", 5, "de", "foo", "some description");

        verify(codeClassServiceMock).saveOrUpdate(isA(CodeClassDefinition.class));
    }

    @Test
    public void submitting_withUnsuccessfulServiceCall_addsErrorMsg() {
        when(codeClassServiceMock.saveOrUpdate(isA(CodeClassDefinition.class))).thenReturn(null);

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("Could not save code class 1.");
    }

    @Test
    public void submitting_withOptimisticLockingException_addsErrorMsg() {
        when(codeClassServiceMock.saveOrUpdate(isA(CodeClassDefinition.class))).thenThrow(
            new OptimisticLockingException("tblName", "rcd", OptimisticLockingException.Type.UPDATE));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("The tblName with id 1 has been modified concurrently "
                                        + "by another user. Please reload it and apply your changes once more.");
    }

    @Test
    public void submitting_withOtherException_addsErrorMsg() {
        when(codeClassServiceMock.saveOrUpdate(isA(CodeClassDefinition.class))).thenThrow(
            new RuntimeException("fooMsg"));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("An unexpected error occurred when trying to save code class 1: fooMsg");
    }

    @Test
    public void submitting_withDuplicateKeyConstraintViolationException_addsErrorMsg() {
        String msg = "...Detail: Key (code_class_id, lang_code)=(1, en) already exists.; "
                     + "nested exception is org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint...";
        when(codeClassServiceMock.saveOrUpdate(isA(CodeClassDefinition.class))).thenThrow(
            new DuplicateKeyException(msg));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("Code class id 1 is already used.");
    }

    @Test
    public void submitting_withDuplicateKeyConstraintViolationException_withUnexpectedErrorMessage_addsThat() {
        String msg = "odd";
        when(codeClassServiceMock.saveOrUpdate(isA(CodeClassDefinition.class))).thenThrow(
            new DuplicateKeyException(msg));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("odd");
    }

    @Test
    public void submitting_withDuplicateKeyConstraintViolationException_withNullMsg_addsThatErrorMsg() {
        //noinspection ConstantConditions
        when(codeClassServiceMock.saveOrUpdate(isA(CodeClassDefinition.class))).thenThrow(
            new DuplicateKeyException(null));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("Unexpected DuplicateKeyConstraintViolation");
    }

    @Test
    public void clickingBackButton_withPageWithoutCallingPageRef_forwardsToCodeListPage() {
        getTester().startPage(new CodeClassEditPage(Model.of(ccd), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("headerPanel:back");

        getTester().assertRenderedPage(CodeListPage.class);

        // from CodeClassListPage
        verify(codeClassServiceMock).find("en_us");
    }

    @Test
    public void clickingBackButton_withPageWithCallingPageRef_forwardsToThat() {
        getTester().startPage(
            new CodeClassEditPage(Model.of(ccd), new LogoutPage(new PageParameters()).getPageReference()));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("headerPanel:back");

        getTester().assertRenderedPage(LogoutPage.class);
    }

}