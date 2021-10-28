package ch.difty.scipamato.core.sync.launcher

interface Warner {

    /**
     * Returns a message indicating papers that cannot be synchronized, because they have no codes assigned.
     * Returns `null` if all papers were synchronized
     */
    fun findUnsynchronizedPapers(): String?

    /**
     * Returns a message indicating newsletters with unsynchronized papers, because they have no codes assigned.
     * Returns `null` if all papers of all newsletters were synchronized
     */
    fun findNewsletterswithUnsynchronizedPapers(): String?
}
