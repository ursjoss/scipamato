package ch.difty.scipamato.core.web.user

import ch.difty.scipamato.core.web.WicketTest
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.apache.wicket.authorization.UnauthorizedInstantiationException
import org.junit.jupiter.api.Test

internal open class UserListPageAsViewerTest : WicketTest() {

    override val userName: String
        get() = "testviewer"

    @Test
    fun viewerCannotAccessUserListPage() {
        invoking { tester.startPage(UserListPage(null)) } shouldThrow
            UnauthorizedInstantiationException::class withMessage
            "Not authorized to instantiate class ch.difty.scipamato.core.web.user.UserListPage"
    }
}