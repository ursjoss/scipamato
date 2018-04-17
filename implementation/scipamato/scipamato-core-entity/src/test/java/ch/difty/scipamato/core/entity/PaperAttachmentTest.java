package ch.difty.scipamato.core.entity;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID;
import static ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID;
import static ch.difty.scipamato.core.entity.PaperAttachment.PaperAttachmentFields.CONTENT;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class PaperAttachmentTest {

    private final PaperAttachment pa = new PaperAttachment(1, 2L, "pdf1", "content".getBytes(), "ct", 2048L);

    @Test
    public void test() {
        assertThat(pa.getId()).isEqualTo(1);
        assertThat(pa.getPaperId()).isEqualTo(2L);
        assertThat(pa.getName()).isEqualTo("pdf1");
        assertThat(pa.getContent()).isEqualTo("content".getBytes());
        assertThat(pa.getContentType()).isEqualTo("ct");
        assertThat(pa.getSize()).isEqualTo(2048L);
        assertThat(pa.getSizeKiloBytes()).isEqualTo(2L);
    }

    @Test
    public void sizeInKiloBytes_withNullSize_isNull() {
        assertThat(new PaperAttachment().getSizeKiloBytes()).isNull();
    }

    @Test
    public void sizeInKiloBytes_isRoundedUp() {
        pa.setSize(2047L);
        assertThat(pa.getSizeKiloBytes()).isEqualTo(2L);
        pa.setSize(2050L);
        assertThat(pa.getSizeKiloBytes()).isEqualTo(3L);
    }

    @Test
    public void displayValue_isName() {
        assertThat(pa.getDisplayValue()).isEqualTo(pa.getName());
    }

    @Test
    public void toString_isMinimal() {
        assertThat(pa.toString()).isEqualTo("PaperAttachment[paperId=2,name=pdf1,id=1]");
    }

    @Test
    public void equals() {
        EqualsVerifier.forClass(PaperAttachment.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CONTENT.getName(), CREATED.getName(), CREATOR_ID.getName(), MODIFIED.getName(),
                MODIFIER_ID.getName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }
}
