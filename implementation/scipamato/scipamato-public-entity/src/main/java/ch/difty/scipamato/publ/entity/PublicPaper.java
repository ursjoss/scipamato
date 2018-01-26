package ch.difty.scipamato.publ.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PublicPaper extends PublicEntity {

    private static final long serialVersionUID = 1L;

    public static final String ID        = "id";
    public static final String NUMBER    = "number";
    public static final String PMID      = "pmId";
    public static final String AUTHORS   = "authors";
    public static final String TITLE     = "title";
    public static final String LOCATION  = "location";
    public static final String PUBL_YEAR = "publicationYear";

    public static final String GOALS      = "goals";
    public static final String METHODS    = "methods";
    public static final String POPULATION = "population";
    public static final String RESULT     = "result";
    public static final String COMMENT    = "comment";

    private Long    id;
    private Long    number;
    private Integer pmId;
    private String  authors;
    private String  title;
    private String  location;
    private Integer publicationYear;

    private String goals;
    private String methods;
    private String population;
    private String result;
    private String comment;

}
