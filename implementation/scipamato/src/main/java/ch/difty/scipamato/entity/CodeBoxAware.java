package ch.difty.scipamato.entity;

import java.util.List;

/**
 * Accessor methods to the {@link Code}s for entities holding an instance of the CodeBox
 *
 * @author u.joss
 */
public interface CodeBoxAware {

    /**
     * @return all {@link Code}s assigned
     */
    List<Code> getCodes();

    /**
     * @param ccId the code class id
     * @return all {@link Code}s of a specific {@link CodeClassId} assigned
     */
    List<Code> getCodesOf(CodeClassId ccId);

    /**
     * Clear all assigned codes.
     */
    void clearCodes();

    /**
     * Clear all assigned codes of the specified {@link CodeClassId}
     * @param ccId the code class id
     */
    void clearCodesOf(CodeClassId ccId);

    /**
     * Add the specified {@link Code}
     *
     * @param code to add
     */
    void addCode(Code code);

    /**
     * Add the entire list of {@link Code}s.
     *
     * @param codes to add
     */
    void addCodes(List<Code> codes);

}
