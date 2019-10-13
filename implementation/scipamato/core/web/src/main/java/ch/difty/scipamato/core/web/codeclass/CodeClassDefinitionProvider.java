package ch.difty.scipamato.core.web.codeclass;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.core.entity.codeclass.CodeClassDefinition;
import ch.difty.scipamato.core.entity.codeclass.CodeClassFilter;
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

    CodeClassDefinitionProvider(@Nullable final CodeClassFilter filter) {
        super(filter);
        setSort(CodeClassDefinition.CodeClassDefinitionFields.ID.getFieldName(), SortOrder.ASCENDING);
    }

    @NotNull
    @Override
    protected CodeClassService getService() {
        return service;
    }

    @NotNull
    @Override
    protected CodeClassFilter newFilter() {
        return new CodeClassFilter();
    }
}
