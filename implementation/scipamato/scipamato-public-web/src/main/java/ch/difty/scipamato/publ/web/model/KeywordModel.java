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

    /**
     * Protected constructor for testing without wicket application.
     *
     * @param languageCode
     *     the two character language code, e.g. 'en' or 'de'
     * @param service
     *     the service to retrieve the keywords
     */
    @SuppressWarnings("WeakerAccess")
    protected KeywordModel(final String languageCode, final KeywordService service) {
        this(languageCode);
        this.service = service;
    }

    @Override
    protected List<Keyword> load() {
        return service.findKeywords(languageCode);
    }
}