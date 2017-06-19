package ch.difty.sipamato.web.panel.paper;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.web.component.SerializableSupplier;
import ch.difty.sipamato.web.jasper.summary.PaperSummaryDataSource;
import ch.difty.sipamato.web.pages.Mode;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;

/**
 * The {@link SearchablePaperPanel} offers a look-alike panel to the paper entry panel,
 * but for entering search terms. It therefore hides some components that are visible
 * in the edit panel. It does not implement specific behavior and therefore
 * simply implements the abstract methods of the {@link PaperPanel} as no-ops.
 *
 * @author u.joss
 */
public abstract class SearchablePaperPanel extends PaperPanel<SearchCondition> {

    private static final long serialVersionUID = 1L;

    public SearchablePaperPanel(String id, IModel<SearchCondition> model) {
        super(id, model, Mode.SEARCH);
    }

    @Override
    protected PaperSummaryDataSource getSummaryDataSource() {
        return null;
    }

    @Override
    protected void reflectPersistedChangesViaTimer(TextField<Integer> id, TextField<String> created, TextField<String> modified) {
        // don't add the behavior
    }

    protected boolean hasPubMedId() {
        return false;
    }

    protected BootstrapButton newNavigationButton(String id, GlyphIconType icon, SerializableSupplier<Boolean> isEnabled, SerializableSupplier<Long> idSupplier) {
        final BootstrapButton btn = new BootstrapButton(id, Model.of(""), Buttons.Type.Default);
        btn.setVisible(false);
        return btn;
    }

    protected BootstrapButton newExcludeButton(String id) {
        BootstrapButton exclude = new BootstrapButton(id, new StringResourceModel("button.exclude.label"), Buttons.Type.Default);
        exclude.setVisible(false);
        return exclude;
    }

}
