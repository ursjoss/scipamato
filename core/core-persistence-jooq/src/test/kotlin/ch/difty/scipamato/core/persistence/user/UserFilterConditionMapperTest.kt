package ch.difty.scipamato.core.persistence.user

import ch.difty.scipamato.common.persistence.FilterConditionMapperTest
import ch.difty.scipamato.core.db.tables.ScipamatoUser.SCIPAMATO_USER
import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord
import ch.difty.scipamato.core.entity.search.UserFilter
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class UserFilterConditionMapperTest :
    FilterConditionMapperTest<ScipamatoUserRecord, ch.difty.scipamato.core.db.tables.ScipamatoUser, UserFilter>() {

    override val mapper = UserFilterConditionMapper()
    override val filter = UserFilter()
    override val table = SCIPAMATO_USER!!

    @Test
    fun creatingWhereCondition_withNameMask_searchesUserNameAndFirstNameAndLastName() {
        val pattern = "am"
        filter.nameMask = pattern
        mapper.map(filter).toString() shouldBeEqualTo
            makeWhereClause(pattern, "user_name", "first_name", "last_name")
    }

    @Test
    fun creatingWhereCondition_withEmailMask_searchesEmail() {
        val pattern = "m"
        filter.emailMask = pattern
        mapper.map(filter).toString() shouldBeEqualTo
            """"public"."scipamato_user"."email" ilike '%m%'"""
    }

    @Test
    fun creatingWhereCondition_withEnabledMask_searchesEnabled() {
        filter.enabled = true
        mapper.map(filter).toString() shouldBeEqualTo
            """"public"."scipamato_user"."enabled" = true"""
    }
}
