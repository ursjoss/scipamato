package ch.difty.scipamato.publ.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.entity.FieldEnumType;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Newsletter extends PublicEntity {

    private static final long serialVersionUID = 1L;

    private final int       id;
    private final String    issue;
    private final LocalDate issueDate;

    public String getMonthName(final String langCode) {
        AssertAs.INSTANCE.notNull(langCode, "langCode");
        return issueDate.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.forLanguageTag(langCode)));
    }

    public enum NewsletterFields implements FieldEnumType {
        ID("id"),
        ISSUE("issue"),
        ISSUE_DATE("issueDate"),
        MONTH_NAME("monthName");

        private final String name;

        NewsletterFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

}
