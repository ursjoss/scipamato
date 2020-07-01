package ch.difty.scipamato.core.web.behavior

import ch.difty.scipamato.core.web.WicketTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class AjaxTextDownloadTest : WicketTest() {

    private val ad = AjaxTextDownload(false)

    private lateinit var targetMock: AjaxRequestTarget

    override fun setUpHook() {
        targetMock = mockk()
        every { targetMock.appendJavaScript(any())} returns Unit
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `can instantiate AjaxTextDownload`() {
        ad.content.shouldBeNull()
        ad.fileName.shouldBeNull()
    }

    @Test
    fun `can set title and content`() {
        ad.apply {
            content = "foo"
            fileName = "bar.txt"
        }
        ad.content shouldBeEqualTo "foo"
        ad.fileName shouldBeEqualTo "bar.txt"
    }

    @Test
    fun `clicking the link adds javascript to target2`() {
        val l = object : AjaxLink<Void>("l") {
            override fun onClick(target: AjaxRequestTarget?) = ad.initiate(targetMock)
        }
        l.add(ad)
        tester.startComponentInPage(l)
        tester.clickLink(l)
        verify { targetMock.appendJavaScript("""setTimeout("window.location.href='./page?2-1.0-l'", 100);""") }
    }

    @Test
    fun `clicking the link adds javascript to target`() {
        val ad2 = AjaxTextDownload(true)
        val l = object : AjaxLink<Void>("l") {
            override fun onClick(target: AjaxRequestTarget?) {
                ad2.initiate(targetMock)
            }
        }
        l.add(ad2)
        tester.startComponentInPage(l)
        tester.clickLink(l)
        // containing timestamp -> difficult to test
        verify { targetMock.appendJavaScript(any()) }
    }
}
