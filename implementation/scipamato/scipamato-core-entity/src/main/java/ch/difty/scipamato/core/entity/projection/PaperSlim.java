package ch.difty.scipamato.core.entity.projection;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    public PaperSlim(Long id, Long number, String firstAuthor, Integer publicationYear, String title) {
        setId(id);
        setNumber(number);
        setFirstAuthor(firstAuthor);
        setPublicationYear(publicationYear);
        setTitle(title);
    }

    @Override
    public String getDisplayValue() {
        return firstAuthor + " (" + publicationYear + "): " + title + ".";
    }

}
