package ch.difty.scipamato.core.web.behavior

import ch.difty.scipamato.common.AjaxRequestTargetSpy
import ch.difty.scipamato.core.web.WicketTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class AjaxCsvDownloadTest : WicketTest() {

    private val ad = AjaxCsvDownload(addAntiCache = false)

    private val targetDummy = AjaxRequestTargetSpy()

    @AfterEach
    fun tearDown() {
        targetDummy.reset()
    }

    @Test
    fun `can instantiate AjaxCsvDownload`() {
        ad.content.shouldBeNull()
        ad.fileName.shouldBeNull()
        ad.contentType shouldBeEqualTo "text/csv"
    }

    @Test
    fun `can set title and content`() {
        ad.apply {
            content = "a;b;c"
            fileName = "file.csv"
        }
        ad.content shouldBeEqualTo "a;b;c"
        ad.fileName shouldBeEqualTo "file.csv"
    }

    @Test
    fun `clicking the link adds javascript to target`() {
        val l = object : AjaxLink<Void>("l") {
            private val serialVersionUID: Long = 1L
            override fun onClick(target: AjaxRequestTarget?) = ad.initiate(targetDummy)
        }
        l.add(ad)
        tester.startComponentInPage(l)
        tester.clickLink(l)
        targetDummy.javaScripts.size shouldBeEqualTo 1
        targetDummy.javaScripts.contains("""setTimeout("window.location.href='./page?2-1.0-l'", 100);""")
    }

    @Test
    fun `clicking the link applying antiCache explicitly adds javascript to target`() {
        assertAntiCacheBehavior(AjaxCsvDownload(addAntiCache = true))
    }

    @Test
    fun `clicking the link applying antiCache implicitly adds javascript to target`() {
        assertAntiCacheBehavior(AjaxCsvDownload())
    }

    private fun assertAntiCacheBehavior(ad: AjaxCsvDownload) {
        val l = object : AjaxLink<Void>("l") {
            private val serialVersionUID: Long = 1L
            override fun onClick(target: AjaxRequestTarget?) {
                ad.initiate(targetDummy)
            }
        }
        l.add(ad)
        tester.startComponentInPage(l)
        tester.clickLink(l)
        // containing timestamp -> difficult to test
        targetDummy.javaScripts.size shouldBeEqualTo 1
    }
}
