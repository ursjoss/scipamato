package ch.difty.scipamato.core.persistence.user

import ch.difty.scipamato.common.persistence.AbstractFilterConditionMapper
import ch.difty.scipamato.common.persistence.FilterConditionMapper
import ch.difty.scipamato.core.db.tables.ScipamatoUser
import ch.difty.scipamato.core.entity.search.UserFilter
import org.jooq.Condition

/**
 * Mapper turning the provider [UserFilter] into a jOOQ [Condition].
 */
@FilterConditionMapper
class UserFilterConditionMapper : AbstractFilterConditionMapper<UserFilter>() {

    override fun internalMap(filter: UserFilter): List<Condition> {
        val conditions = mutableListOf<Condition>()
        filter.nameMask?.let {
            val likeExpression = "%$it%"
            conditions.add(
                ScipamatoUser.SCIPAMATO_USER.USER_NAME.likeIgnoreCase(likeExpression)
                    .or(ScipamatoUser.SCIPAMATO_USER.FIRST_NAME.likeIgnoreCase(likeExpression))
                    .or(ScipamatoUser.SCIPAMATO_USER.LAST_NAME.likeIgnoreCase(likeExpression))
            )
        }
        filter.emailMask?.let {
            conditions.add(ScipamatoUser.SCIPAMATO_USER.EMAIL.likeIgnoreCase("%$it%"))
        }
        filter.enabled?.let {
            conditions.add(ScipamatoUser.SCIPAMATO_USER.ENABLED.eq(it))
        }
        return conditions
    }
}
