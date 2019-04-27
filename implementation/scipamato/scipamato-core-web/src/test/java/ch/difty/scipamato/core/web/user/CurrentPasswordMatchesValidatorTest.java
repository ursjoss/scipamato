package ch.difty.scipamato.core.web.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.validation.Validatable;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ch.difty.scipamato.common.TestUtils;

class CurrentPasswordMatchesValidatorTest {

    private static final String PW_ADMIN           = "admin";
    private static final String PW_ADMIN_ENCRYPTED = "$2a$04$oOL75tgCf3kXdr6vO5gagu6sIUZWfXyEhZHmDd4LpGvOPTaO5xEoO";

    private final CurrentPasswordMatchesValidator v = new CurrentPasswordMatchesValidator(new BCryptPasswordEncoder(),
        PW_ADMIN_ENCRYPTED);

    private Validatable<String> validatable;

    @Test
    void degenerateConstruction_withNullPasswordEncoder_fails() {
        TestUtils.assertDegenerateSupplierParameter(() -> new CurrentPasswordMatchesValidator(null, "foo"),
            "passwordEncoder");
    }

    @Test
    void degenerateConstruction_withNullPassword_fails() {
        TestUtils.assertDegenerateSupplierParameter(
            () -> new CurrentPasswordMatchesValidator(new BCryptPasswordEncoder(), null),
            "currentPasswordHashPersisted");
    }

    @Test
    void withMatchingHash_succeeds() {
        validatable = new Validatable<>(PW_ADMIN);
        v.validate(validatable);
        assertThat(validatable.isValid()).isTrue();
        assertThat(validatable.getErrors()).isEmpty();
    }

    @Test
    void withNonMatchingHash_succeeds() {
        validatable = new Validatable<>(PW_ADMIN + "X");
        v.validate(validatable);
        assertThat(validatable.isValid()).isFalse();
        assertThat(validatable.getErrors()).hasSize(1);
    }
}