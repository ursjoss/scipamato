package ch.difty.scipamato.publ.persistence.paper

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.publ.entity.PublicPaper
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter
import ch.difty.scipamato.publ.persistence.api.PublicPaperService
import org.springframework.stereotype.Service

@Service
class JooqPublicPaperService(private val repository: PublicPaperRepository) : PublicPaperService {

    override fun findByNumber(number: Long): PublicPaper? = repository.findByNumber(number)

    override fun findPageByFilter(
        filter: PublicPaperFilter,
        paginationContext: PaginationContext,
    ): List<PublicPaper> = repository.findPageByFilter(filter, paginationContext)

    override fun countByFilter(filter: PublicPaperFilter): Int = repository.countByFilter(filter)

    override fun findPageOfNumbersByFilter(
        filter: PublicPaperFilter,
        paginationContext: PaginationContext,
    ): List<Long> = repository.findPageOfNumbersByFilter(filter, paginationContext)
}
