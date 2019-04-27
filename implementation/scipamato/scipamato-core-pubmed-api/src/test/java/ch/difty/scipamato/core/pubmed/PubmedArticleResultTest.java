package ch.difty.scipamato.core.pubmed;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class PubmedArticleResultTest {

    @Mock
    private PubmedArticleFacade paf;

    private void assertPar(PubmedArticleResult par, final PubmedArticleFacade paf, final String msg) {
        assertThat(par.getPubmedArticleFacade()).isEqualTo(paf);
        assertThat(par.getErrorMessage()).isEqualTo(msg);
    }

    @Test
    void withPubmedArticleFacade_messageIsAlwaysNull() {
        assertPar(new PubmedArticleResult(paf, null, null), paf, null);
        assertPar(new PubmedArticleResult(paf, HttpStatus.OK, "foo"), paf, null);
        assertPar(new PubmedArticleResult(paf, null, "foo"), paf, null);
        assertPar(new PubmedArticleResult(paf, HttpStatus.BAD_GATEWAY, "foo"), paf, null);
    }

    @Test
    void withNoPubmedArticleFacade_withNullStatus_messageIsRawMessage() {
        final HttpStatus status = null;
        assertPar(new PubmedArticleResult(null, status, "foo"), null, "foo");
        assertPar(new PubmedArticleResult(null, status, null), null, null);
    }

    @Test
    void withNoFacade_withStatus200_messageIsRawMessage() {
        final HttpStatus status = HttpStatus.OK;
        assertPar(new PubmedArticleResult(null, status, "foo"), null, "foo");
        assertPar(new PubmedArticleResult(null, status, null), null, null);
    }

    @Test
    void withNoFacade_withStatus400_withNullMessage_messageIsHttpStatus() {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        final String msg = null;
        assertPar(new PubmedArticleResult(null, status, msg), null, "Status 400 BAD_REQUEST");
    }

    @Test
    void withNoFacade_withStatus400_witValidMessage_messageIsHttpStatusPlusErrorTag() {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        final String msg = "status 400 reading PubMed#articleWithId(String,String); content:\n"
                           + "{\"error\":\"API key invalid\",\"api-key\":\"xxx\",\"type\":\"invalid\",\"status\":\"unknown\"}";
        assertPar(new PubmedArticleResult(null, status, msg), null, "Status 400 BAD_REQUEST: API key invalid");
    }

    @Test
    void withNoFacade_withStatus400_witValidMessage_messageIsHttpStatusPlusRawMessage() {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        final String msg = "status 400 reading PubMed#articleWithId(String,String); content:\n" + "bar";
        assertPar(new PubmedArticleResult(null, status, msg), null,
            "Status 400 BAD_REQUEST: status 400 reading PubMed#articleWithId(String,String); content:\nbar");
    }

    @Test
    void withNoFacade_withStatus502_withNullMessage_messageIsHttpStatus() {
        final HttpStatus status = HttpStatus.BAD_GATEWAY;
        final String msg = null;
        assertPar(new PubmedArticleResult(null, status, msg), null, "Status 502 BAD_GATEWAY");
    }

    @Test
    void withNoFacade_withStatus502_witValidMessage_messageIsHttpStatusPlusReason() {
        final HttpStatus status = HttpStatus.BAD_GATEWAY;
        final String msg = "status 502 reading PubMed#articleWithId(String,String); content:\n"
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
                           + "</address>\n" + "</body>\n" + "</html>\n";
        assertPar(new PubmedArticleResult(null, status, msg), null,
            "Status 502 BAD_GATEWAY: Error reading from remote server");
    }

    @Test
    void withNoFacade_withStatus502_witValidMessage_messageIsHttpStatusPlusRawMessage() {
        final HttpStatus status = HttpStatus.BAD_GATEWAY;
        final String msg = "status 502 reading PubMed#articleWithId(String,String); content:\n"
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
                           + "<body>\n" + "</body>\n" + "</html>\n";
        assertPar(new PubmedArticleResult(null, status, msg), null,
            "Status 502 BAD_GATEWAY: status 502 reading PubMed#articleWithId(String,String); content:\n"
            + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
            + "  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">\n" + "<head>\n"
            + "<title>Bad Gateway!</title>\n" + "<link rev=\"made\" href=\"mailto:info@ncbi.nlm.nih.gov\" />\n"
            + "<style type=\"text/css\"><!--/*--><![CDATA[/*><!--*/ \n"
            + "    body { color: #000000; background-color: #FFFFFF; }\n" + "    a:link { color: #0000CC; }\n"
            + "    p, address {margin-left: 3em;}\n" + "    span {font-size: smaller;}\n" + "/*]]>*/--></style>\n"
            + "</head>\n" + "\n" + "<body>\n" + "</body>\n" + "</html>\n");
    }

    @Test
    void withNoFacade_withUnhandledStatus_messageIsRawMessage() {
        final HttpStatus status = HttpStatus.FAILED_DEPENDENCY;
        assertPar(new PubmedArticleResult(null, status, "foo\nbar"), null, "Status 424 FAILED_DEPENDENCY: foo\nbar");
        assertPar(new PubmedArticleResult(null, status, null), null, "Status 424 FAILED_DEPENDENCY");
    }

}