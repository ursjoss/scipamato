package ch.difty.sipamato.entity;

import java.io.Serializable;

public class PaperFilter implements Serializable {

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
