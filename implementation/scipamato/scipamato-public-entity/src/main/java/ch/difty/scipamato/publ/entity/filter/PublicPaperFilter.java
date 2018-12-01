package ch.difty.scipamato.publ.entity.filter;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.publ.entity.Code;
import ch.difty.scipamato.publ.entity.Keyword;
import ch.difty.scipamato.publ.entity.PopulationCode;
import ch.difty.scipamato.publ.entity.StudyDesignCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PublicPaperFilter extends ScipamatoFilter {

    private static final long serialVersionUID = 1L;

    private Long                  number;
    private String                authorMask;
    private String                titleMask;
    private String                methodsMask;
    private Integer               publicationYearFrom;
    private Integer               publicationYearUntil;
    private List<PopulationCode>  populationCodes;
    private List<StudyDesignCode> studyDesignCodes;
    private List<Code>            codesOfClass1;
    private List<Code>            codesOfClass2;
    private List<Code>            codesOfClass3;
    private List<Code>            codesOfClass4;
    private List<Code>            codesOfClass5;
    private List<Code>            codesOfClass6;
    private List<Code>            codesOfClass7;
    private List<Code>            codesOfClass8;
    private List<Keyword>         keywords;

    public enum PublicPaperFilterFields implements FieldEnumType {
        NUMBER("number"),
        AUTHOR_MASK("authorMask"),
        TITLE_MASK("titleMask"),
        METHODS_MASK("methodsMask"),
        PUB_YEAR_FROM("publicationYearFrom"),
        PUB_YEAR_UNTIL("publicationYearUntil"),
        POPULATION_CODES("populationCodes"),
        STUDY_DESIGN_CODES("studyDesignCodes"),
        CODES_OF_CLASS_1("codesOfClass1"),
        CODES_OF_CLASS_2("codesOfClass2"),
        CODES_OF_CLASS_3("codesOfClass3"),
        CODES_OF_CLASS_4("codesOfClass4"),
        CODES_OF_CLASS_5("codesOfClass5"),
        CODES_OF_CLASS_6("codesOfClass6"),
        CODES_OF_CLASS_7("codesOfClass7"),
        CODES_OF_CLASS_8("codesOfClass8"),
        KEYWORDS("keywords");

        private final String name;

        PublicPaperFilterFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
