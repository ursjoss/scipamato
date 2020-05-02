package ch.difty.scipamato.core.pubmed

import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

@Suppress("SpellCheckingInspection")
internal class PubmedArticleResultTest {

    private val paf = mockk<PubmedArticleFacade>()

    private fun assertPar(par: PubmedArticleResult, paf: PubmedArticleFacade?, msg: String) {
        par.pubmedArticleFacade shouldBeEqualTo paf
        par.errorMessage shouldBeEqualTo msg
    }

    @Test
    fun withPubmedArticleFacade_messageIsAlwaysEmpty() {
        assertPar(PubmedArticleResult(paf, null, null), paf, "")
        assertPar(PubmedArticleResult(paf, HttpStatus.OK, "foo"), paf, "")
        assertPar(PubmedArticleResult(paf, null, "foo"), paf, "")
        assertPar(PubmedArticleResult(paf, HttpStatus.BAD_GATEWAY, "foo"), paf, "")
    }

    @Test
    fun withNoPubmedArticleFacade_withNullStatus_messageIsRawMessage() {
        val status: HttpStatus? = null
        assertPar(PubmedArticleResult(null, status, "foo"), null, "foo")
        assertPar(PubmedArticleResult(null, status, null), null, "")
    }

    @Test
    fun withNoFacade_withStatus200_messageIsRawMessage() {
        val status = HttpStatus.OK
        assertPar(PubmedArticleResult(null, status, "foo"), null, "foo")
        assertPar(PubmedArticleResult(null, status, null), null, "")
    }

    @Test
    fun withNoFacade_withStatus400_withNullMessage_messageIsHttpStatus() {
        val status = HttpStatus.BAD_REQUEST
        val msg: String? = null
        assertPar(PubmedArticleResult(null, status, msg), null, "Status 400 BAD_REQUEST")
    }

    @Test
    fun withNoFacade_withStatus400_witValidMessage_messageIsHttpStatusPlusErrorTag() {
        val status = HttpStatus.BAD_REQUEST
        val msg =
            """status 400 reading PubMed#articleWithId(String,String); content:
                |{"error":"API key invalid","api-key":"xxx","type":"invalid","status":"unknown"}""".trimMargin()
        assertPar(PubmedArticleResult(null, status, msg), null, "Status 400 BAD_REQUEST: API key invalid")
    }

    @Test
    fun withNoFacade_withStatus400_witValidMessage_messageIsHttpStatusPlusRawMessage() {
        val status = HttpStatus.BAD_REQUEST
        val msg = "status 400 reading PubMed#articleWithId(String,String); content:\nbar"
        assertPar(PubmedArticleResult(null, status, msg), null,
            "Status 400 BAD_REQUEST: status 400 reading PubMed#articleWithId(String,String); content:\nbar")
    }

    @Test
    fun withNoFacade_withStatus502_withNullMessage_messageIsHttpStatus() {
        val status = HttpStatus.BAD_GATEWAY
        val msg: String? = null
        assertPar(PubmedArticleResult(null, status, msg), null, "Status 502 BAD_GATEWAY")
    }

    @Suppress("LongMethod", "MaxLineLength")
    @Test
    fun withNoFacade_withStatus502_witValidMessage_messageIsHttpStatusPlusReason() {
        val status = HttpStatus.BAD_GATEWAY
        val msg = (
            """status 502 reading PubMed#articleWithId(String,String); content:   
                    |<?xml version="1.0" encoding="UTF-8"?>
                    |<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
                    |  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
                    |<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
                    |<head>
                    |<title>Bad Gateway!</title>
                    |<link rev="made" href="mailto:info@ncbi.nlm.nih.gov" />
                    |<style type="text/css"><!--/*--><![CDATA[/*><!--*/ 
                    |    body { color: #000000; background-color: #FFFFFF; }
                    |    a:link { color: #0000CC; }
                    |    p, address {margin-left: 3em;}
                    |    span {font-size: smaller;}
                    |/*]]>*/--></style>
                    |</head>
                    |
                    |<body>
                    |<h1>Bad Gateway!</h1>
                    |<p>
                    |
                    |
                    |    The proxy server received an invalid
                    |    response from an upstream server.
                    |
                    |  
                    |    </p>
                    |<p>
                    |
                    |    The proxy server could not handle the request <em><a href="/entrez/eutils/efetch.fcgi">GET&nbsp;/entrez/eutils/efetch.fcgi</a></em>.<p>
                    |Reason: <strong>Error reading from remote server</strong></p>
                    |  
                    |    
                    |</p>
                    |<p>
                    |If you think this is a server error, please contact
                    |the <a href="mailto:info@ncbi.nlm.nih.gov">webmaster</a>.
                    |
                    |</p>
                    |
                    |
                    |<h2>Error 502</h2>                                                                                                                                                                                                                                                    [301/810]
                    |<address>
                    |  <a href="/">eutils.ncbi.nlm.nih.gov</a><br />
                    |  <span>Apache</span>
                    |</address>
                    |</body>
                    |</html>
                    |
                    |
                    |2019-01-24 12:26:40.303 ERROR 25027 --- [XNIO-1 task-47] c.d.s.core.pubmed.PubmedXmlService       : Unexpected error: status 502 reading PubMed#articleWithId(String,String); content:
                    |<?xml version="1.0" encoding="UTF-8"?>
                    |<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
                    |  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
                    |<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
                    |<head>
                    |<title>Bad Gateway!</title>
                    |<link rev="made" href="mailto:info@ncbi.nlm.nih.gov" />
                    |<style type="text/css"><!--/*--><![CDATA[/*><!--*/ 
                    |    body { color: #000000; background-color: #FFFFFF; }
                    |    a:link { color: #0000CC; }
                    |    p, address {margin-left: 3em;}
                    |    span {font-size: smaller;}
                    |/*]]>*/--></style>
                    |</head>
                    |
                    |<body>
                    |<h1>Bad Gateway!</h1>
                    |<p>
                    |
                    |
                    |    The proxy server received an invalid
                    |    response from an upstream server.
                    |
                    |  
                    |    </p>
                    |<p>
                    |
                    |    The proxy server could not handle the request <em><a href="/entrez/eutils/efetch.fcgi">GET&nbsp;/entrez/eutils/efetch.fcgi</a></em>.<p>
                    |Reason: <strong>Error reading from remote server</strong></p>
                    |  
                    |    
                    |</p>
                    |<p>
                    |If you think this is a server error, please contact
                    |the <a href="mailto:info@ncbi.nlm.nih.gov">webmaster</a>.
                    |
                    |</p>
                    |
                    |<h2>Error 502</h2>
                    |<address>
                    |  <a href="/">eutils.ncbi.nlm.nih.gov</a><br />
                    |  <span>Apache</span>
                    |</address>
                    |</body>
                    |</html>""".trimMargin()
            )
        assertPar(PubmedArticleResult(null, status, msg), null,
            "Status 502 BAD_GATEWAY: Error reading from remote server")
    }

    @Test
    fun withNoFacade_withStatus502_witValidMessage_messageIsHttpStatusPlusRawMessage() {
        val status = HttpStatus.BAD_GATEWAY
        val msg = (
            """status 502 reading PubMed#articleWithId(String,String); content:
                |<?xml version="1.0" encoding="UTF-8"?>
                |<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
                |  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
                |<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
                |<head>
                |<title>Bad Gateway!</title>
                |<link rev="made" href="mailto:info@ncbi.nlm.nih.gov" />
                |<style type="text/css"><!--/*--><![CDATA[/*><!--*/ 
                |    body { color: #000000; background-color: #FFFFFF; }
                |    a:link { color: #0000CC; }
                |    p, address {margin-left: 3em;}
                |    span {font-size: smaller;}
                |/*]]>*/--></style>
                |</head>
                |
                |<body>
                |</body>
                |</html>""".trimMargin()
            )
        assertPar(PubmedArticleResult(null, status, msg), null,
            """Status 502 BAD_GATEWAY: status 502 reading PubMed#articleWithId(String,String); content:
            |<?xml version="1.0" encoding="UTF-8"?>
            |<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
            |  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
            |<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
            |<head>
            |<title>Bad Gateway!</title>
            |<link rev="made" href="mailto:info@ncbi.nlm.nih.gov" />
            |<style type="text/css"><!--/*--><![CDATA[/*><!--*/ 
            |    body { color: #000000; background-color: #FFFFFF; }
            |    a:link { color: #0000CC; }
            |    p, address {margin-left: 3em;}
            |    span {font-size: smaller;}
            |/*]]>*/--></style>
            |</head>
            |
            |<body>
            |</body>
            |</html>""".trimMargin()
        )
    }

    @Test
    fun withNoFacade_withUnhandledStatus_messageIsRawMessage() {
        val status = HttpStatus.FAILED_DEPENDENCY
        assertPar(PubmedArticleResult(null, status, "foo\nbar"), null, "Status 424 FAILED_DEPENDENCY: foo\nbar")
        assertPar(PubmedArticleResult(null, status, null), null, "Status 424 FAILED_DEPENDENCY")
    }
}
