package ch.difty.scipamato.publ.entity;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class NewStudyTopicTest extends PublicEntityTest<NewStudyTopic> {

    @Override
    protected NewStudyTopic newEntity() {
        List<NewStudy> studies = new ArrayList<>();
        return new NewStudyTopic(1, "title", studies);
    }

    @Override
    protected void assertSpecificGetters() {
        assertThat(getEntity().getSort()).isEqualTo(1);
        assertThat(getEntity().getTitle()).isEqualTo("title");
        assertThat(getEntity().getStudies()).isEmpty();
    }

    @Override
    protected String getToString() {
        return "NewStudyTopic(sort=1, title=title, studies=[])";
    }

    @Override
    protected void verifyEquals() {
        EqualsVerifier
            .forClass(NewStudyTopic.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.getFieldName(), MODIFIED.getFieldName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    void assertEnumFields() {
        assertThat(NewStudyTopic.NewStudyTopicFields.values())
            .extracting("name")
            .containsExactly("sort", "title", "studies");
    }

    @Test
    void secondaryConstructor_hasNoStudies() {
        final NewStudyTopic t = new NewStudyTopic(2, "title2");
        assertThat(t.getSort()).isEqualTo(2);
        assertThat(t.getTitle()).isEqualTo("title2");
        assertThat(t.getStudies()).isEmpty();

    }

}
