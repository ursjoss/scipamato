package ch.difty.scipamato.publ.entity.filter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import ch.difty.scipamato.publ.entity.Code;
import ch.difty.scipamato.publ.entity.PopulationCode;
import ch.difty.scipamato.publ.entity.StudyDesignCode;

public class PublicPaperFilterTest {

    @Test
    public void construct() {
        PublicPaperFilter filter = new PublicPaperFilter();
        filter.setNumber(1L);
        filter.setAuthorMask("am");
        filter.setMethodsMask("mm");
        filter.setPublicationYearFrom(2000);
        filter.setPublicationYearUntil(3000);
        filter.setPopulationCodes(Arrays.asList(PopulationCode.CHILDREN, PopulationCode.ADULTS));
        filter.setStudyDesignCodes(Collections.singletonList(StudyDesignCode.EXPERIMENTAL));
        filter.setCodesOfClass1(List.of(newCode("1A"), newCode("1B")));
        filter.setCodesOfClass2(List.of(newCode("2A"), newCode("2B")));
        filter.setCodesOfClass3(List.of(newCode("3A"), newCode("3B")));
        filter.setCodesOfClass4(List.of(newCode("4A"), newCode("4B")));
        filter.setCodesOfClass5(List.of(newCode("5A"), newCode("5B")));
        filter.setCodesOfClass6(List.of(newCode("6A"), newCode("6B")));
        filter.setCodesOfClass7(List.of(newCode("7A"), newCode("7B")));
        filter.setCodesOfClass8(List.of(newCode("8A"), newCode("8B")));
        filter.setKeywords(List.of(new Keyword(1, 1,"de", "k1", null)));

        assertThat(filter.getNumber()).isEqualTo(1L);
        assertThat(filter.getAuthorMask()).isEqualTo("am");
        assertThat(filter.getMethodsMask()).isEqualTo("mm");
        assertThat(filter.getPublicationYearFrom()).isEqualTo(2000);
        assertThat(filter.getPublicationYearUntil()).isEqualTo(3000);

        assertThat(filter.getPopulationCodes()).contains(PopulationCode.CHILDREN, PopulationCode.ADULTS);
        assertThat(filter.getStudyDesignCodes()).contains(StudyDesignCode.EXPERIMENTAL);

        assertThat(filter.getCodesOfClass1()).hasSize(2);
        assertThat(filter.getCodesOfClass2()).hasSize(2);
        assertThat(filter.getCodesOfClass3()).hasSize(2);
        assertThat(filter.getCodesOfClass4()).hasSize(2);
        assertThat(filter.getCodesOfClass5()).hasSize(2);
        assertThat(filter.getCodesOfClass6()).hasSize(2);
        assertThat(filter.getCodesOfClass7()).hasSize(2);
        assertThat(filter.getCodesOfClass8()).hasSize(2);

        assertThat(filter.toString()).isEqualTo(
            "PublicPaperFilter(number=1, authorMask=am, methodsMask=mm, publicationYearFrom=2000, publicationYearUntil=3000, populationCodes=[CHILDREN, ADULTS], studyDesignCodes=[EXPERIMENTAL], "
            + "codesOfClass1=[Code(codeClassId=1, code=1A, langCode=en, name=null, comment=null, sort=0), Code(codeClassId=1, code=1B, langCode=en, name=null, comment=null, sort=0)], "
            + "codesOfClass2=[Code(codeClassId=2, code=2A, langCode=en, name=null, comment=null, sort=0), Code(codeClassId=2, code=2B, langCode=en, name=null, comment=null, sort=0)], "
            + "codesOfClass3=[Code(codeClassId=3, code=3A, langCode=en, name=null, comment=null, sort=0), Code(codeClassId=3, code=3B, langCode=en, name=null, comment=null, sort=0)], "
            + "codesOfClass4=[Code(codeClassId=4, code=4A, langCode=en, name=null, comment=null, sort=0), Code(codeClassId=4, code=4B, langCode=en, name=null, comment=null, sort=0)], "
            + "codesOfClass5=[Code(codeClassId=5, code=5A, langCode=en, name=null, comment=null, sort=0), Code(codeClassId=5, code=5B, langCode=en, name=null, comment=null, sort=0)], "
            + "codesOfClass6=[Code(codeClassId=6, code=6A, langCode=en, name=null, comment=null, sort=0), Code(codeClassId=6, code=6B, langCode=en, name=null, comment=null, sort=0)], "
            + "codesOfClass7=[Code(codeClassId=7, code=7A, langCode=en, name=null, comment=null, sort=0), Code(codeClassId=7, code=7B, langCode=en, name=null, comment=null, sort=0)], "
            + "codesOfClass8=[Code(codeClassId=8, code=8A, langCode=en, name=null, comment=null, sort=0), Code(codeClassId=8, code=8B, langCode=en, name=null, comment=null, sort=0)], "
            + "keywords=[Keyword(id=1, keywordId=1, langCode=de, name=k1, searchOverride=null)])");
    }

    private Code newCode(String code) {
        return Code
            .builder()
            .code(code)
            .codeClassId(Integer.parseInt(code.substring(0, 1)))
            .langCode("en")
            .build();
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(PublicPaperFilter.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    public void assertEnumFields() {
        assertThat(PublicPaperFilter.PublicPaperFilterFields.values())
            .extracting("name")
            .containsExactly("number", "authorMask", "methodsMask", "publicationYearFrom", "publicationYearUntil",
                "populationCodes", "studyDesignCodes", "codesOfClass1", "codesOfClass2", "codesOfClass3",
                "codesOfClass4", "codesOfClass5", "codesOfClass6", "codesOfClass7", "codesOfClass8");
    }
}
