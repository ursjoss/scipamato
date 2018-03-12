package ch.difty.scipamato.publ.entity;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class NewStudyTest extends PublicEntityTest<NewStudy> {

    @Override
    protected NewStudy newEntity() {
        return new NewStudy(1, 10, "ref", "hl", "descr");
    }

    @Override
    protected void assertSpecificGetters() {
        assertThat(getEntity().getSort()).isEqualTo(1);
        assertThat(getEntity().getNumber()).isEqualTo(10);
        assertThat(getEntity().getReference()).isEqualTo("ref");
        assertThat(getEntity().getHeadline()).isEqualTo("hl");
        assertThat(getEntity().getDescription()).isEqualTo("descr");
    }

    @Override
    protected String getToString() {
        return "NewStudy(sort=1, number=10, reference=ref, headline=hl, description=descr)";
    }

    @Override
    protected void verifyEquals() {
        EqualsVerifier.forClass(NewStudy.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.getName(), MODIFIED.getName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    public void assertEnumFields() {
        assertThat(NewStudy.NewStudyFields.values()).extracting("name")
            .containsExactly("sort", "number", "reference", "headline", "description");
    }

}
