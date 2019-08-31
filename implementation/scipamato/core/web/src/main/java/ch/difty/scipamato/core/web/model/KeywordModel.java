package ch.difty.scipamato.core.web.model;

import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.web.model.InjectedLoadableDetachableModel;
import ch.difty.scipamato.core.entity.keyword.Keyword;
import ch.difty.scipamato.core.persistence.KeywordService;

/**
 * Model that offers a wicket page to load {@link Keyword}s.
 *
 * @author u.joss
 */
public class KeywordModel extends InjectedLoadableDetachableModel<Keyword> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private KeywordService service;

    private final String languageCode;

    public KeywordModel(final String languageCode) {
        this.languageCode = AssertAs.INSTANCE.notNull(languageCode, "languageCode");
    }

    public List<Keyword> load() {
        return service.findAll(languageCode);
    }

}
