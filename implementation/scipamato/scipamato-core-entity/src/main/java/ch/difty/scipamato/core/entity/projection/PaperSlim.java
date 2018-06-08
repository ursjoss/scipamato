package ch.difty.scipamato.core.entity.projection;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaperSlim extends IdScipamatoEntity<Long> {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long    number;
    @NotNull
    private String  firstAuthor;
    @NotNull
    private Integer publicationYear;
    @NotNull
    private String  title;

    private NewsletterAssociation newsletterAssociation;

    public enum PaperSlimFields implements FieldEnumType {
        NUMBER("number"),
        FIRST_AUTHOR("firstAuthor"),
        PUBLICATION_YEAR("publicationYear"),
        TITLE("title"),
        NEWSLETTER_ASSOCIATION("newsletterAssociation");

        private final String name;

        PaperSlimFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

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
    public PaperSlim(Long id, Long number, String firstAuthor, Integer publicationYear, String title) {
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
    public PaperSlim(Long id, Long number, String firstAuthor, Integer publicationYear, String title, int newsletterId,
        String newsletterTitle, int publicationStatusId, String headline) {
        this(id, number, firstAuthor, publicationYear, title,
            new NewsletterAssociation(newsletterId, AssertAs.notNull(newsletterTitle, "newsletterTitle"),
                publicationStatusId, headline));
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
    public PaperSlim(Long id, Long number, String firstAuthor, Integer publicationYear, String title,
        NewsletterAssociation newsletterAssociation) {
        setId(id);
        setNumber(number);
        setFirstAuthor(firstAuthor);
        setPublicationYear(publicationYear);
        setTitle(title);
        setNewsletterAssociation(newsletterAssociation);
    }

    @Override
    public String getDisplayValue() {
        return firstAuthor + " (" + publicationYear + "): " + title + ".";
    }

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
