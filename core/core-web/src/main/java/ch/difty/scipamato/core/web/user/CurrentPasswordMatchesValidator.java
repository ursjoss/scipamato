package ch.difty.scipamato.core.web.user;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Validator to verify if the text field contains a password that matches
 * the password hash passed into the constructor.
 */
class CurrentPasswordMatchesValidator implements IValidator<String> {

    private static final long serialVersionUID = 1L;

    private final PasswordEncoder passwordEncoder;
    private final String          currentPasswordHashPersisted;

    public CurrentPasswordMatchesValidator(@NotNull PasswordEncoder passwordEncoder,
        @NotNull final String currentPasswordHashPersisted) {
        this.passwordEncoder = passwordEncoder;
        this.currentPasswordHashPersisted = currentPasswordHashPersisted;
    }

    @Override
    public void validate(@NotNull final IValidatable<String> validatable) {
        final String pwCandidate = validatable.getValue();
        if (!passwordEncoder.matches(pwCandidate, currentPasswordHashPersisted)) {
            ValidationError error = new ValidationError();
            error.addKey(getClass().getSimpleName() + "." + "not-matching");
            validatable.error(error);
        }
    }
}
