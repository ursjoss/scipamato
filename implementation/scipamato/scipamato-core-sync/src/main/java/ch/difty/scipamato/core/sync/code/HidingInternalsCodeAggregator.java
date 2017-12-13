package ch.difty.scipamato.core.sync.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * The {@link HidingInternalsCodeAggregator} has the purpose of
 * <ul>
 * <li> Providing aggregated codes by </li>
 *    <ul>
 *    <li> Enriching codes with aggregated codes </li>
 *    <li> Filtering out internal codes </li>
 *    </ul>
 * <li> providing the aggregated </li>
 *    <ul>
 *    <li> codesPopulation values </li>
 *    <li> codesStudyDesign values </li>
 *    </ul>
 * </ul>
 * @author u.joss
 */
@Service
@Scope("prototype")
public class HidingInternalsCodeAggregator implements CodeAggregator {

    private final List<String> internalCodes = new ArrayList<>();
    private final List<String> codes = new ArrayList<>();
    private final List<Short> codesPopulation = new ArrayList<>();
    private final List<Short> codesStudyDesign = new ArrayList<>();

    @Override
    public void setInternalCodes(final List<String> internalCodes) {
        this.internalCodes.clear();
        this.internalCodes.addAll(internalCodes);
    }

    @Override
    public void load(final String[] codes) {
        clearAll();
        if (codes != null) {
            this.codes.addAll(aggregateCodes(codes));
            this.codesPopulation.addAll(gatherCodesPopulation());
            this.codesStudyDesign.addAll(gatherCodesStudyDesign());
        }
    }

    private void clearAll() {
        this.codes.clear();
        this.codesPopulation.clear();
        this.codesStudyDesign.clear();
    }

    private List<String> aggregateCodes(final String[] codeArray) {
        final List<String> codeList = filterAndEnrich(codeArray);
        Collections.sort(codeList);
        return codeList;
    }

    /**
     * HARDCODED consider moving aggregation into some table in scipamato-core (see also CodeSyncConfig#selectSql)
     */
    private List<String> filterAndEnrich(final String[] codeArray) {
        final Set<String> codes = new HashSet<>();
        for (final String code : codeArray) {
            if ("5A".equals(code) || "5B".equals(code) || "5C".equals(code))
                codes.add("5abc");
            else if (!internalCodes.contains(code))
                codes.add(code);
        }
        return new ArrayList<>(codes);
    }

    private List<Short> gatherCodesPopulation() {
        final List<Short> pcs = new ArrayList<>();
        if (codes.stream().anyMatch(x -> "3A".equals(x) || "3B".equals(x)))
            pcs.add((short) 1);
        if (codes.stream().anyMatch(x -> "3C".equals(x)))
            pcs.add((short) 2);
        return pcs;
    }

    private List<Short> gatherCodesStudyDesign() {
        final List<Short> csds = new ArrayList<>();
        if (codes.stream().anyMatch(x -> "5abc".equals(x)))
            csds.add((short) 1);
        if (codes.stream().anyMatch(x -> "5E".equals(x) || "5F".equals(x) || "5G".equals(x) || "5H".equals(x) || "5I".equals(x)))
            csds.add((short) 2);
        if (codes.stream().anyMatch(x -> "5U".equals(x) || "5M".equals(x)))
            csds.add((short) 3);
        return csds;
    }

    @Override
    public String[] getAggregatedCodes() {
        return codes.toArray(new String[codes.size()]);
    }

    @Override
    public Short[] getCodesPopulation() {
        return codesPopulation.toArray(new Short[codesPopulation.size()]);
    }

    @Override
    public Short[] getCodesStudyDesign() {
        return codesStudyDesign.toArray(new Short[codesStudyDesign.size()]);
    }

}
