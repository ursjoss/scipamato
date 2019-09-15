package ch.difty.scipamato.core.web.codeclass;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.core.entity.code_class.CodeClassDefinition;
import ch.difty.scipamato.core.entity.code_class.CodeClassFilter;
import ch.difty.scipamato.core.persistence.CodeClassService;
import ch.difty.scipamato.core.web.DefinitionProvider;

public class CodeClassDefinitionProvider
    extends DefinitionProvider<CodeClassDefinition, CodeClassFilter, CodeClassService> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private CodeClassService service;

    CodeClassDefinitionProvider() {
        this(null);
    }

    CodeClassDefinitionProvider(final CodeClassFilter filter) {
        super(filter);
        setSort(CodeClassDefinition.CodeClassDefinitionFields.ID.getFieldName(), SortOrder.ASCENDING);
    }

    @Override
    protected CodeClassService getService() {
        return service;
    }

    @Override
    protected CodeClassFilter newFilter() {
        return new CodeClassFilter();
    }

}
