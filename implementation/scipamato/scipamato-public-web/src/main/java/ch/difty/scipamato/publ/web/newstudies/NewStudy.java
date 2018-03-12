package ch.difty.scipamato.publ.web.newstudies;

import java.io.Serializable;

import lombok.Value;

@Value
public class NewStudy implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int sort;
    private final long   number;
    private final String reference;
    private final String headline;
    private final String description;

}
