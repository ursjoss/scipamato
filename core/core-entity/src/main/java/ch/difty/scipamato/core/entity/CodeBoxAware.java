package ch.difty.scipamato.core.entity;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.CodeClassId;

/**
 * Accessor methods to the {@link Code}s for entities holding an instance of the
 * CodeBox
 *
 * @author u.joss
 */
public interface CodeBoxAware {

    /**
     * @return all {@link Code}s assigned
     */
    @NotNull
    List<Code> getCodes();

    /**
     * @param ccId
     *     the code class id
     * @return all {@link Code}s of a specific {@link CodeClassId} assigned
     */
    @NotNull
    List<Code> getCodesOf(@NotNull CodeClassId ccId);

    /**
     * Clear all assigned codes.
     */
    void clearCodes();

    /**
     * Clear all assigned codes of the specified {@link CodeClassId}
     *
     * @param ccId
     *     the code class id
     */
    void clearCodesOf(@NotNull CodeClassId ccId);

    /**
     * Add the specified {@link Code}
     *
     * @param code
     *     to add
     */
    void addCode(@Nullable Code code);

    /**
     * Add the entire list of {@link Code}s.
     *
     * @param codes
     *     to add
     */
    void addCodes(@NotNull List<Code> codes);

    void setCodesExcluded(@Nullable final String codesExcluded);

    /**
     * @return the String with the codes to be excluded
     */
    @Nullable
    String getCodesExcluded();

    /**
     * @return as list of the individual codes considered excluded
     */
    @NotNull
    List<String> getExcludedCodeCodes();
}
