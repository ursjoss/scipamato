package ch.difty.scipamato.common

import io.mockk.clearAllMocks
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * Junit extension that resets all mocks after every test execution.
 * It should not be needed because MockkBean clears the mocks that have been created in the application context by
 * default. Unfortunately, that doesn't work for nested tests due to a Spring Boot issue
 * (see https://github.com/spring-projects/spring-boot/issues/12470). So, to make things easier, we keep using this
 * extension.
 * @author JB Nizet
 *
 * Taken from https://github.com/Ninja-Squad/springmockk/issues/12 - thanks to @jnizet
 */
class ClearAllMocksExtension : AfterTestExecutionCallback {
    override fun afterTestExecution(context: ExtensionContext?) {
        clearAllMocks()
    }
}
