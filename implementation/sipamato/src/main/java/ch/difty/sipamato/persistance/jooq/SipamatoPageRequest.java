package ch.difty.sipamato.persistance.jooq;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class SipamatoPageRequest extends PageRequest {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new {@link PageRequest}. Pages are zero indexed, thus providing 0 for {@code page} will return the first page.
     *
     * @param page zero-based page index.
     * @param size the size of the page to be returned.
     */
    public SipamatoPageRequest(int page, int size) {
        super(page, size, null);
    }
    
    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     *
     * @param page zero-based page index.
     * @param size the size of the page to be returned.
     * @param direction the direction of the {@link Sort} to be specified, can be {@literal null}.
     * @param properties the properties to sort by, must not be {@literal null} or empty.
     */
    public SipamatoPageRequest(int page, int size, Direction direction, String... properties) {
        super(page, size, new Sort(direction, properties));
    }

}
