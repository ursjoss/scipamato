package ch.difty.scipamato.core.web.common.pastemodal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.fileUpload.DropZoneFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.scipamato.core.web.common.PanelTest;

public class XmlPasteModalPanelTest extends PanelTest<XmlPasteModalPanel> {

    @Mock
    private AjaxRequestTarget requestTargetMock;

    private final Map<String, List<FileItem>> map = new HashMap<>();

    private final List<FileItem> files = new ArrayList<>();

    @Mock
    private FileItem fileItem;

    @After
    public void tearDown() {
        verifyNoMoreInteractions(requestTargetMock, fileItem);
    }

    @Override
    protected XmlPasteModalPanel makePanel() {
        return new XmlPasteModalPanel(PANEL_ID);
    }

    @Override
    protected void assertSpecificComponents() {
        String b = PANEL_ID;
        getTester().assertComponent(b, Panel.class);

        b += ":form";
        getTester().assertComponent(b, Form.class);

        b += ":";
        getTester().assertComponent(b + "content", TextArea.class);
        getTester().assertComponent(b + "dropzone", DropZoneFileUpload.class);
        getTester().assertComponent(b + "submit", BootstrapAjaxButton.class);
        getTester().assertComponent(b + "cancel", BootstrapAjaxButton.class);
    }

    @Test
    public void clickingCancel_clearsPastedContentAndClosesWindow() {
        XmlPasteModalPanel panel = makePanel();
        getTester().startComponentInPage(panel);
        panel
            .get("form:content")
            .setDefaultModelObject("abc");
        assertThat(panel.getPastedContent()).isEqualTo("abc");

        getTester().executeAjaxEvent("panel:form:cancel", "click");

        assertThat(panel.getPastedContent()).isNull();
        getTester().assertNoFeedbackMessage(0);

        assertThat(getTester()
            .getLastResponse()
            .getDocument()).contains("win.current.close();");
    }

    @Test
    public void clickingSubmit_keepsPastedContentAndClosesWindow() {
        XmlPasteModalPanel panel = makePanel();
        getTester().startComponentInPage(panel);
        panel
            .get("form:content")
            .setDefaultModelObject("def");
        assertThat(panel.getPastedContent()).isEqualTo("def");

        getTester().executeAjaxEvent("panel:form:submit", "click");

        assertThat(panel.getPastedContent()).isEqualTo("def");
        getTester().assertNoFeedbackMessage(0);

        assertThat(getTester()
            .getLastResponse()
            .getDocument()).contains("win.current.close();");
    }

    @Test
    public void updating_withNullMap_doesNothing() {
        XmlPasteModalPanel panel = makePanel();
        panel.doOnUpdate(requestTargetMock, null);
    }

    @Test
    public void updating_withEmptyMap_doesNothing() {
        XmlPasteModalPanel panel = makePanel();
        panel.doOnUpdate(requestTargetMock, map);
    }

    @Test
    public void updating_withFileKeyNotFoundInMap_doesNothing() {
        files.add(fileItem);
        map.put("foo", files);

        XmlPasteModalPanel panel = makePanel();
        panel.doOnUpdate(requestTargetMock, map);
    }

    @Test
    public void updating_withFileKeyFoundButEmptyMap_doesNothingExceptForAddingFieldToTarget() {
        map.put("file", files);

        XmlPasteModalPanel panel = makePanel();
        getTester().startComponentInPage(panel);
        panel.doOnUpdate(requestTargetMock, map);

        verify(requestTargetMock).add(isA(TextArea.class));
    }

    @Test
    public void updating_withFileWIthWrongContentType_doesNothingExceptForAddingFieldToTarget() {
        when(fileItem.getContentType()).thenReturn("foo");
        files.add(fileItem);
        map.put("file", files);

        XmlPasteModalPanel panel = makePanel();
        getTester().startComponentInPage(panel);
        panel.doOnUpdate(requestTargetMock, map);

        verify(requestTargetMock).add(isA(TextArea.class));
        verify(fileItem).getContentType();
    }

    @Test
    public void updating__extractsContentAndAddsContentToTarget() {
        when(fileItem.getContentType()).thenReturn("text/xml");
        when(fileItem.getName()).thenReturn("fileName");
        when(fileItem.getString()).thenReturn("fileContent");

        files.add(fileItem);
        map.put("file", files);

        XmlPasteModalPanel panel = makePanel();
        getTester().startComponentInPage(panel);
        panel.doOnUpdate(requestTargetMock, map);

        getTester().assertInfoMessages("File 'fileName' [text/xml] was uploaded successfully.");

        verify(requestTargetMock).add(isA(TextArea.class));
        verify(fileItem, times(2)).getContentType();
        verify(fileItem).getString();
        verify(fileItem).getName();

    }
}
