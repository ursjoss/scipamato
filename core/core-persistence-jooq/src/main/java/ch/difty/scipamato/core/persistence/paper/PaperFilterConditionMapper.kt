package ch.difty.scipamato.core.persistence.paper

import ch.difty.scipamato.common.persistence.AbstractFilterConditionMapper
import ch.difty.scipamato.common.persistence.FilterConditionMapper
import ch.difty.scipamato.core.db.tables.Paper
import ch.difty.scipamato.core.db.tables.PaperNewsletter
import ch.difty.scipamato.core.entity.search.PaperFilter
import org.jooq.Condition
import org.jooq.impl.DSL

/**
 * Mapper turning the provider [PaperFilter] into a jOOQ [Condition].
 */
@FilterConditionMapper
class PaperFilterConditionMapper : AbstractFilterConditionMapper<PaperFilter>() {

    override fun internalMap(filter: PaperFilter): List<Condition> {
        val conditions = mutableListOf<Condition>()
        filter.number?.let { conditions.add(Paper.PAPER.NUMBER.eq(it)) }
        filter.authorMask?.let {
            val likeExpression = "%$it%"
            conditions.add(
                Paper.PAPER.FIRST_AUTHOR.likeIgnoreCase(likeExpression)
                    .or(Paper.PAPER.AUTHORS.likeIgnoreCase(likeExpression))
            )
        }
        filter.methodsMask?.let {
            val likeExpression = "%$it%"
            conditions.add(
                Paper.PAPER.EXPOSURE_POLLUTANT.likeIgnoreCase(likeExpression)
                    .or(Paper.PAPER.EXPOSURE_ASSESSMENT.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.METHODS.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.METHOD_STUDY_DESIGN.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.METHOD_OUTCOME.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.METHOD_STATISTICS.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.METHOD_CONFOUNDERS.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.POPULATION_PLACE.likeIgnoreCase(likeExpression))
            )
        }
        filter.populationMask?.let {
            val likeExpression = "%$it%"
            conditions.add(
                Paper.PAPER.POPULATION.likeIgnoreCase(likeExpression)
                    .or(Paper.PAPER.POPULATION_PLACE.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.POPULATION_PARTICIPANTS.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.POPULATION_DURATION.likeIgnoreCase(likeExpression))
            )
        }
        filter.searchMask?.let {
            val likeExpression = "%$it%"
            conditions.add(
                Paper.PAPER.DOI.likeIgnoreCase(likeExpression)
                    .or(Paper.PAPER.LOCATION.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.TITLE.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.GOALS.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.RESULT.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.RESULT_EXPOSURE_RANGE.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.RESULT_EFFECT_ESTIMATE.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.RESULT_MEASURED_OUTCOME.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.CONCLUSION.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.COMMENT.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.INTERN.likeIgnoreCase(likeExpression))
                    .or(Paper.PAPER.ORIGINAL_ABSTRACT.likeIgnoreCase(likeExpression))
            )
        }
        filter.publicationYearFrom?.let { conditions.add(Paper.PAPER.PUBLICATION_YEAR.ge(it)) }
        filter.publicationYearUntil?.let { conditions.add(Paper.PAPER.PUBLICATION_YEAR.le(it)) }
        filter.newsletterId?.let {
            conditions.add(
                DSL.exists(
                    DSL.selectOne().from(PaperNewsletter.PAPER_NEWSLETTER).where(
                        PaperNewsletter.PAPER_NEWSLETTER.PAPER_ID.eq(Paper.PAPER.ID)
                            .and(PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_ID.eq(it))
                    )
                )
            )
        }
        return conditions
    }
}
