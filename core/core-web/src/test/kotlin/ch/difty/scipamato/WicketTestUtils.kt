package ch.difty.scipamato

import org.apache.wicket.protocol.http.FetchMetadataResourceIsolationPolicy.SAME_SITE
import org.apache.wicket.protocol.http.FetchMetadataResourceIsolationPolicy.SEC_FETCH_SITE_HEADER
import org.apache.wicket.util.tester.FormTester
import org.apache.wicket.util.tester.WicketTester

fun WicketTester.clickLinkSameSite(path: String) {
    addRequestHeader(SEC_FETCH_SITE_HEADER, SAME_SITE)
    clickLink(path)
}

fun WicketTester.newFormTesterSameSite(path: String, fillBlankString: Boolean = true): FormTester {
    addRequestHeader(SEC_FETCH_SITE_HEADER, SAME_SITE)
    return newFormTester(path, fillBlankString)
}

fun WicketTester.submitFormSameSite(path: String): Unit {
    addRequestHeader(SEC_FETCH_SITE_HEADER, SAME_SITE)
    submitForm(path)
}
