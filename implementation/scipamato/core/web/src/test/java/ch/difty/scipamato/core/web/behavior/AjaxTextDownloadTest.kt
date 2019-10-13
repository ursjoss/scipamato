package ch.difty.scipamato.core.web.behavior

import ch.difty.scipamato.core.web.WicketTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AjaxTextDownloadTest : WicketTest() {

    private val ad = AjaxTextDownload(false)

    private val targetMock = mock<AjaxRequestTarget>()

    @Test
    fun `can instantiate AjaxTextDownload`() {
        assertThat(ad.content == null).isTrue()
        assertThat(ad.fileName == null).isTrue()
    }

    @Test
    fun `can set title and content`() {
        ad.apply {
            content = "foo"
            fileName = "bar.txt"
        }
        assertThat(ad.content).isEqualTo("foo")
        assertThat(ad.fileName).isEqualTo("bar.txt")
    }

    @Test
    fun `clicking the link adds javascript to target2`() {
        val l = object : AjaxLink<Void>("l") {
            override fun onClick(target: AjaxRequestTarget?) {
                ad.initiate(targetMock)
            }
        }
        l.add(ad)
        tester.startComponentInPage(l)
        tester.clickLink(l)
        verify(targetMock).appendJavaScript("""setTimeout("window.location.href='./page?2-1.0-l'", 100);""")
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
        verify(targetMock).appendJavaScript(any()) // containing timestamp -> difficult to test
    }
}
