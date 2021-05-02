package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.PublicNewStudyTopic
import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter
import ch.difty.scipamato.publ.db.tables.NewStudyTopic
import org.jooq.DSLContext

/**
 * [ScipamatoItemWriter] implementation to synchronize newStudyTopics from scipamato-core to public.
 *
 *  * Takes care of inserts and updates.
 *  * Every record will be updated at least with the current timestamp in last_synched.
 *  * id columns and audit fields are those of the scipamato-core tables
 */
class NewStudyTopicItemWriter(jooqDslContextPublic: DSLContext) : ScipamatoItemWriter<PublicNewStudyTopic>(jooqDslContextPublic, "newStudyTopic") {
    override fun executeUpdate(i: PublicNewStudyTopic): Int =
        dslContext
            .insertInto(NewStudyTopic.NEW_STUDY_TOPIC)
            .columns(NewStudyTopic.NEW_STUDY_TOPIC.NEWSLETTER_ID, NewStudyTopic.NEW_STUDY_TOPIC.NEWSLETTER_TOPIC_ID,
                NewStudyTopic.NEW_STUDY_TOPIC.SORT, NewStudyTopic.NEW_STUDY_TOPIC.VERSION, NewStudyTopic.NEW_STUDY_TOPIC.CREATED,
                NewStudyTopic.NEW_STUDY_TOPIC.LAST_MODIFIED, NewStudyTopic.NEW_STUDY_TOPIC.LAST_SYNCHED)
            .values(i.newsletterId, i.newsletterTopicId, i.sort, i.version, i.created, i.lastModified, i.lastSynched)
            .onConflict(NewStudyTopic.NEW_STUDY_TOPIC.NEWSLETTER_ID, NewStudyTopic.NEW_STUDY_TOPIC.NEWSLETTER_TOPIC_ID)
            .doUpdate()
            .set(NewStudyTopic.NEW_STUDY_TOPIC.SORT, i.sort)
            .set(NewStudyTopic.NEW_STUDY_TOPIC.VERSION, i.version)
            .set(NewStudyTopic.NEW_STUDY_TOPIC.CREATED, i.created)
            .set(NewStudyTopic.NEW_STUDY_TOPIC.LAST_MODIFIED, i.lastModified)
            .set(NewStudyTopic.NEW_STUDY_TOPIC.LAST_SYNCHED, i.lastSynched)
            .where(
                NewStudyTopic.NEW_STUDY_TOPIC.NEWSLETTER_ID.eq(i.newsletterId)
                    .and(NewStudyTopic.NEW_STUDY_TOPIC.NEWSLETTER_TOPIC_ID.eq(i.newsletterTopicId))
            )
            .execute()
}
