package ch.difty.scipamato.core.entity.projection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.core.entity.IdScipamatoEntity;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaperSlim extends IdScipamatoEntity<Long> {

    private static final long serialVersionUID = 1L;

    @javax.validation.constraints.NotNull
    private Long    number;
    @javax.validation.constraints.NotNull
    private String  firstAuthor;
    @javax.validation.constraints.NotNull
    private Integer publicationYear;
    @javax.validation.constraints.NotNull
    private String  title;

    private NewsletterAssociation newsletterAssociation;

    /**
     * Constructs a PaperSlim (leaving the Newsletter null).
     *
     * @param id
     *     the technical paper database id
     * @param number
     *     the identifying business key
     * @param firstAuthor
     *     single first author
     * @param publicationYear
     *     publication year
     * @param title
     *     paper title
     */
    public PaperSlim(@Nullable Long id, @NotNull Long number, @NotNull String firstAuthor,
        @NotNull Integer publicationYear, @NotNull String title) {
        this(id, number, firstAuthor, publicationYear, title, null);
    }

    /**
     * Constructs a PaperSlim including Newsletter (constructed from the individual newsletter fields).
     *
     * @param id
     *     the technical paper database id
     * @param number
     *     the identifying business key
     * @param firstAuthor
     *     single first author
     * @param publicationYear
     *     publication year
     * @param title
     *     paper title
     * @param newsletterId
     *     the database id of the newsletter
     * @param newsletterTitle
     *     the newsletter title - must not be null
     * @param publicationStatusId
     *     the newsletter publication status id
     * @param headline
     *     the headline of the newsletter association
     */
    public PaperSlim(@Nullable Long id, @NotNull Long number, @NotNull String firstAuthor,
        @NotNull Integer publicationYear, @NotNull String title, int newsletterId, @NotNull String newsletterTitle,
        int publicationStatusId, @Nullable String headline) {
        this(id, number, firstAuthor, publicationYear, title,
            new NewsletterAssociation(newsletterId, newsletterTitle, publicationStatusId, headline));
    }

    /**
     * Constructs a PaperSlim including Newsletter.
     *
     * @param id
     *     the technical paper database id
     * @param number
     *     the identifying business key
     * @param firstAuthor
     *     single first author
     * @param publicationYear
     *     publication year
     * @param title
     *     paper title
     * @param newsletterAssociation
     *     the {@link NewsletterAssociation}
     */
    public PaperSlim(@Nullable Long id, @NotNull Long number, @NotNull String firstAuthor,
        @NotNull Integer publicationYear, @NotNull String title,
        @Nullable NewsletterAssociation newsletterAssociation) {
        setId(id);
        this.number = number;
        this.firstAuthor = firstAuthor;
        this.publicationYear = publicationYear;
        this.title = title;
        this.newsletterAssociation = newsletterAssociation;
    }

    @NotNull
    @Override
    public String getDisplayValue() {
        return firstAuthor + " (" + publicationYear + "): " + title + ".";
    }

    @NotNull
    @Override
    public String toString() {
        return "PaperSlim(number=" + number + ", firstAuthor=" + firstAuthor + ", publicationYear=" + publicationYear
               + ", title=" + title + (newsletterAssociation != null ?
                                           ", newsletter=" + newsletterAssociation.getIssue() :
                                           "") + (
                   newsletterAssociation != null && newsletterAssociation.getHeadline() != null ?
                       ", headline=" + newsletterAssociation.getHeadline() :
                       "") + ")";
    }
}
