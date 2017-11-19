package ch.difty.scipamato.entity.filter;

import java.util.List;

import ch.difty.scipamato.entity.PopulationCode;
import ch.difty.scipamato.entity.StudyDesignCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PublicPaperFilter extends ScipamatoFilter {

    private static final long serialVersionUID = 1L;

    public static final String NUMBER = "number";
    public static final String AUTHOR_MASK = "authorMask";
    public static final String METHODS_MASK = "methodsMask";
    public static final String PUB_YEAR_FROM = "publicationYearFrom";
    public static final String PUB_YEAR_UNTIL = "publicationYearUntil";
    public static final String POPULATION_CODES = "populationCodes";
    public static final String STUDY_DESIGN_CODES = "studyDesignCodes";

    private Long number;
    private String authorMask;
    private String methodsMask;
    private Integer publicationYearFrom;
    private Integer publicationYearUntil;
    private List<PopulationCode> populationCodes;
    private List<StudyDesignCode> studyDesignCodes;

}
