package ch.difty.sipamato.web;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;

/**
 * The brandImage in the default Navbar is stateful and therefore causes an issue with using a StatelessForm in the LoginPage.
 *
 * Temporarily overriding the method <code>newBrandImage</code> to provide the stateless hint.
 *
 * @author u.joss
 */
public class SipamatoNavbar extends Navbar {

    private static final long serialVersionUID = 1L;

    public SipamatoNavbar(String componentId) {
        super(componentId);
    }

    public SipamatoNavbar(String componentId, IModel<?> model) {
        super(componentId, model);
    }

    @Override
    protected Image newBrandImage(String markupId) {
        return new Image(markupId, Model.of("")) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();

                setVisible(getImageResourceReference() != null || getImageResource() != null);
            }

            /**
             * Work around issue with StatelessForm in LoginPage
             */
            @Override
            public boolean getStatelessHint() {
                return true;
            }

        };
    }
}
