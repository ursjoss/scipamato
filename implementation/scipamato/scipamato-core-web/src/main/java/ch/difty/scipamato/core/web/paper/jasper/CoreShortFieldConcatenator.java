package ch.difty.scipamato.core.web.paper.jasper;

import ch.difty.scipamato.core.entity.Paper;

/**
 * Gathers the content for the the fields methods, population and result based on a Paper.
 * <p>
 * The content may either be those fields themselves or some concatenated short fields.
 */
public interface CoreShortFieldConcatenator {

    /**
     * @param paper
     *     the paper
     * @return the methods field content
     */
    String methodsFrom(Paper paper);

    /**
     * @param paper
     *     the paper
     * @return the population field content
     */
    String populationFrom(Paper paper);

    /**
     * @param paper
     *     the paper
     * @return the result field content
     */
    String resultFrom(Paper paper);
}
