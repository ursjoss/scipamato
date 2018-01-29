package ch.difty.scipamato.core.pubmed;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import ch.difty.scipamato.core.pubmed.api.Abstract;
import ch.difty.scipamato.core.pubmed.api.AbstractText;
import ch.difty.scipamato.core.pubmed.api.ArticleId;
import ch.difty.scipamato.core.pubmed.api.ArticleIdList;
import ch.difty.scipamato.core.pubmed.api.Author;
import ch.difty.scipamato.core.pubmed.api.AuthorList;
import ch.difty.scipamato.core.pubmed.api.CollectiveName;
import ch.difty.scipamato.core.pubmed.api.ForeName;
import ch.difty.scipamato.core.pubmed.api.Initials;
import ch.difty.scipamato.core.pubmed.api.LastName;
import ch.difty.scipamato.core.pubmed.api.PubmedArticle;
import ch.difty.scipamato.core.pubmed.api.PubmedBookArticle;
import ch.difty.scipamato.core.pubmed.api.Suffix;
import lombok.Data;

/**
 * Facade encapsulating both {@link PubmedArticle}s and
 * {@link PubmedBookArticle}s, making the relevant information accessible
 * through simple getters.
 *
 * @author u.joss
 */
@Data
public abstract class AbstractPubmedArticleFacade implements PubmedArticleFacade {

    protected static final String PAGE_SEPARATOR = "-";

    private String pmId;
    private String authors;
    private String firstAuthor;
    private String publicationYear;
    private String location;
    private String title;
    private String doi;
    private String originalAbstract;

    protected String getAuthorsFrom(final AuthorList authorList) {
        final String individualAuthors = getIndividualAuthors(authorList);
        final String collectives = getCollectives(authorList);
        return combine(individualAuthors, collectives);
    }

    private String getIndividualAuthors(final AuthorList authorList) {
        final List<String> names = new ArrayList<>();
        for (final Author author : authorList.getAuthor()) {
            final StringBuilder auth = parseIndividualAuthor(author);
            if (auth.length() > 0)
                names.add(auth.toString());
        }
        return names.stream()
            .collect(Collectors.joining(", ", "", ""));
    }

    private StringBuilder parseIndividualAuthor(final Author author) {
        final StringBuilder asb = new StringBuilder();
        for (final java.lang.Object o : author.getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName()) {
            if (o instanceof ForeName || o instanceof CollectiveName)
                continue;
            if (asb.length() > 0)
                asb.append(" ");
            if (o instanceof LastName)
                asb.append(((LastName) o).getvalue());
            else if (o instanceof Initials)
                asb.append(((Initials) o).getvalue());
            else
                asb.append(((Suffix) o).getvalue());
        }
        return asb;
    }

    private String getCollectives(final AuthorList authorList) {
        final List<String> names = new ArrayList<>();
        for (final Author author : authorList.getAuthor()) {
            final StringBuilder asb = parseCollectiveAuthor(author);
            if (asb.length() > 0)
                names.add(asb.toString());
        }
        return names.stream()
            .collect(Collectors.joining(", ", "", ""));
    }

    private StringBuilder parseCollectiveAuthor(final Author author) {
        final StringBuilder asb = new StringBuilder();
        for (final java.lang.Object o : author.getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName()) {
            if (o instanceof CollectiveName)
                asb.append(((CollectiveName) o).getvalue());
            else
                continue;
        }
        return asb;
    }

    private String combine(final String individualAuthors, final String collectives) {
        final StringBuilder comb = new StringBuilder();
        comb.append(individualAuthors);
        if (!StringUtils.isEmpty(collectives)) {
            if (comb.length() > 0)
                comb.append("; ");
            comb.append(collectives);
        }
        if (comb.length() > 0)
            comb.append(".");
        return comb.toString();
    }

    protected String getFirstAuthorFrom(final AuthorList authorList) {
        return authorList.getAuthor()
            .stream()
            .map(Author::getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName)
            .flatMap(List<java.lang.Object>::stream)
            .filter(o -> o instanceof LastName)
            .map(ln -> ((LastName) ln).getvalue())
            .limit(1)
            .findFirst()
            .orElse(authorList.getAuthor()
                .stream()
                .map(Author::getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName)
                .flatMap(List<java.lang.Object>::stream)
                .filter(o -> o instanceof CollectiveName)
                .map(cn -> ((CollectiveName) cn).getvalue())
                .limit(1)
                .findFirst()
                .orElse(""));
    }

    protected String getDoiFromArticleIdList(final ArticleIdList articleIdList) {
        if (articleIdList != null) {
            return articleIdList.getArticleId()
                .stream()
                .filter(ai -> "doi".equals(ai.getIdType()))
                .map(ArticleId::getvalue)
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    protected String getAbstractFrom(final Abstract abstr) {
        if (abstr == null)
            return null;
        return abstr.getAbstractText()
            .stream()
            .map(this::concatenateAbstract)
            .collect(Collectors.joining("\n"));
    }

    private String concatenateAbstract(final AbstractText a) {
        if (a.getLabel() != null)
            return a.getLabel() + ": " + a.getvalue();
        else
            return a.getvalue();
    }

}
