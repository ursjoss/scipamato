package ch.difty.sipamato.entity;

public class PaperFilter extends SipamatoFilter {

    private static final long serialVersionUID = 1L;

    public static final String SEARCH_MASK = "searchMask";

    private String searchMask;

    public String getSearchMask() {
        return searchMask;
    }

    public void setSearchMask(String searchMask) {
        this.searchMask = searchMask;
    }

}
