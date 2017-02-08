package ch.difty.sipamato.entity;

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
 * The paper specific implementation of the {@link CodeBox} interface.
 *
 * @author u.joss
 */
public class PaperCodeBox implements CodeBox {

    private static final long serialVersionUID = 1L;

    private final List<Code> codes = new ArrayList<>();

    @Override
    public boolean isEmpty() {
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

    /** {@inheritDoc} */
    @Override
    public void addCode(final Code code) {
        if (isNewAndNonNull(code))
            codes.add(code);
    }

    private boolean isNewAndNonNull(final Code code) {
        return code != null && !codes.contains(code);
    }

    /** {@inheritDoc} */
    @Override
    public List<Code> getCodes() {
        return new UnmodifiableList<>(codes);
    }

    /** {@inheritDoc} */
    @Override
    public List<Code> getCodesBy(final CodeClassId codeClassId) {
        return new UnmodifiableList<>(collectBy(nullSafe(codeClassId)));
    }

    /** {@inheritDoc} */
    @Override
    public void addCodes(final List<Code> newCodes) {
        if (!CollectionUtils.isEmpty(newCodes))
            codes.addAll(newCodes.stream().distinct().filter(this::isNewAndNonNull).collect(Collectors.toList()));
    }

    /** {@inheritDoc} */
    @Override
    public void clear() {
        codes.clear();
    }

    /** {@inheritDoc} */
    @Override
    public void clearBy(final CodeClassId codeClassId) {
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

    @Override
    public int hashCode() {
        final List<Code> sorted = new ArrayList<>(codes);
        sorted.sort((c1, c2) -> c1.getCode().compareTo(c2.getCode()));
        final int prime = 31;
        int result = 1;
        result = prime * result + sorted.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final List<Code> thisSorted = new ArrayList<>(codes);
        thisSorted.sort((c1, c2) -> c1.getCode().compareTo(c2.getCode()));
        final PaperCodeBox other = (PaperCodeBox) obj;
        final List<Code> otherSorted = new ArrayList<>(other.codes);
        otherSorted.sort((c1, c2) -> c1.getCode().compareTo(c2.getCode()));
        if (!thisSorted.equals(otherSorted))
            return false;
        return true;
    }

}
