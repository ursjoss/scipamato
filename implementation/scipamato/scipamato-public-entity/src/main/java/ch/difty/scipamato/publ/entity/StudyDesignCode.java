package ch.difty.scipamato.publ.entity;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * StudyDesignCode contains aggregated Codes of Code Class 5:
 * <ol>
 * <li>Experimental Studies: Codes 5A + 5B + 5C</li>
 * <li>Epidemiological Studies: Codes 5E + 5F + 5G + 5H + 5I</li>
 * <li>Overview Methodology: Codes 5U + 5M</li>
 * </ol>
 *
 * @author u.joss
 */
public enum StudyDesignCode {
    EXPERIMENTAL((short) 1),
    EPIODEMIOLOGICAL((short) 2),
    OVERVIEW_METHODOLOGY((short) 3);

    private static final Map<Short, StudyDesignCode> ID2CODE = Stream
        .of(values())
        .collect(toMap(StudyDesignCode::getId, e -> e));

    private final short id;

    StudyDesignCode(final short id) {
        this.id = id;
    }

    public static Optional<StudyDesignCode> of(final short id) {
        return Optional.ofNullable(ID2CODE.get(id));
    }

    public short getId() {
        return id;
    }

}
