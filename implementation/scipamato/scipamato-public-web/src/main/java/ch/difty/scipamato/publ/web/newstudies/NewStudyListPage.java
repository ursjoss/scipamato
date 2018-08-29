package ch.difty.scipamato.publ.web.newstudies;

import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.publ.entity.NewStudy;
import ch.difty.scipamato.publ.entity.NewStudyTopic;
import ch.difty.scipamato.publ.entity.Newsletter;
import ch.difty.scipamato.publ.persistence.api.NewStudyTopicService;
import ch.difty.scipamato.publ.web.CommercialFontResourceProvider;
import ch.difty.scipamato.publ.web.PublicPageParameters;
import ch.difty.scipamato.publ.web.common.BasePage;
import ch.difty.scipamato.publ.web.paper.browse.PublicPaperDetailPage;

/**
 * The page lists 'new studies', i.e. studies that were collected and flagged by
 * the SciPaMaTo-team as eligible for this page. By default, the newest
 * collection of new studies is presented.
 * <p>
 * With the use of page-parameters, an older collection of new studies can be
 * selected instead.
 * <p>
 * The page is typically shown in an iframe of a CMS.
 *
 * @author Urs Joss
 */
@MountPath("new-studies")
public class NewStudyListPage extends BasePage<Void> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private NewStudyTopicService newStudyTopicService;

    @SpringBean(name = "simplonFontResourceProvider")
    private CommercialFontResourceProvider simplonFontResourceProvider;

    public NewStudyListPage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void renderAdditionalCommercialFonts(final IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(simplonFontResourceProvider.getCssResourceReference()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queue(newLabel("h1Title"));

        queue(newLabel("introParagraph"));
        queue(newLink("dbLink", getProperties().getCmsUrlSearchPage()));

        queue(newNewStudyCollection("topics"));

        queue(newLabel("h2ArchiveTitle"));

        queue(newNewsletterArchive("archive"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(
            CssHeaderItem.forReference(new CssResourceReference(NewStudyListPage.class, "NewStudyListPage.css")));
    }

    private Label newLabel(final String id) {
        return new Label(id, new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null).getString());
    }

    private ExternalLink newLink(final String id, final String href) {
        return new ExternalLink(id, href, new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null).getString()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("target", "_blank");
            }
        };
    }

    private ListView<NewStudyTopic> newNewStudyCollection(final String id) {
        final List<NewStudyTopic> topics = retrieveStudyCollection();
        return new ListView<NewStudyTopic>(id, topics) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<NewStudyTopic> topic) {
                topic.add(new Label("topicTitle",
                    new PropertyModel<String>(topic.getModel(), NewStudyTopic.NewStudyTopicFields.TITLE.getName())));
                topic.add(new ListView<NewStudy>("topicStudies", topic
                    .getModelObject()
                    .getStudies()) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void populateItem(ListItem<NewStudy> study) {
                        study.add(new Label("headline",
                            new PropertyModel<String>(study.getModel(), NewStudy.NewStudyFields.HEADLINE.getName())));
                        study.add(new Label("description", new PropertyModel<String>(study.getModel(),
                            NewStudy.NewStudyFields.DESCRIPTION.getName())));
                        study.add(newLinkToStudy("reference", study));
                    }
                });
            }
        };
    }

    private List<NewStudyTopic> retrieveStudyCollection() {
        final StringValue issue = getPageParameters().get(PublicPageParameters.ISSUE.getName());
        if (issue == null || issue.isNull() || issue.isEmpty())
            return newStudyTopicService.findMostRecentNewStudyTopics(getLanguageCode());
        else
            return newStudyTopicService.findNewStudyTopicsForNewsletterIssue(issue.toString(), getLanguageCode());
    }

    /**
     * Link pointing to the study detail page with the current study
     *
     * @param id
     *     the id on the html page. Also expects a label with the same id +
     *     tag 'Label'
     * @param study
     *     the current study as ListItem
     * @return the link
     */
    private Link<NewStudy> newLinkToStudy(final String id, final ListItem<NewStudy> study) {
        PageParameters pp = new PageParameters();
        pp.set(PublicPageParameters.NUMBER.getName(), study
            .getModelObject()
            .getNumber());
        Link<NewStudy> link = new Link<NewStudy>(id) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(new PublicPaperDetailPage(pp, getPageReference()));
            }

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("target", "_blank");
            }
        };
        link.add(new Label(id + "Label",
            new PropertyModel<String>(study.getModel(), NewStudy.NewStudyFields.REFERENCE.getName())));
        return link;
    }

    private ListView<Newsletter> newNewsletterArchive(final String id) {
        final List<Newsletter> newsletters = newStudyTopicService.findArchivedNewsletters(getLanguageCode());
        return new ListView<Newsletter>(id, newsletters) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Newsletter> nl) {
                nl.add(newLinkToNewsletter("monthName", nl));
            }
        };
    }

    private Link<Newsletter> newLinkToNewsletter(final String id, final ListItem<Newsletter> newsletter) {
        final PageParameters pp = new PageParameters();
        pp.set(PublicPageParameters.ISSUE.getName(), newsletter
            .getModelObject()
            .getIssue());
        final Link<Newsletter> link = new Link<>(id) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(new NewStudyListPage(pp));
            }

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("target", "_blank");
            }
        };
        link.add(new Label(id + "Label", newsletter
            .getModelObject()
            .getMonthName(getLanguageCode())));
        return link;
    }
}
