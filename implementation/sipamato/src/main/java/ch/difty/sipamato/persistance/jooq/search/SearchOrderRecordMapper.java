package ch.difty.sipamato.persistance.jooq.search;

import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.db.tables.records.SearchOrderRecord;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.lib.AssertAs;

@Component
public class SearchOrderRecordMapper implements RecordMapper<SearchOrderRecord, SearchOrder> {

    /** {@inheritDoc} */
    @Override
    public SearchOrder map(SearchOrderRecord from) {
        AssertAs.notNull(from, "from");
        SearchOrder to = new SearchOrder();
        to.setId(from.getId());
        to.setOwner(from.getOwner());
        to.setGlobal(from.getGlobal());

        return to;
    }

}
