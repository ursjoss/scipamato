package ch.difty.scipamato.core.pubmed

import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

internal class PubmedArticleResultTest {

    private val paf = mock<PubmedArticleFacade>()

    private fun assertPar(par: PubmedArticleResult, paf: PubmedArticleFacade?, msg: String?) {
        assertThat(par.pubmedArticleFacade).isEqualTo(paf)
        assertThat(par.errorMessage).isEqualTo(msg)
    }

    @Test
    fun withPubmedArticleFacade_messageIsAlwaysNull() {
        assertPar(PubmedArticleResult(paf, null, null), paf, null)
        assertPar(PubmedArticleResult(paf, HttpStatus.OK, "foo"), paf, null)
        assertPar(PubmedArticleResult(paf, null, "foo"), paf, null)
        assertPar(PubmedArticleResult(paf, HttpStatus.BAD_GATEWAY, "foo"), paf, null)
    }

    @Test
    fun withNoPubmedArticleFacade_withNullStatus_messageIsRawMessage() {
        val status: HttpStatus? = null
        assertPar(PubmedArticleResult(null, status, "foo"), null, "foo")
        assertPar(PubmedArticleResult(null, status, null), null, null)
    }

    @Test
    fun withNoFacade_withStatus200_messageIsRawMessage() {
        val status = HttpStatus.OK
        assertPar(PubmedArticleResult(null, status, "foo"), null, "foo")
        assertPar(PubmedArticleResult(null, status, null), null, null)
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
        val msg = "status 400 reading PubMed#articleWithId(String,String); content:\n" + "{\"error\":\"API key invalid\",\"api-key\":\"xxx\",\"type\":\"invalid\",\"status\":\"unknown\"}"
        assertPar(PubmedArticleResult(null, status, msg), null, "Status 400 BAD_REQUEST: API key invalid")
    }

    @Test
    fun withNoFacade_withStatus400_witValidMessage_messageIsHttpStatusPlusRawMessage() {
        val status = HttpStatus.BAD_REQUEST
        val msg = "status 400 reading PubMed#articleWithId(String,String); content:\n" + "bar"
        assertPar(PubmedArticleResult(null, status, msg), null,
                "Status 400 BAD_REQUEST: status 400 reading PubMed#articleWithId(String,String); content:\nbar")
    }

    @Test
    fun withNoFacade_withStatus502_withNullMessage_messageIsHttpStatus() {
        val status = HttpStatus.BAD_GATEWAY
        val msg: String? = null
        assertPar(PubmedArticleResult(null, status, msg), null, "Status 502 BAD_GATEWAY")
    }

    @Test
    fun withNoFacade_withStatus502_witValidMessage_messageIsHttpStatusPlusReason() {
        val status = HttpStatus.BAD_GATEWAY
        val msg = ("status 502 reading PubMed#articleWithId(String,String); content:\n"
                + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
                + "  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">\n" + "<head>\n"
                + "<title>Bad Gateway!</title>\n"
                + "<link rev=\"made\" href=\"mailto:info@ncbi.nlm.nih.gov\" />\n"
                + "<style type=\"text/css\"><!--/*--><![CDATA[/*><!--*/ \n"
                + "    body { color: #000000; background-color: #FFFFFF; }\n"
                + "    a:link { color: #0000CC; }\n" + "    p, address {margin-left: 3em;}\n"
                + "    span {font-size: smaller;}\n" + "/*]]>*/--></style>\n" + "</head>\n" + "\n"
                + "<body>\n" + "<h1>Bad Gateway!</h1>\n" + "<p>\n" + "\n" + "\n"
                + "    The proxy server received an invalid\n" + "    response from an upstream server.\n"
                + "\n" + "  \n" + "    </p>\n" + "<p>\n" + "\n"
                + "    The proxy server could not handle the request <em><a href=\"/entrez/eutils/efetch.fcgi\">GET&nbsp;/entrez/eutils/efetch.fcgi</a></em>.<p>\n"
                + "Reason: <strong>Error reading from remote server</strong></p>\n" + "  \n" + "    \n"
                + "</p>\n" + "<p>\n" + "If you think this is a server error, please contact\n"
                + "the <a href=\"mailto:info@ncbi.nlm.nih.gov\">webmaster</a>.\n" + "\n" + "</p>\n" + "\n"
                + "\n"
                + "<h2>Error 502</h2>                                                                                                                                                                                                                                                    [301/810]\n"
                + "<address>\n" + "  <a href=\"/\">eutils.ncbi.nlm.nih.gov</a><br />\n"
                + "  <span>Apache</span>\n" + "</address>\n" + "</body>\n" + "</html>\n" + "\n" + "\n"
                + "2019-01-24 12:26:40.303 ERROR 25027 --- [XNIO-1 task-47] c.d.s.core.pubmed.PubmedXmlService       : Unexpected error: status 502 reading PubMed#articleWithId(String,String); content:\n"
                + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
                + "  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">\n" + "<head>\n"
                + "<title>Bad Gateway!</title>\n"
                + "<link rev=\"made\" href=\"mailto:info@ncbi.nlm.nih.gov\" />\n"
                + "<style type=\"text/css\"><!--/*--><![CDATA[/*><!--*/ \n"
                + "    body { color: #000000; background-color: #FFFFFF; }\n"
                + "    a:link { color: #0000CC; }\n" + "    p, address {margin-left: 3em;}\n"
                + "    span {font-size: smaller;}\n" + "/*]]>*/--></style>\n" + "</head>\n" + "\n"
                + "<body>\n" + "<h1>Bad Gateway!</h1>\n" + "<p>\n" + "\n" + "\n"
                + "    The proxy server received an invalid\n" + "    response from an upstream server.\n"
                + "\n" + "  \n" + "    </p>\n" + "<p>\n" + "\n"
                + "    The proxy server could not handle the request <em><a href=\"/entrez/eutils/efetch.fcgi\">GET&nbsp;/entrez/eutils/efetch.fcgi</a></em>.<p>\n"
                + "Reason: <strong>Error reading from remote server</strong></p>\n" + "  \n" + "    \n"
                + "</p>\n" + "<p>\n" + "If you think this is a server error, please contact\n"
                + "the <a href=\"mailto:info@ncbi.nlm.nih.gov\">webmaster</a>.\n" + "\n" + "</p>\n" + "\n"
                + "<h2>Error 502</h2>\n" + "<address>\n"
                + "  <a href=\"/\">eutils.ncbi.nlm.nih.gov</a><br />\n" + "  <span>Apache</span>\n"
                + "</address>\n" + "</body>\n" + "</html>\n")
        assertPar(PubmedArticleResult(null, status, msg), null,
                "Status 502 BAD_GATEWAY: Error reading from remote server")
    }

    @Test
    fun withNoFacade_withStatus502_witValidMessage_messageIsHttpStatusPlusRawMessage() {
        val status = HttpStatus.BAD_GATEWAY
        val msg = ("status 502 reading PubMed#articleWithId(String,String); content:\n"
                + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
                + "  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">\n" + "<head>\n"
                + "<title>Bad Gateway!</title>\n"
                + "<link rev=\"made\" href=\"mailto:info@ncbi.nlm.nih.gov\" />\n"
                + "<style type=\"text/css\"><!--/*--><![CDATA[/*><!--*/ \n"
                + "    body { color: #000000; background-color: #FFFFFF; }\n"
                + "    a:link { color: #0000CC; }\n" + "    p, address {margin-left: 3em;}\n"
                + "    span {font-size: smaller;}\n" + "/*]]>*/--></style>\n" + "</head>\n" + "\n"
                + "<body>\n" + "</body>\n" + "</html>\n")
        assertPar(PubmedArticleResult(null, status, msg), null,
                "Status 502 BAD_GATEWAY: status 502 reading PubMed#articleWithId(String,String); content:\n"
                        + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
                        + "  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                        + "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">\n" + "<head>\n"
                        + "<title>Bad Gateway!</title>\n" + "<link rev=\"made\" href=\"mailto:info@ncbi.nlm.nih.gov\" />\n"
                        + "<style type=\"text/css\"><!--/*--><![CDATA[/*><!--*/ \n"
                        + "    body { color: #000000; background-color: #FFFFFF; }\n" + "    a:link { color: #0000CC; }\n"
                        + "    p, address {margin-left: 3em;}\n" + "    span {font-size: smaller;}\n" + "/*]]>*/--></style>\n"
                        + "</head>\n" + "\n" + "<body>\n" + "</body>\n" + "</html>\n"
        )
    }

    @Test
    fun withNoFacade_withUnhandledStatus_messageIsRawMessage() {
        val status = HttpStatus.FAILED_DEPENDENCY
        assertPar(PubmedArticleResult(null, status, "foo\nbar"), null, "Status 424 FAILED_DEPENDENCY: foo\nbar")
        assertPar(PubmedArticleResult(null, status, null), null, "Status 424 FAILED_DEPENDENCY")
    }

}