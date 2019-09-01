package ch.difty.scipamato.publ.entity;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class NewsletterTest extends PublicEntityTest<Newsletter> {

    @Override
    protected Newsletter newEntity() {
        return new Newsletter(1, "2018/04", LocalDate.of(2018, 4, 10));
    }

    @Override
    protected void assertSpecificGetters() {
        assertThat(getEntity().getId()).isEqualTo(1);
        assertThat(getEntity().getIssue()).isEqualTo("2018/04");
        assertThat(getEntity()
            .getIssueDate()
            .toString()).isEqualTo("2018-04-10");

        assertThat(getEntity().getMonthName("de")).isEqualTo("April 2018");
        assertThat(getEntity().getMonthName("en")).isEqualTo("April 2018");
        assertThat(getEntity().getMonthName("fr")).isEqualTo("avril 2018");
    }

    @Override
    protected String getToString() {
        return "Newsletter(id=1, issue=2018/04, issueDate=2018-04-10)";
    }

    @Override
    protected void verifyEquals() {
        EqualsVerifier
            .forClass(NewStudy.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.getFieldName(), MODIFIED.getFieldName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    void assertEnumFields() {
        assertThat(Newsletter.NewsletterFields.values())
            .extracting("name")
            .containsExactly("id", "issue", "issueDate", "monthName");
    }
}