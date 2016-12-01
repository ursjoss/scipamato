package ch.difty.sipamato.persistance.jooq.search;

import ch.difty.sipamato.db.tables.records.SearchOrderRecord;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.persistance.jooq.EntityRepository;

public interface SearchOrderRepository extends EntityRepository<SearchOrderRecord, SearchOrder, Integer, SearchOrderRecordMapper, SearchOrderFilter> {

}
