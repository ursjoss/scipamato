package ch.difty.scipamato.publ.web.resources

import org.apache.wicket.request.resource.JavaScriptResourceReference

/**
 * JavaScriptResourceReference to pym.js allowing to use SciPaMaTo within a responsive iframe.
 * @see [https://blog.apps.npr.org/pym.js/](https://blog.apps.npr.org/pym.js/)
 */
object PymJavaScriptResourceReference : JavaScriptResourceReference(PymJavaScriptResourceReference::class.java, "js/pym.v1.js")
