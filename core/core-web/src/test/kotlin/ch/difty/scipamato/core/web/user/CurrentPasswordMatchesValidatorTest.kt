package ch.difty.scipamato.core.web.user

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldHaveSize
import org.apache.wicket.validation.Validatable
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

internal class CurrentPasswordMatchesValidatorTest {

    private val v = CurrentPasswordMatchesValidator(BCryptPasswordEncoder(), PW_ADMIN_ENCRYPTED)

    private lateinit var validatable: Validatable<String>

    @Test
    fun withMatchingHash_succeeds() {
        validatable = Validatable(PW_ADMIN)
        v.validate(validatable)
        validatable.isValid.shouldBeTrue()
        validatable.errors.shouldBeEmpty()
    }

    @Test
    fun withNonMatchingHash_succeeds() {
        validatable = Validatable(PW_ADMIN + "X")
        v.validate(validatable)
        validatable.isValid.shouldBeFalse()
        validatable.errors shouldHaveSize 1
    }

    companion object {
        private const val PW_ADMIN = "admin"
        private const val PW_ADMIN_ENCRYPTED = "$2a$04\$oOL75tgCf3kXdr6vO5gagu6sIUZWfXyEhZHmDd4LpGvOPTaO5xEoO"
    }
}
