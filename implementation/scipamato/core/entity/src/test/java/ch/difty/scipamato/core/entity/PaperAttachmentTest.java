package ch.difty.scipamato.core.entity;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID;
import static ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID;
import static ch.difty.scipamato.core.entity.PaperAttachment.PaperAttachmentFields.CONTENT;
import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class PaperAttachmentTest {

    private final PaperAttachment pa = new PaperAttachment(1, 2L, "pdf1", "content".getBytes(), "ct", 2048L);

    @Test
    void test() {
        assertThat(pa.getId()).isEqualTo(1);
        assertThat(pa.getPaperId()).isEqualTo(2L);
        assertThat(pa.getName()).isEqualTo("pdf1");
        assertThat(pa.getContent()).isEqualTo("content".getBytes());
        assertThat(pa.getContentType()).isEqualTo("ct");
        assertThat(pa.getSize()).isEqualTo(2048L);
        assertThat(pa.getSizeKiloBytes()).isEqualTo(2L);
    }

    @Test
    void sizeInKiloBytes_withNullSize_isNull() {
        assertThat(new PaperAttachment().getSizeKiloBytes()).isNull();
    }

    @Test
    void sizeInKiloBytes_isRoundedUp() {
        pa.setSize(2047L);
        assertThat(pa.getSizeKiloBytes()).isEqualTo(2L);
        pa.setSize(2050L);
        assertThat(pa.getSizeKiloBytes()).isEqualTo(3L);
    }

    @Test
    void displayValue_isName() {
        assertThat(pa.getDisplayValue()).isEqualTo(pa.getName());
    }

    @Test
    void toString_isMinimal() {
        assertThat(pa.toString()).isEqualTo("PaperAttachment[paperId=2,name=pdf1,id=1]");
    }

    @Test
    void equals() {
        EqualsVerifier
            .forClass(PaperAttachment.class)
            .withRedefinedSuperclass()
            .usingGetClass()
            .withIgnoredFields(CONTENT.getFieldName(), CREATED.getFieldName(), CREATOR_ID.getFieldName(), MODIFIED.getFieldName(),
                MODIFIER_ID.getFieldName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }
}
