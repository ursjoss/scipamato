package ch.difty.scipamato.publ.entity;

import static ch.difty.scipamato.publ.entity.StudyDesignCode.EPIODEMIOLOGICAL;
import static ch.difty.scipamato.publ.entity.StudyDesignCode.EXPERIMENTAL;
import static ch.difty.scipamato.publ.entity.StudyDesignCode.OVERVIEW_METHODOLOGY;
import static ch.difty.scipamato.publ.entity.StudyDesignCode.values;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import ch.difty.scipamato.publ.entity.StudyDesignCode;

public class StudyDesignCodeTest {

    @Test
    public void hasAllvalues() {
        assertThat(values()).containsExactly(EXPERIMENTAL, EPIODEMIOLOGICAL, OVERVIEW_METHODOLOGY);
    }

    @Test
    public void assertIds() {
        assertThat(extractProperty("id").from(values())).containsExactly((short) 1, (short) 2, (short) 3);
    }

    @Test
    public void of_withExistingId() {
        assertThat(StudyDesignCode.of((short) 1)).isEqualTo(EXPERIMENTAL);
    }

    @Test
    public void of_withNotExistingId_throws() {
        try {
            StudyDesignCode.of((short) 0);
            fail("Should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No matching type for id 0");
        }
    }
}
