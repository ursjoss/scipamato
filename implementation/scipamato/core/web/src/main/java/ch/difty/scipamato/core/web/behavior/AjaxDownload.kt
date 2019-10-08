package ch.difty.scipamato.core.web.behavior

import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.behavior.AbstractAjaxBehavior
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler
import org.apache.wicket.request.resource.ContentDisposition
import org.apache.wicket.util.resource.IResourceStream
import org.apache.wicket.util.resource.StringResourceStream

/**
 * @author Sven Meier
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 * @author Jordi Deu-Pons (jordi@jordeu.net)
 */
@Suppress("SpellCheckingInspection")
sealed class AjaxDownload(private val addAntiCache: Boolean) : AbstractAjaxBehavior() {

    var fileName: String? = null

    /**
     * Hook method providing the actual resource stream.
     */
    protected abstract val resourceStream: IResourceStream

    /**
     * Call this method to initiate the download.
     * The timeout is needed to let Wicket release the channel
     */
    fun initiate(target: AjaxRequestTarget) {
        target.appendJavaScript("""setTimeout("window.location.href='${callbackUrl.toUrl(addAntiCache)}'", 100);""")
    }

    override fun onRequest() {
        val handler = ResourceStreamRequestHandler(resourceStream, fileName)
        handler.contentDisposition = ContentDisposition.ATTACHMENT
        component.requestCycle.scheduleRequestHandlerAfterCurrent(handler)
    }
}

open class AjaxTextDownload @JvmOverloads constructor(addAntiCache: Boolean = true) : AjaxDownload(addAntiCache) {

    var content: String? = null

    override val resourceStream: IResourceStream
        get() = StringResourceStream(content, "application/text")
}

private fun CharSequence.toUrl(antiCache: Boolean): String =
        if (!antiCache) toString() else "$this${if (this.contains("?")) "&" else "?"}antiCache=${System.currentTimeMillis()}"