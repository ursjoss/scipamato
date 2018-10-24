package ch.difty.scipamato.publ.web.model;

import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.web.model.InjectedLoadableDetachableModel;
import ch.difty.scipamato.publ.entity.Keyword;
import ch.difty.scipamato.publ.persistence.api.KeywordService;

public class KeywordModel extends InjectedLoadableDetachableModel<Keyword> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private KeywordService service;

    private final String languageCode;

    public KeywordModel(final String languageCode) {
        super();
        this.languageCode = AssertAs.notNull(languageCode, "languageCode");
    }

    @Override
    protected List<Keyword> load() {
        return service.findKeywords(languageCode);
    }
}