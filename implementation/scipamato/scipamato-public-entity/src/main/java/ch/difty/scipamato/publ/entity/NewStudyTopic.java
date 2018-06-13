package ch.difty.scipamato.publ.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import ch.difty.scipamato.common.entity.FieldEnumType;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NewStudyTopic extends PublicEntity {

    private static final long serialVersionUID = 1L;

    private final int            sort;
    private final String         title;
    private final List<NewStudy> studies;

    public NewStudyTopic(final int sort, final String title) {
        this(sort, title, new ArrayList<>());
    }

    public enum NewStudyTopicFields implements FieldEnumType {
        SORT("sort"),
        TITLE("title"),
        STUDIES("studies");

        private String name;

        NewStudyTopicFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

}
