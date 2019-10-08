package ch.difty.scipamato.core.sync.code;

import java.util.*;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The {@link HidingInternalsCodeAggregator} has the purpose of
 * <ul>
 * <li>Providing aggregated codes by
 * <ul>
 * <li>Enriching codes with aggregated codes</li>
 * <li>Filtering out internal codes</li>
 * </ul>
 * </li>
 * <li>providing the aggregated
 * <ul>
 * <li>codesPopulation values</li>
 * <li>codesStudyDesign values</li>
 * </ul>
 * </li>
 * </ul>
 *
 * @author u.joss
 */
@Component
@Scope("prototype")
public class HidingInternalsCodeAggregator implements CodeAggregator {

    private static final short CP_ID_1 = 1;
    private static final short CP_ID_2 = 2;

    private static final short CSD_ID_1 = 1;
    private static final short CSD_ID_2 = 2;
    private static final short CSD_ID_3 = 3;

    private final List<String> internalCodes    = new ArrayList<>();
    private final List<String> codes            = new ArrayList<>();
    private final List<Short>  codesPopulation  = new ArrayList<>();
    private final List<Short>  codesStudyDesign = new ArrayList<>();

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
     * HARDCODED consider moving aggregation into some table in scipamato-core (see
     * also CodeSyncConfig#selectSql)
     */
    private List<String> filterAndEnrich(final String[] codeArray) {
        final Set<String> filtered = new HashSet<>();
        for (final String code : codeArray) {
            if ("5A".equals(code) || "5B".equals(code) || "5C".equals(code))
                filtered.add("5abc");
            else if (!internalCodes.contains(code))
                filtered.add(code);
        }
        return new ArrayList<>(filtered);
    }

    private List<Short> gatherCodesPopulation() {
        final List<Short> pcs = new ArrayList<>();
        if (codes
            .stream()
            .anyMatch(x -> "3A".equals(x) || "3B".equals(x)))
            pcs.add(CP_ID_1);
        if (codes
            .stream()
            .anyMatch("3C"::equals))
            pcs.add(CP_ID_2);
        return pcs;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private List<Short> gatherCodesStudyDesign() {
        final List<Short> csds = new ArrayList<>();
        if (codes
            .stream()
            .anyMatch("5abc"::equals))
            csds.add(CSD_ID_1);
        if (codes
            .stream()
            .anyMatch(x -> "5E".equals(x) || "5F".equals(x) || "5G".equals(x) || "5H".equals(x) || "5I".equals(x)))
            csds.add(CSD_ID_2);
        if (codes
            .stream()
            .anyMatch(x -> "5U".equals(x) || "5M".equals(x)))
            csds.add(CSD_ID_3);
        return csds;
    }

    @Override
    public String[] getAggregatedCodes() {
        return codes.toArray(new String[0]);
    }

    @Override
    public Short[] getCodesPopulation() {
        return codesPopulation.toArray(new Short[0]);
    }

    @Override
    public Short[] getCodesStudyDesign() {
        return codesStudyDesign.toArray(new Short[0]);
    }

}
