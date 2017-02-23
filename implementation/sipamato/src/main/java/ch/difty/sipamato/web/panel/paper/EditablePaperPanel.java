package ch.difty.sipamato.web.panel.paper;

import java.util.Collection;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponent;
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
import ch.difty.sipamato.service.PubmedArticleService;
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
    private PubmedArticleService pubmedArticleService;

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
        final String populationLabel = new StringResourceModel(Paper.POPULATION + LABEL_RECOURCE_TAG, this, null).getString();
        final String methodsLabel = new StringResourceModel(Paper.FLD_METHODS + LABEL_RECOURCE_TAG, this, null).getString();
        final String resultLabel = new StringResourceModel(Paper.RESULT + LABEL_RECOURCE_TAG, this, null).getString();
        final String commentLabel = new StringResourceModel(Paper.COMMENT + LABEL_RECOURCE_TAG, this, null).getString();
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

    @SuppressWarnings("unchecked")
    @Override
    protected void getPubmedArticleAndCompare(AjaxRequestTarget target) {
        Paper paper = getModelObject();
        Integer pmId = paper.getPmId();
        if (pmId != null) {
            Optional<PubmedArticleFacade> pao = pubmedArticleService.getPubmedArticleWithPmid(pmId);
            if (pao.isPresent()) {
                PubmedArticleFacade pa = pao.get();
                if (String.valueOf(pmId).equals(pa.getPmId())) {
                    setFieldsIfBlankCompareOtherwise(paper, pa, target);
                }
            } else {
                error(new StringResourceModel("pubmedRetrieval.pmid.error", this, getModel()).getString());
            }
        } else {
            error(new StringResourceModel("pubmedRetrieval.pmid.missing", this, null).getString());
        }
        if (getPage() instanceof BasePage) {
            target.add(((BasePage<Paper>) getPage()).getFeedbackPanel());
        }
    }

    /**
     * If the field in the sipamato paper is null, the value from the pubmed paper is inserted.
     * Otherwise the two values are compared and differences are alerted (except for the abstract).
     */
    private void setFieldsIfBlankCompareOtherwise(Paper p, PubmedArticleFacade a, AjaxRequestTarget target) {
        boolean allMatching = true;
        boolean dirty = false;

        String fieldName = getLocalizedFieldName(Paper.AUTHORS);
        String value = a.getAuthors();
        if (p.getAuthors() == null) {
            p.setAuthors(value);
            dirty |= informChangedValue(fieldName, value, target, authors, firstAuthor);
        } else {
            allMatching &= compareFields(value, p.getAuthors(), fieldName);
        }

        fieldName = getLocalizedFieldName(Paper.FIRST_AUTHOR);
        value = a.getFirstAuthor();
        if (p.getFirstAuthor() == null) {
            p.setFirstAuthor(value);
            dirty |= informChangedValue(fieldName, value, target, firstAuthor);
        } else {
            allMatching &= compareFields(value, p.getFirstAuthor(), fieldName);
        }

        fieldName = getLocalizedFieldName(Paper.TITLE);
        value = a.getTitle();
        if (p.getTitle() == null) {
            p.setTitle(value);
            dirty |= informChangedValue(fieldName, value, target, title);
        } else {
            allMatching &= compareFields(value, p.getTitle(), fieldName);
        }

        fieldName = getLocalizedFieldName(Paper.PUBL_YEAR);
        value = a.getPublicationYear();
        if (p.getPublicationYear() == null) {
            try {
                p.setPublicationYear(Integer.parseInt(value));
                dirty |= informChangedValue(fieldName, value, target, publicationYear);
            } catch (Exception ex) {
                error(new StringResourceModel("year.parse.error", this, null).setParameters(value).getString());
            }
        } else {
            allMatching &= compareFields(value, String.valueOf(p.getPublicationYear()), fieldName);
        }

        fieldName = getLocalizedFieldName(Paper.LOCATION);
        value = a.getLocation();
        if (p.getLocation() == null) {
            p.setLocation(value);
            dirty |= informChangedValue(fieldName, value, target, location);
        } else {
            allMatching &= compareFields(value, p.getLocation(), fieldName);
        }

        fieldName = getLocalizedFieldName(Paper.DOI);
        value = a.getDoi();
        if (p.getDoi() == null) {
            p.setDoi(value);
            dirty |= informChangedValue(fieldName, value, target, doi);
        } else {
            allMatching &= compareFields(value, p.getDoi(), fieldName);
        }

        fieldName = getLocalizedFieldName(Paper.ORIGINAL_ABSTRACT);
        value = a.getOriginalAbstract();
        // not comparing abstract on purpose
        if (p.getOriginalAbstract() == null) {
            p.setOriginalAbstract(value);
            dirty |= informChangedValue(fieldName, value, target, originalAbstract);
        }

        if (dirty) {
            info(new StringResourceModel("pubmedRetrieval.dirty.info", this, null).getString());
        } else if (allMatching) {
            info(new StringResourceModel("pubmedRetrieval.no-difference.info", this, null).getString());
        }
    }

    private String getLocalizedFieldName(String fieldName) {
        return new StringResourceModel(fieldName + LABEL_RECOURCE_TAG, this, null).getString();
    }

    private boolean informChangedValue(String fieldName, String value, AjaxRequestTarget target, FormComponent<?>... fcs) {
        info(fieldName + ": " + value);
        if (fcs.length > 0) {
            target.add(fcs);
        }
        return true;
    }

    /**
     * compares the pubmed field with the sipamato article field. warns if it does not match.
     * @param pmField field from pubmed article
     * @param paperField field from sipamato paper
     * @param fieldName the (localized) name of the field, used to construct the warn message
     * @return true if fields match, false if there are differences
     */
    private boolean compareFields(String pmField, String paperField, String fieldName) {
        if (pmField != null && !pmField.equals(paperField)) {
            warn("PubMed " + fieldName + ": " + pmField);
            return false;
        }
        return true;
    }

}
