package ch.difty.scipamato.core.pubmed;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

/**
 * Data Class providing the {@link PubmedArticleFacade} or an error specific message
 * providing information about the problem that prevented the retrieval of the article.
 */
@Getter
public class PubmedArticleResult {

    private static final Pattern ERROR_PATTERN  = Pattern.compile(".+\\{\"error\":\"([^\"]*)\",.+}", Pattern.DOTALL);
    private static final Pattern REASON_PATTERN = Pattern.compile(".+Reason: <strong>([^<]+)</strong>+.+",
        Pattern.DOTALL);

    private final PubmedArticleFacade pubmedArticleFacade;
    private final String              errorMessage;

    public PubmedArticleResult(final PubmedArticleFacade pubmedArticleFacade, final HttpStatus httpStatus,
        final String rawMessage) {
        this.pubmedArticleFacade = pubmedArticleFacade;
        this.errorMessage = evaluateMessageFrom(httpStatus, rawMessage);
    }

    private String evaluateMessageFrom(final HttpStatus httpStatus, final String rawMessage) {
        if (pubmedArticleFacade != null)
            return null;
        if (httpStatus == null)
            return rawMessage;
        switch (httpStatus) {
        case OK:
            return rawMessage;
        case BAD_REQUEST:
            return wrap(httpStatus, extractErrorFrom(rawMessage));
        case BAD_GATEWAY:
            return wrap(httpStatus, extractReasonFrom(rawMessage));
        default:
            return wrap(httpStatus, fullMessage(rawMessage));
        }
    }

    private String wrap(final HttpStatus status, final String msg) {
        return "Status " + status.toString() + msg;
    }

    private String extractErrorFrom(final String rawMessage) {
        return extractPattern(rawMessage, ERROR_PATTERN);
    }

    private String extractPattern(final String rawMessage, final Pattern pattern) {
        if (StringUtils.isNotEmpty(rawMessage)) {
            final Matcher matcher = pattern.matcher(rawMessage);
            if (matcher.matches())
                return prependColumn(matcher.group(1));
            else
                return prependColumn(rawMessage);
        }
        return "";
    }

    private String prependColumn(String msg) {
        return ": " + msg;
    }

    private String extractReasonFrom(final String rawMessage) {
        return extractPattern(rawMessage, REASON_PATTERN);
    }

    private String fullMessage(final String rawMessage) {
        if (StringUtils.isNotEmpty(rawMessage))
            return prependColumn(rawMessage);
        return "";
    }

}
