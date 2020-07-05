package ch.difty.scipamato.core.web.user

internal class UserListPageAsUserTest : UserListPageAsViewerTest() {

    override val userName: String
        get() = "testuser"
}
