package ch.difty.scipamato.core.entity;

import java.io.Serializable;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.CodeClassId;

/**
 * A class implementing {@link CodeBox} is a container for {@link Code}s. It
 * allows to access all codes accumulated or only the codes of a given
 * {@link CodeClass}.
 *
 * @author u.joss
 */
public interface CodeBox extends Serializable {

    /**
     * @return all codes contained in the code box.
     */
    @NotNull
    List<Code> getCodes();

    /**
     * returns all codes of the code class with the given id.
     *
     * @param codeClassId
     *     the id of the code class to clear all codes for
     * @return a list of codes
     */
    @NotNull
    List<Code> getCodesBy(@NotNull CodeClassId codeClassId);

    /**
     * Add the code to the code box.
     *
     * @param code
     *     the code to add
     */
    void addCode(@Nullable Code code);

    /**
     * Add all codes to the code box
     *
     * @param newCodes
     *     the codes to add
     */
    void addCodes(@NotNull List<Code> newCodes);

    /**
     * Clears all codes in the code box.
     */
    void clear();

    /**
     * Clears all codes of a given code class
     *
     * @param codeClassId
     *     the id of the code class to clear all codes for
     */
    void clearBy(@NotNull CodeClassId codeClassId);

    /**
     * @return true if the CodeBox holds not codes - false otherwise
     */
    boolean isEmpty();
}
