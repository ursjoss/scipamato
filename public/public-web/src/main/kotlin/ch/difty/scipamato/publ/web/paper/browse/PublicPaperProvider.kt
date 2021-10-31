package ch.difty.scipamato.publ.web.paper.browse

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.common.persistence.paging.PaginationRequest
import ch.difty.scipamato.common.persistence.paging.Sort
import ch.difty.scipamato.publ.entity.PublicPaper
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter
import ch.difty.scipamato.publ.persistence.api.PublicPaperService
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider
import org.apache.wicket.injection.Injector
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.spring.injection.annot.SpringBean

/**
 * The data provider for [PublicPaper] entities providing the wicket
 * components access to the persisted data
 */
internal class PublicPaperProvider(
    paperFilter: PublicPaperFilter?,
    resultPageSize: Int,
) : SortableDataProvider<PublicPaper, String>(), ISortableDataProvider<PublicPaper, String>, IFilterStateLocator<PublicPaperFilter> {

    @SpringBean
    private lateinit var service: PublicPaperService

    private var paperFilter: PublicPaperFilter

    /**
     * Return the (max) rowsPerPage (or pageSize), regardless of the number of records actually available on the page.
     */
    val rowsPerPage: Int

    init {
        Injector.get().inject(this)
        this.paperFilter = paperFilter ?: PublicPaperFilter()
        rowsPerPage = resultPageSize
        setSort("number", SortOrder.DESCENDING)
    }


    /** for test purposes  */
    fun setService(service: PublicPaperService) {
        this.service = service
    }

    override fun iterator(offset: Long, count: Long): Iterator<PublicPaper> {
        val dir = if (sort.isAscending) Sort.Direction.ASC else Sort.Direction.DESC
        val sortProp = sort.property!!
        val pc: PaginationContext = PaginationRequest(offset.toInt(), count.toInt(), dir, sortProp)
        return service.findPageByFilter(paperFilter, pc).iterator()
    }

    override fun size(): Long = service.countByFilter(paperFilter).toLong()

    override fun model(entity: PublicPaper?): IModel<PublicPaper> = Model(entity)

    override fun getFilterState(): PublicPaperFilter = paperFilter

    override fun setFilterState(filterState: PublicPaperFilter) {
        paperFilter = filterState
    }

    /**
     * Applies the normal filter and the sort aspect of the pageable to return only
     * the numbers (business key) of all papers (un-paged).
     */
    fun findAllPaperNumbersByFilter(): List<Long> {
        val dir = if (sort.isAscending) Sort.Direction.ASC else Sort.Direction.DESC
        val sortProp = sort.property!!
        return service.findPageOfNumbersByFilter(filterState, PaginationRequest(dir, sortProp))
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
