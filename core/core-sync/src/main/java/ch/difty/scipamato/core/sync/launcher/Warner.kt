package ch.difty.scipamato.core.sync.launcher

interface Warner {

    /**
     * Returns a message indicating papers that cannot be synchronized, because they have no codes assigned.
     * Returns `null` if all papers were synchronized
     */
    fun findUnsynchronizedPapers(): String?
}
