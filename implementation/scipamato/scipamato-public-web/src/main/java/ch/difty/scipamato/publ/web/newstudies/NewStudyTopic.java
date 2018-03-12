package ch.difty.scipamato.publ.web.newstudies;

import java.io.Serializable;
import java.util.List;

import lombok.Value;

@Value
public class NewStudyTopic implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int            sort;
    private final String         title;
    private final List<NewStudy> studies;

}
