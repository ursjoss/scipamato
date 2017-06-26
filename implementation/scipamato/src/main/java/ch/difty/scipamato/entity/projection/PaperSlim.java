package ch.difty.scipamato.entity.projection;

import javax.validation.constraints.NotNull;

import ch.difty.scipamato.entity.IdScipamatoEntity;

public class PaperSlim extends IdScipamatoEntity<Long> {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long number;
    @NotNull
    private String firstAuthor;
    @NotNull
    private Integer publicationYear;
    @NotNull
    private String title;

    public PaperSlim() {
        // default constructor
    }

    public PaperSlim(Long id, Long number, String firstAuthor, Integer publicationYear, String title) {
        setId(id);
        setNumber(number);
        setFirstAuthor(firstAuthor);
        setPublicationYear(publicationYear);
        setTitle(title);
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getFirstAuthor() {
        return firstAuthor;
    }

    public void setFirstAuthor(String firstAuthor) {
        this.firstAuthor = firstAuthor;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDisplayValue() {
        final StringBuilder sb = new StringBuilder();
        sb.append(firstAuthor).append(" (").append(publicationYear).append("): ");
        sb.append(title).append(".");
        return sb.toString();
    }

}
