package ch.difty.sipamato.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.list.UnmodifiableList;
import org.springframework.util.CollectionUtils;

import ch.difty.sipamato.lib.AssertAs;

/**
 * The {@link CodeBox} is a container for {@link Code}s.
 * It allows to access all codes cumulated or only the codes of a given {@link CodeClass}.
 *
 * @author u.joss
 */
class CodeBox implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Code> codes = new ArrayList<>();

    boolean isEmpty() {
        return codes.isEmpty();
    }

    int size() {
        return codes.size();
    }

    int sizeOf(final CodeClassId codeClassId) {
        return collectBy(nullSafe(codeClassId)).size();
    }

    private CodeClassId nullSafe(final CodeClassId codeClassId) {
        return AssertAs.notNull(codeClassId, "codeClassId");
    }

    private List<Code> collectBy(final CodeClassId ccId) {
        return codes.stream().filter(isMatching(ccId)).collect(Collectors.toList());
    }

    private Predicate<? super Code> isMatching(final CodeClassId ccId) {
        return c -> ccId.getId() == c.getCodeClass().getId().intValue();
    }

    void addCode(final Code code) {
        if (isNewAndNonNull(code))
            codes.add(code);
    }

    private boolean isNewAndNonNull(final Code code) {
        return code != null && !codes.contains(code);
    }

    List<Code> getCodes() {
        return new UnmodifiableList<Code>(codes);
    }

    List<Code> getCodesBy(final CodeClassId codeClassId) {
        return new UnmodifiableList<Code>(collectBy(nullSafe(codeClassId)));
    }

    void addCodes(final List<Code> newCodes) {
        if (!CollectionUtils.isEmpty(newCodes))
            codes.addAll(newCodes.stream().distinct().filter(this::isNewAndNonNull).collect(Collectors.toList()));
    }

    void clear() {
        codes.clear();
    }

    void clearBy(final CodeClassId codeClassId) {
        codes.removeIf(isMatching(nullSafe(codeClassId)));
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        final Map<CodeClass, List<Code>> map = codes.stream().collect(Collectors.groupingBy(Code::getCodeClass, LinkedHashMap::new, Collectors.toList()));
        String delim = "";
        builder.append("[");
        for (final Entry<CodeClass, List<Code>> e : map.entrySet()) {
            builder.append(delim).append("codesOfClass").append(e.getKey().getId()).append("=");
            builder.append(e.getValue());
            delim = ",";
        }
        builder.append("]");
        return builder.toString();
    }

}
