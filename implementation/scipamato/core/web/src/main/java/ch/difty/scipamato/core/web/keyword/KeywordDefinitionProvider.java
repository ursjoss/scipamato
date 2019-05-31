package ch.difty.scipamato.core.web.keyword;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.core.entity.keyword.Keyword;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;
import ch.difty.scipamato.core.persistence.KeywordService;
import ch.difty.scipamato.core.web.DefinitionProvider;

public class KeywordDefinitionProvider extends DefinitionProvider<KeywordDefinition, KeywordFilter, KeywordService> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private KeywordService service;

    KeywordDefinitionProvider() {
        this(null);
    }

    KeywordDefinitionProvider(KeywordFilter filter) {
        super(filter);
        setSort(Keyword.KeywordFields.NAME.getName(), SortOrder.ASCENDING);
    }

    @Override
    protected KeywordService getService() {
        return service;
    }

    @Override
    protected KeywordFilter newFilter() {
        return new KeywordFilter();
    }

}