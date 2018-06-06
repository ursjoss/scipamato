package ch.difty.scipamato.core.web.model;

import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.web.model.InjectedLoadableDetachableModel;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;

/**
 * Model that offers a wicket page to load {@link NewsletterTopic}s.
 *
 * @author u.joss
 */
public class NewsletterTopicModel extends InjectedLoadableDetachableModel<NewsletterTopic> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private NewsletterTopicService service;

    private final String languageCode;

    public NewsletterTopicModel(final String languageCode) {
        this.languageCode = AssertAs.notNull(languageCode, "languageCode");
    }

    /**
     * Protected constructor for testing without wicket application.
     */
    protected NewsletterTopicModel(final String languageCode, final NewsletterTopicService service) {
        this(languageCode);
        this.service = service;
    }

    public List<NewsletterTopic> load() {
        return service.findAll(languageCode);
    }

}
