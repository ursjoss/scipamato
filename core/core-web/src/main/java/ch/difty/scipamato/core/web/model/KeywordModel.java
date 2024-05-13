package ch.difty.scipamato.core.web.model;

import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.web.model.InjectedLoadableDetachableModel;
import ch.difty.scipamato.core.entity.keyword.Keyword;
import ch.difty.scipamato.core.persistence.KeywordService;

/**
 * Model that offers a wicket page to load {@link Keyword}s.
 *
 * @author u.joss
 */
public class KeywordModel extends InjectedLoadableDetachableModel<Keyword> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @SpringBean
    private KeywordService service;

    private final String languageCode;

    public KeywordModel(@NotNull final String languageCode) {
        this.languageCode = languageCode;
    }

    @NotNull
    public List<Keyword> load() {
        return service.findAll(languageCode);
    }
}
