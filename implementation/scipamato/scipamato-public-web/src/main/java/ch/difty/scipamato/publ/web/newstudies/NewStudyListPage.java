package ch.difty.scipamato.publ.web.newstudies;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.publ.entity.NewStudy;
import ch.difty.scipamato.publ.entity.NewStudyTopic;
import ch.difty.scipamato.publ.persistence.api.NewStudyTopicService;
import ch.difty.scipamato.publ.web.PublicPageParameters;
import ch.difty.scipamato.publ.web.common.BasePage;
import ch.difty.scipamato.publ.web.paper.browse.PublicPaperDetailPage;

/**
 * The page lists 'new studies', i.e. studies that were collected and flagged by
 * the SciPaMaTo-team as eligible for this page. By default, the newest
 * collection of new studies is presented.
 *
 * With the use of page-parameters, an older collection of new studies can be
 * selected instead (TODO).
 *
 * The page is typically shown in an iframe of a CMS.
 *
 * @author Urs Joss
 */
@MountPath("new-studies")
public class NewStudyListPage extends BasePage<Void> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private NewStudyTopicService newStudyTopicService;

    public NewStudyListPage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queue(newLabel("h1Title"));

        queue(newLabel("introParagraph"));
        queue(newLink("dbLink", getProperties().getCmsUrlSearchPage()));

        queue(newNewStudyCollection("topics"));
    }

    private Label newLabel(final String id) {
        return new Label(id, new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null).getString());
    }

    private ExternalLink newLink(final String id, final String href) {
        return new ExternalLink(id, href, new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null).getString());
    }

    private ListView<NewStudyTopic> newNewStudyCollection(final String id) {
        return new ListView<NewStudyTopic>(id, newStudyTopicService.findMostRecentNewStudyTopics()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<NewStudyTopic> topic) {
                topic.add(new Label("topicTitle", new PropertyModel<String>(topic.getModel(),
                        NewStudyTopic.NewStudyTopicFields.TITLE.getName())));
                topic.add(new ListView<NewStudy>("topicStudies", topic.getModelObject()
                    .getStudies()) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void populateItem(ListItem<NewStudy> study) {
                        study.add(new Label("headline", new PropertyModel<String>(study.getModel(),
                                NewStudy.NewStudyFields.HEADLINE.getName())));
                        study.add(new Label("description", new PropertyModel<String>(study.getModel(),
                                NewStudy.NewStudyFields.DESCRIPTION.getName())));
                        study.add(newLinkToStudy("reference", study));
                    }
                });
            }
        };
    }

    /**
     * Link pointing to the study detail page with the current study
     *
     * @param id
     *            the id on the html page. Also expects a label with the same id +
     *            tag 'Label'
     * @param study
     *            the current study as ListItem
     * @return the link
     */
    private Link<NewStudy> newLinkToStudy(final String id, final ListItem<NewStudy> study) {
        PageParameters pp = new PageParameters();
        pp.set(PublicPageParameters.NUMBER.getName(), study.getModelObject()
            .getNumber());
        Link<NewStudy> link = new Link<NewStudy>(id) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(new PublicPaperDetailPage(pp, getPageReference()));
            }
        };
        link.add(new Label(id + "Label",
                new PropertyModel<String>(study.getModel(), NewStudy.NewStudyFields.REFERENCE.getName())));
        return link;
    }

}
