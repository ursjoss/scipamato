package ch.difty.scipamato.entity.filter;

import java.util.List;

import ch.difty.scipamato.entity.Code;
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

    public static final String CODES_OF_CLASS_1 = "codesOfClass1";
    public static final String CODES_OF_CLASS_2 = "codesOfClass2";
    public static final String CODES_OF_CLASS_3 = "codesOfClass3";
    public static final String CODES_OF_CLASS_4 = "codesOfClass4";
    public static final String CODES_OF_CLASS_5 = "codesOfClass5";
    public static final String CODES_OF_CLASS_6 = "codesOfClass6";
    public static final String CODES_OF_CLASS_7 = "codesOfClass7";
    public static final String CODES_OF_CLASS_8 = "codesOfClass8";

    private Long number;
    private String authorMask;
    private String methodsMask;
    private Integer publicationYearFrom;
    private Integer publicationYearUntil;
    private List<PopulationCode> populationCodes;
    private List<StudyDesignCode> studyDesignCodes;
    private List<Code> codesOfClass1;
    private List<Code> codesOfClass2;
    private List<Code> codesOfClass3;
    private List<Code> codesOfClass4;
    private List<Code> codesOfClass5;
    private List<Code> codesOfClass6;
    private List<Code> codesOfClass7;
    private List<Code> codesOfClass8;

}
