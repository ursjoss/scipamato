package ch.difty.scipamato.core.logic.parsing;

import lombok.Value;

/**
 * Value object used by an author parser to represent one individual author
 * extracted from the raw author string passed to its constructor.
 *
 * @author u.joss
 */
@Value
public class Author {

    /**
     * The input to the AuthorParser. The format depends on the configurable
     * concrete author parser implementation.
     */
    private String authorString;

    /**
     * The resulting last name of an author
     */
    private String lastName;

    /**
     * The resulting first name of an author
     */
    private String firstName;

}