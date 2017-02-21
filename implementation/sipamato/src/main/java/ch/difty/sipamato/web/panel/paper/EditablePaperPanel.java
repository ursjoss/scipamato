package ch.difty.sipamato.web.panel.paper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.logic.parsing.AuthorParser;
import ch.difty.sipamato.logic.parsing.AuthorParserFactory;
import ch.difty.sipamato.pubmed.entity.PubmedArticleFacade;
import ch.difty.sipamato.service.PubmedService;
import ch.difty.sipamato.web.jasper.SipamatoPdfExporterConfiguration;
import ch.difty.sipamato.web.jasper.summary.PaperSummaryDataSource;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.pages.Mode;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;

/**
 * The {@link EditablePaperPanel} is backed by the {@link Paper} entity. The purpose is to create new papers
 * or edit existing papers.
 *
 * @author u.joss
 */
public abstract class EditablePaperPanel extends PaperPanel<Paper> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private AuthorParserFactory authorParserFactory;

    @SpringBean
    private PubmedService pubmedService;

    public EditablePaperPanel(String id, IModel<Paper> model) {
        super(id, model, Mode.EDIT);
    }

    /**
     * If the user does not choose to override the first author, the field can be disabled. 
     */
    @Override
    protected TextField<String> makeFirstAuthor(String firstAuthorId, CheckBox firstAuthorOverridden) {
        TextField<String> firstAuthor = new TextField<String>(firstAuthorId) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(firstAuthorOverridden.getModelObject());
            }
        };
        firstAuthor.add(new PropertyValidator<String>());
        return firstAuthor;
    }

    /**
     * Add the behavior to derive the first author from the authors field (unless overridden).
     */
    @Override
    protected void addAuthorBehavior(TextArea<String> authors, CheckBox firstAuthorOverridden, TextField<String> firstAuthor) {
        firstAuthorOverridden.add(makeFirstAuthorChangeBehavior(authors, firstAuthorOverridden, firstAuthor));
        authors.add(makeFirstAuthorChangeBehavior(authors, firstAuthorOverridden, firstAuthor));
    }

    /*
     * Behavior to parse the authors string and populate the firstAuthor field - but only if not overridden. 
     */
    private OnChangeAjaxBehavior makeFirstAuthorChangeBehavior(TextArea<String> authors, CheckBox overridden, TextField<String> firstAuthor) {
        return new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (!overridden.getModelObject()) {
                    AuthorParser p = authorParserFactory.createParser(authors.getValue());
                    firstAuthor.setModelObject(p.getFirstAuthor().orElse(null));
                }
                target.add(firstAuthor);
            }
        };
    }

    /**
     * Prepares the {@link PaperSummaryDatasource} for exporting the current entity into the pdf.
     */
    @Override
    protected PaperSummaryDataSource getSummaryDataSource() {
        final String populationLabel = new StringResourceModel("population" + LABEL_RECOURCE_TAG, this, null).getString();
        final String methodsLabel = new StringResourceModel("methods" + LABEL_RECOURCE_TAG, this, null).getString();
        final String resultLabel = new StringResourceModel("result" + LABEL_RECOURCE_TAG, this, null).getString();
        final String commentLabel = new StringResourceModel("comment" + LABEL_RECOURCE_TAG, this, null).getString();
        final String brand = getProperties().getBrand();
        final String headerPart = brand + "-" + new StringResourceModel("headerPart", this, null).getString();

        SipamatoPdfExporterConfiguration config = new SipamatoPdfExporterConfiguration.Builder(headerPart, getModelObject().getId()).withCreator(brand)
                .withPaperTitle(getModelObject().getTitle())
                .withPaperAuthor(getModelObject().getFirstAuthor())
                .withSubject(getModelObject().getMethods())
                .withAuthor(getModelObject().getCreatedByFullName())
                .withCodes(getModelObject().getCodes())
                .withCompression()
                .build();
        return new PaperSummaryDataSource(getModelObject(), populationLabel, methodsLabel, resultLabel, commentLabel, headerPart, brand, config);
    }

    /**
     * Enable the codeClass1 field to update the mainCodeOfCodeClass1 field automatically.
     */
    @Override
    protected void addCodeClass1ChangeBehavior(final TextField<String> mainCodeOfCodeClass1, final BootstrapMultiSelect<Code> codeClass1) {
        codeClass1.add(makeCodeClass1ChangeBehavior(codeClass1, mainCodeOfCodeClass1));
    }

    /*
     * Behavior to set the field mainCodeOfCodeClass1 to the first selection of the codeClass1 field.
     * Needs to be capable of handling the situation where the initial first choice is removed from the codeClass1 field.
     */
    private OnChangeAjaxBehavior makeCodeClass1ChangeBehavior(final BootstrapMultiSelect<Code> codeClass1, final TextField<String> mainCodeOfCodeClass1) {
        return new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                if (codeClass1 != null && codeClass1.getModelObject() != null) {
                    final Collection<Code> codesOfClass1 = codeClass1.getModelObject();
                    switch (codesOfClass1.size()) {
                    case 0:
                        setMainCodeOfClass1(null, mainCodeOfCodeClass1, target);
                        break;
                    case 1:
                        setMainCodeOfClass1(codesOfClass1.iterator().next().getCode(), mainCodeOfCodeClass1, target);
                        break;
                    default:
                        ensureMainCodeIsPartOfCodes(codesOfClass1, mainCodeOfCodeClass1, target);
                        break;
                    }
                }
            }

            private void setMainCodeOfClass1(final String code, final TextField<String> mainCodeOfCodeClass1, final AjaxRequestTarget target) {
                mainCodeOfCodeClass1.setModelObject(code);
                target.add(mainCodeOfCodeClass1);
            }

            private void ensureMainCodeIsPartOfCodes(Collection<Code> codesOfClass1, TextField<String> mainCodeOfCodeClass1, AjaxRequestTarget target) {
                final Optional<String> main = Optional.ofNullable(mainCodeOfCodeClass1.getModelObject());
                final Optional<String> match = codesOfClass1.stream().map(Code::getCode).filter(c -> main.isPresent() && main.get().equals(c)).findFirst();
                if (main.isPresent() && !match.isPresent()) {
                    mainCodeOfCodeClass1.setModelObject(null);
                    target.add(mainCodeOfCodeClass1);
                }
            }
        };
    }

    @Override
    protected void onXmlPasteModalPanelClose(XmlPasteModalPanel xmlPastePanel, AjaxRequestTarget target) {
        List<PubmedArticleFacade> articles = pubmedService.extractArticlesFrom(xmlPastePanel.getPastedContent());
        if (articles.isEmpty()) {
            warn("XML could not be parsed...");
        } else {
            info("Extracted " + articles.size() + " articles from PubMed. 1st with pmid " + articles.get(0).getPmId() + " and title " + articles.get(0).getTitle());
        }
        Paper paper = getModelObject();
        if (paper.getPmId() != null) {
            for (PubmedArticleFacade a : articles) {
                if (String.valueOf(paper.getPmId()).equals(a.getPmId())) {
                    compareFieldsOf(paper, a);
                    break;
                }
            }
        }
        target.add(((BasePage<?>) getPage()).getFeedbackPanel());
    }

    private void compareFieldsOf(Paper p, PubmedArticleFacade a) {
        compareFields(a.getAuthors(), p.getAuthors(), "authors");
        compareFields(a.getFirstAuthor(), p.getFirstAuthor(), "firstAuthor");
        compareFields(a.getTitle(), p.getTitle(), "title");
        compareFields(String.valueOf(a.getPublicationYear()), String.valueOf(p.getPublicationYear()), "publication year");
        compareFields(a.getLocation(), p.getLocation(), "location");
        compareFields(a.getDoi(), p.getDoi(), "DOI");
        compareFields(a.getOriginalAbstract(), p.getOriginalAbstract(), "abstract");
    }

    private void compareFields(String pmField, String paperField, String fieldName) {
        if (pmField != null && !pmField.equals(paperField)) {
            warn("Pubmed " + fieldName + ": " + pmField);
        }
    }

}
