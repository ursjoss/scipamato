package ch.difty.scipamato.core.web.paper.jasper;

import org.jetbrains.annotations.NotNull;

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
    @NotNull
    String methodsFrom(@NotNull Paper paper, @NotNull ReportHeaderFields rhf);

    /**
     * @param paper
     *     the paper
     * @return the population field content
     */
    @NotNull
    String populationFrom(@NotNull Paper paper, @NotNull ReportHeaderFields rhf);

    /**
     * @param paper
     *     the paper
     * @return the result field content
     */
    @NotNull
    String resultFrom(@NotNull Paper paper, @NotNull ReportHeaderFields rhf);
}
