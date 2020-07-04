/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.difty.scipamato

import org.apache.wicket.Application
import org.apache.wicket.Component
import org.apache.wicket.MarkupContainer
import org.apache.wicket.Page
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.AjaxRequestTarget.IJavaScriptResponse
import org.apache.wicket.ajax.AjaxRequestTarget.IListener
import org.apache.wicket.ajax.AjaxRequestTarget.ITargetRespondListener
import org.apache.wicket.core.request.handler.PageProvider
import org.apache.wicket.core.request.handler.RenderPageRequestHandler
import org.apache.wicket.core.request.handler.logger.PageLogData
import org.apache.wicket.event.Broadcast
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.page.PartialPageUpdate
import org.apache.wicket.page.XmlPartialPageUpdate
import org.apache.wicket.request.IRequestCycle
import org.apache.wicket.request.IRequestHandler
import org.apache.wicket.request.Response
import org.apache.wicket.request.component.IRequestablePage
import org.apache.wicket.request.cycle.RequestCycle
import org.apache.wicket.request.http.WebRequest
import org.apache.wicket.request.http.WebResponse
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.response.StringResponse
import org.apache.wicket.response.filter.IResponseFilter
import org.apache.wicket.util.encoding.UrlDecoder
import org.apache.wicket.util.lang.Args
import org.apache.wicket.util.lang.Classes
import org.apache.wicket.util.string.AppendingStringBuffer
import org.apache.wicket.util.string.Strings
import java.util.Collections
import java.util.HashSet

/**
 * Copy of the original AjaxRequestHandler bundled in wicket, converted to kotlin
 * with some minor refactorings but with one major change that has a significant effect
 * on the duration required to test core-web:
 *
 * Instead of using a `LinkedList` to store the listeners, we use a [HashSet]. This
 * improves the time complexity of the [contains] method on the collection in the
 * method [addListener] from O(n) to constant time O(1). It does not maintain
 * insertion order though. If insertion order would be important, we could use
 * LinkedHashSet, which is slower than HashSet however.
 *
 * When running the tests with mockk, this decreased the total time used for `gradlew check`
 * from several hours to 20 minutes.
 *
 * @author Igor Vaynberg (ivaynberg)
 * @author Eelco Hillenius
 * @author Urs Joss
 */
@Suppress("SpellCheckingInspection")
class TestAjaxRequestHandler(page: Page) : AjaxRequestTarget {
    /**
     * Collector of page updates.
     */
    private val update: PartialPageUpdate

    /** a set of listeners  */
    private var listeners: MutableSet<IListener>? = null

    private val respondListeners: MutableSet<ITargetRespondListener> = HashSet()

    /** see https://issues.apache.org/jira/browse/WICKET-3564  */
    @Transient
    var respondersFrozen = false

    @Transient
    var listenersFrozen = false

    /** The associated Page  */
    private val page: Page = Args.notNull(page, "page")
    private var logData: PageLogData? = null

    /**
     * @see org.apache.wicket.core.request.handler.IPageRequestHandler.getPage
     */
    override fun getPage(): Page = page

    @Throws(IllegalStateException::class)
    override fun addListener(listener: IListener) {
        Args.notNull(listener, "listener")
        assertListenersNotFrozen()
        if (listeners == null)
            listeners = HashSet()
        if (listener !in listeners!!)
            listeners!!.add(listener)
    }

    override fun addChildren(parent: MarkupContainer, childCriteria: Class<*>?) {
        Args.notNull(parent, "parent")
        Args.notNull(childCriteria, "childCriteria")
        parent.visitChildren<Component, Void>(childCriteria) { component, visit ->
            add(component!!)
            visit.dontGoDeeper()
        }
    }

    override fun add(vararg components: Component) {
        for (component in components) {
            Args.notNull(component, "component")
            require(component.outputMarkupId || component is Page) {
                "Cannot update component that does not have setOutputMarkupId property set to true. Component: " +
                    component.toString()
            }
            add(component, component.markupId)
        }
    }

    override fun add(component: Component, markupId: String) {
        update.add(component, markupId)
    }

    override fun getComponents(): Collection<Component> = update.components

    override fun focusComponent(component: Component) {
        require(component.outputMarkupId) {
            "cannot update component that does not have setOutputMarkupId property set to true. Component: " +
                component.toString()
        }
        val id = "'${component.markupId}'"
        appendJavaScript("Wicket.Focus.setFocusOnId($id);")
    }

    override fun appendJavaScript(javascript: CharSequence) {
        update.appendJavaScript(javascript)
    }

    /**
     * @see org.apache.wicket.core.request.handler.IPageRequestHandler.detach
     */
    override fun detach(requestCycle: IRequestCycle) {
        if (logData == null) {
            logData = PageLogData(page)
        }
        update.detach(requestCycle)
    }

    /**
     * @see Object.equals
     */
    override fun equals(other: Any?): Boolean {
        if (other is TestAjaxRequestHandler) {
            return update == other.update
        }
        return false
    }

    /**
     * @see Object.hashCode
     */
    override fun hashCode(): Int {
        var result = "AjaxRequestHandler".hashCode()
        result += update.hashCode() * 17
        return result
    }

    override fun prependJavaScript(javascript: CharSequence) {
        update.prependJavaScript(javascript)
    }

    override fun registerRespondListener(listener: ITargetRespondListener) {
        assertRespondersNotFrozen()
        respondListeners.add(listener)
    }

    /**
     * @see org.apache.wicket.core.request.handler.IPageRequestHandler.respond
     */
    override fun respond(requestCycle: IRequestCycle) {
        val rc = requestCycle as RequestCycle
        val response = requestCycle.getResponse() as WebResponse
        if (shouldRedirectToPage(requestCycle)) {
            // the page itself has been added to the request target, we simply issue a redirect
            // back to the page
            val handler: IRequestHandler = RenderPageRequestHandler(PageProvider(page))
            val url = rc.urlFor(handler).toString()
            response.sendRedirect(url)
            return
        }
        respondersFrozen = true
        for (listener in respondListeners)
            listener.onTargetRespond(this)
        val app = page.application
        page.send(app, Broadcast.BREADTH, this)

        // Determine encoding
        val encoding = app.requestCycleSettings.responseRequestEncoding

        // Set content type based on markup type for page
        update.setContentType(response, encoding)

        // Make sure it is not cached by a client
        response.disableCaching()
        val bodyResponse = StringResponse()
        update.writeTo(bodyResponse, encoding)
        val filteredResponse: CharSequence = invokeResponseFilters(bodyResponse)
        response.write(filteredResponse)
    }

    private fun shouldRedirectToPage(requestCycle: IRequestCycle): Boolean {
        if (update.containsPage()) return true
        return !(requestCycle.request as WebRequest).isAjax
    }

    /**
     * Runs the configured [IResponseFilter]s over the constructed Ajax response
     *
     * @param contentResponse
     * the Ajax [Response] body
     * @return filtered response
     */
    private fun invokeResponseFilters(contentResponse: StringResponse): AppendingStringBuffer {
        var responseBuffer = AppendingStringBuffer(contentResponse.buffer)
        val responseFilters = Application.get()
            .requestCycleSettings
            .responseFilters
        responseFilters?.forEach { filter ->
            responseBuffer = filter.filter(responseBuffer)
        }
        return responseBuffer
    }

    /**
     * @see Object.toString
     */
    override fun toString(): String = "[AjaxRequestHandler@${hashCode()} responseObject [$update]"

    override fun getHeaderResponse(): IHeaderResponse = update.headerResponse

    /**
     * @return the markup id of the focused element in the browser
     */
    override fun getLastFocusedElementId(): String? {
        val request = page.request as WebRequest
        val id = request.getHeader("Wicket-FocusedElementId")

        // WICKET-6568 might contain non-ASCII
        return if (Strings.isEmpty(id)) null else UrlDecoder.QUERY_INSTANCE.decode(id, request.charset)
    }

    override fun getPageClass(): Class<out IRequestablePage?> = page.pageClass

    override fun getPageId(): Int = page.pageId

    override fun getPageParameters(): PageParameters = page.pageParameters

    override fun isPageInstanceCreated(): Boolean = true

    override fun getRenderCount(): Int = page.renderCount

    override fun getLogData(): PageLogData = logData!!

    private fun assertNotFrozen(frozen: Boolean, clazz: Class<*>) {
        check(!frozen) { "${Classes.simpleName(clazz)}s can no longer be added" }
    }

    private fun assertRespondersNotFrozen() {
        assertNotFrozen(respondersFrozen, ITargetRespondListener::class.java)
    }

    private fun assertListenersNotFrozen() {
        assertNotFrozen(listenersFrozen, IListener::class.java)
    }

    init {
        update = object : XmlPartialPageUpdate(page) {
            /**
             * Freezes the [TestAjaxRequestHandler.listeners] before firing the event and
             * un-freezes them afterwards to allow components to add more
             * [IListener]s for the second event.
             */
            override fun onBeforeRespond(response: Response) {
                listenersFrozen = true
                listeners?.forEach { listener ->
                    listener.onBeforeRespond(markupIdToComponent, this@TestAjaxRequestHandler)
                }
                listenersFrozen = false
            }

            /**
             * Freezes the [TestAjaxRequestHandler.listeners], and does not un-freeze them as the
             * events will have been fired by now.
             *
             * @param response the response to write to
             */
            override fun onAfterRespond(response: Response) {
                listenersFrozen = true

                // invoke onafterresponse event on listeners
                listeners?.let {
                    val components = Collections
                        .unmodifiableMap(markupIdToComponent)

                    // create response that will be used by listeners to append javascript
                    val jsresponse = IJavaScriptResponse { script ->
                        writeNormalEvaluations(response, setOf<CharSequence>(script))
                    }
                    it.forEach { listener ->
                        listener.onAfterRespond(components, jsresponse)
                    }
                }
            }
        }
    }
}
