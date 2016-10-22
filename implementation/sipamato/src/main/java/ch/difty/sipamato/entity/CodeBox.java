package ch.difty.sipamato.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.list.UnmodifiableList;
import org.springframework.util.CollectionUtils;

/**
 * The {@link CodeBox} is a container for Codes. It currently maintains them as a flat list
 * but will evolve to be able to retrieve them by code class.
 *
 * @author u.joss
 */
public class CodeBox {

    private final List<Code> codes = new ArrayList<>();

    public boolean isEmpty() {
        return codes.isEmpty();
    }

    public int size() {
        return codes.size();
    }

    public void addCode(final Code code) {
        if (isNewAndNonNull(code))
            codes.add(code);
    }

    private boolean isNewAndNonNull(final Code code) {
        return code != null && !codes.contains(code);
    }

    public List<Code> getCodes() {
        return new UnmodifiableList<Code>(codes);
    }

    public void addCodes(final List<Code> newCodes) {
        if (!CollectionUtils.isEmpty(newCodes))
            codes.addAll(newCodes.stream().distinct().filter(this::isNewAndNonNull).collect(Collectors.toList()));
    }

    public void clear() {
        codes.clear();
    }

}
