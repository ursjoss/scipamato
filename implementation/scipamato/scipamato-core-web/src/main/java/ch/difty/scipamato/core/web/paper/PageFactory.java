package ch.difty.scipamato.core.web.paper;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.scipamato.common.web.component.SerializableBiConsumer;
import ch.difty.scipamato.common.web.component.SerializableBiFunction;
import ch.difty.scipamato.common.web.component.SerializableConsumer;
import ch.difty.scipamato.core.entity.search.SearchCondition;

/**
 * Together with spring dependency injection, this interface and its
 * implementing class have the purpose to help untangle the dependency cycles
 * that arise with wicket components (pages, panels) referencing each other,
 * e.g. in the context of setResponsePage.
 *
 * @author Urs Joss
 */
public interface PageFactory {

    /**
     * Returns a (bi) consumer that sets the responsePage of the provided container
     * (page/panel) to a PaperSearchCriteriaPage exactly when you let it accept a
     * specific search condition model and searchOrderId
     *
     * @param container
     *     the page or panel to set the response page
     * @return the SerializableBiConsumer you then can let accept the constructor
     *     arguments of the PaperSearchCriteriaPage
     */
    SerializableBiConsumer<IModel<SearchCondition>, Long> setResponsePageToPaperSearchCriteriaPageConsumer(
        MarkupContainer container);

    /**
     * Returns a consumer that sets the responsePage of the provided container
     * (page/panel) to a PaperSearchPageWithPage exactly when you let it accept an
     * instance of {@link PageParameters}.
     *
     * @param container
     *     the page or panel to set the response page
     * @return the SerializableConsumer you can then let accept the constructor
     *     argument (pageParameters) of the PaperSearchPageWithPage
     */
    SerializableConsumer<PageParameters> setResponsePageToPaperSearchPageConsumer(MarkupContainer container);

    /**
     * Returns a (bi) function that instantiates a new instance of a
     * PaperSearchCriteriaPage exactly when you apply a specific search condition
     * model and searchOrderId to it.
     *
     * @return the SerializableBiFunction on which you can then apply the
     *     constructor arguments of the PaperSearchCriteriaPage
     */
    SerializableBiFunction<IModel<SearchCondition>, Long, GenericWebPage<SearchCondition>> newPaperSearchCriteriaPage();

}
