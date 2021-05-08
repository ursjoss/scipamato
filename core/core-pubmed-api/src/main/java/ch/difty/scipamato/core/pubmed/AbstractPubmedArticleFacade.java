package ch.difty.scipamato.core.pubmed;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.StringUtils;

import ch.difty.scipamato.core.pubmed.api.*;

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

    @Nullable
    private String pmId;
    private String authors;
    private String firstAuthor;
    private String publicationYear;
    private String location;
    private String title;
    private String doi;
    private String originalAbstract;

    @NotNull
    String getAuthorsFrom(@NotNull final AuthorList authorList) {
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
        return names
            .stream()
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
        return names
            .stream()
            .collect(Collectors.joining(", ", "", ""));
    }

    private StringBuilder parseCollectiveAuthor(final Author author) {
        final StringBuilder asb = new StringBuilder();
        for (final java.lang.Object o : author.getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName()) {
            if (o instanceof CollectiveName)
                asb.append(((CollectiveName) o).getvalue());
        }
        return asb;
    }

    private String combine(final String individualAuthors, final String collectives) {
        final StringBuilder comb = new StringBuilder();
        comb.append(individualAuthors);
        if (StringUtils.hasLength(collectives)) {
            if (comb.length() > 0)
                comb.append("; ");
            comb.append(collectives);
        }
        if (comb.length() > 0)
            comb.append(".");
        return comb.toString();
    }

    @NotNull
    String getFirstAuthorFrom(@NotNull final AuthorList authorList) {
        return authorList
            .getAuthor()
            .stream()
            .map(Author::getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName)
            .flatMap(List<java.lang.Object>::stream)
            .filter(LastName.class::isInstance)
            .map(ln -> ((LastName) ln).getvalue())
            .limit(1)
            .findFirst()
            .orElse(authorList
                .getAuthor()
                .stream()
                .map(Author::getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName)
                .flatMap(List<java.lang.Object>::stream)
                .filter(CollectiveName.class::isInstance)
                .map(cn -> ((CollectiveName) cn).getvalue())
                .limit(1)
                .findFirst()
                .orElse(""));
    }

    @Nullable
    String getDoiFromArticleIdList(@Nullable final ArticleIdList articleIdList) {
        if (articleIdList != null) {
            return articleIdList
                .getArticleId()
                .stream()
                .filter(ai -> "doi".equals(ai.getIdType()))
                .map(ArticleId::getvalue)
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    @Nullable
    String getAbstractFrom(@Nullable final Abstract abstr) {
        if (abstr == null)
            return null;
        return abstr
            .getAbstractText()
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
