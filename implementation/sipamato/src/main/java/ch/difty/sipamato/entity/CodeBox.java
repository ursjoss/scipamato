package ch.difty.sipamato.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.list.UnmodifiableList;
import org.springframework.util.CollectionUtils;

import ch.difty.sipamato.lib.AssertAs;

/**
 * The {@link CodeBox} is a container for Codes it allows to retrieve all codes or by {@link CodeClass}.
 *
 * TODO change to split the codes by code class when adding, not when retrieving. 
 *
 * @author u.joss
 */
class CodeBox implements Serializable {

    private static final long serialVersionUID = 1L;

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

    public List<Code> getCodesBy(final CodeClassId codeClassId) {
        final CodeClassId ccId = AssertAs.notNull(codeClassId, "codeClassId");
        final List<Code> result = codes.stream().filter(c -> ccId.getId() == c.getCodeClass().getId().intValue()).collect(Collectors.toList());
        return new UnmodifiableList<Code>(result);
    }

    public void addCodes(final List<Code> newCodes) {
        if (!CollectionUtils.isEmpty(newCodes))
            codes.addAll(newCodes.stream().distinct().filter(this::isNewAndNonNull).collect(Collectors.toList()));
    }

    public void clear() {
        codes.clear();
    }

    public void clearBy(final CodeClassId codeClassId) {
        final CodeClassId ccId = AssertAs.notNull(codeClassId, "codeClassId");
        codes.removeIf(c -> ccId.getId() == c.getCodeClass().getId().intValue());
    }

}
