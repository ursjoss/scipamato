package ch.difty.scipamato.core.web.behavior

import ch.difty.scipamato.common.AjaxRequestTargetSpy
import ch.difty.scipamato.core.web.WicketTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class AjaxTextDownloadTest : WicketTest() {

    private val ad = AjaxTextDownload(addAntiCache = false)

    private val targetDummy = AjaxRequestTargetSpy()

    @AfterEach
    fun tearDown() {
        targetDummy.reset()
    }

    @Test
    fun `can instantiate AjaxTextDownload`() {
        ad.content.shouldBeNull()
        ad.fileName.shouldBeNull()
        ad.contentType shouldBeEqualTo "application/text"
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
    fun `clicking the link applying antiCache explicitly adds javascript to target`() {
        assertAntiCacheBehavior(AjaxTextDownload(addAntiCache = true))
    }

    @Test
    fun `clicking the link applying antiCache implicitly adds javascript to target`() {
        assertAntiCacheBehavior(AjaxTextDownload())
    }

    private fun assertAntiCacheBehavior(ad2: AjaxTextDownload) {
        val l = object : AjaxLink<Void>("l") {
            private val serialVersionUID: Long = 1L
            override fun onClick(target: AjaxRequestTarget?) {
                ad2.initiate(targetDummy)
            }
        }
        l.add(ad2)
        tester.startComponentInPage(l)
        tester.clickLink(l)
        // containing timestamp -> difficult to test
        targetDummy.javaScripts.size shouldBeEqualTo 1
    }
}
