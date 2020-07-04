package ch.difty.scipamato.core.web.common.pastemodal

import ch.difty.scipamato.common.AjaxRequestTargetSpy
import ch.difty.scipamato.core.web.common.PanelTest
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton
import de.agilecoders.wicket.extensions.markup.html.bootstrap.fileUpload.DropZoneFileUpload
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotBeEmpty
import org.apache.commons.fileupload.FileItem
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextArea
import org.apache.wicket.markup.html.panel.Panel
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.util.ArrayList

@Suppress("SpellCheckingInspection")
internal class XmlPasteModalPanelTest : PanelTest<XmlPasteModalPanel>() {

    private val targetSpy = AjaxRequestTargetSpy()
    private lateinit var fileItem: FileItem

    private val map = mutableMapOf<String, List<FileItem?>>()
    private val files: MutableList<FileItem?> = ArrayList()

    override fun setUpHook() {
        fileItem = mockk()
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(fileItem)
        targetSpy.reset()
        tester.destroy()
        unmockkAll()
    }

    override fun makePanel(): XmlPasteModalPanel = XmlPasteModalPanel(PANEL_ID)

    override fun assertSpecificComponents() {
        var b = PANEL_ID
        tester.assertComponent(b, Panel::class.java)
        b += ":form"
        tester.assertComponent(b, Form::class.java)
        b += ":"
        tester.assertComponent(b + "content", TextArea::class.java)
        tester.assertComponent(b + "dropzone", DropZoneFileUpload::class.java)
        tester.assertComponent(b + "submit", BootstrapAjaxButton::class.java)
        tester.assertComponent(b + "cancel", BootstrapAjaxButton::class.java)
    }

    @Test
    fun clickingCancel_clearsPastedContentAndClosesWindow() {
        val panel = makePanel()
        tester.startComponentInPage(panel)
        panel["form:content"].defaultModelObject = "abc"
        panel.pastedContent shouldBeEqualTo "abc"
        tester.executeAjaxEvent("panel:form:cancel", "click")
        panel.pastedContent.shouldBeNull()
        tester.assertNoFeedbackMessage(0)
        tester.lastResponse.document shouldContain "win.current.close();"
    }

    @Test
    fun clickingSubmit_keepsPastedContentAndClosesWindow() {
        val panel = makePanel()
        tester.startComponentInPage(panel)
        panel["form:content"].defaultModelObject = "def"
        panel.pastedContent shouldBeEqualTo "def"
        tester.executeAjaxEvent("panel:form:submit", "click")
        panel.pastedContent shouldBeEqualTo "def"
        tester.assertNoFeedbackMessage(0)
        tester.lastResponse.document shouldContain "win.current.close();"
    }

    @Test
    fun updating_withNullMap_doesNothing() {
        val panel = makePanel()
        panel.doOnUpdate(targetSpy, null)
    }

    @Test
    fun updating_withEmptyMap_doesNothing() {
        val panel = makePanel()
        panel.doOnUpdate(targetSpy, map)
    }

    @Test
    fun updating_withFileKeyNotFoundInMap_doesNothing() {
        files.add(fileItem)
        map["foo"] = files
        val panel = makePanel()
        panel.doOnUpdate(targetSpy, map)
    }

    @Test
    fun updating_withFileKeyFoundButEmptyMap_doesNothingExceptForAddingFieldToTarget() {
        map["file"] = files
        val panel = makePanel()
        tester.startComponentInPage(panel)
        panel.doOnUpdate(targetSpy, map)
        targetSpy.components.shouldNotBeEmpty()
        targetSpy.reset()
    }

    @Test
    fun updating_withFileWIthWrongContentType_doesNothingExceptForAddingFieldToTarget() {
        every { fileItem.contentType } returns "foo"
        files.add(fileItem)
        map["file"] = files
        val panel = makePanel()
        tester.startComponentInPage(panel)
        panel.doOnUpdate(targetSpy, map)
        targetSpy.components.shouldNotBeEmpty()
        verify { fileItem.contentType }
    }

    @Test
    fun updating__extractsContentAndAddsContentToTarget() {
        every { fileItem.contentType } returns "text/xml"
        every { fileItem.name } returns "fileName"
        every { fileItem.string } returns "fileContent"
        files.add(fileItem)
        map["file"] = files
        val panel = makePanel()
        tester.startComponentInPage(panel)
        panel.doOnUpdate(targetSpy, map)
        tester.assertInfoMessages("File 'fileName' [text/xml] was uploaded successfully.")
        targetSpy.components.shouldNotBeEmpty()
        verify(exactly = 2) { fileItem.contentType }
        verify { fileItem.string }
        verify { fileItem.name }
    }
}
