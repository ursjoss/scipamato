package ch.difty.scipamato.publ.entity;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * PopulationCode contains aggregated Codes of Code Class 3:
 * <ol>
 * <li>Children: Codes 3A + 3B</li>
 * <li>Adults: Codes 3C</li>
 * </ol>
 *
 * @author u.joss
 */
public enum PopulationCode {
    CHILDREN((short) 1),
    ADULTS((short) 2);

    private static final Map<Short, PopulationCode> ID2CODE = Stream
        .of(values())
        .collect(toMap(PopulationCode::getId, e -> e));

    private final short id;

    PopulationCode(final short id) {
        this.id = id;
    }

    public static Optional<PopulationCode> of(final short id) {
        return Optional.ofNullable(ID2CODE.get(id));
    }

    public short getId() {
        return id;
    }

}
