package ch.difty.scipamato.common.entity;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

public class ScipamatoEntityTest {

    private static final int VERSION = 10;

    private static final LocalDateTime CD = LocalDateTime
        .now()
        .minusDays(1);
    private static final LocalDateTime LM = LocalDateTime
        .now()
        .plusDays(1);

    private final ScipamatoEntity e = new ScipamatoEntity();

    @Before
    public void setUp() {
        e.setCreated(CD);
        e.setLastModified(LM);
        e.setVersion(VERSION);
    }

    @Test
    public void get() {
        assertThat(e.getCreated()).isEqualTo(CD);
        assertThat(e.getLastModified()).isEqualTo(LM);
        assertThat(e.getVersion()).isEqualTo(VERSION);
    }

    @Test
    public void testingToString() {
        assertThat(e.toString()).isEqualTo(
            "ScipamatoEntity[created=" + CD + ",lastModified=" + LM + ",version=" + VERSION + "]");
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(ScipamatoEntity.class)
            .withIgnoredFields(CREATED.getName(), MODIFIED.getName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

}
