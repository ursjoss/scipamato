package ch.difty.sipamato.web.pages.jasper;

import java.io.Serializable;
import java.util.Collection;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class JasperBeanCollectionDataSource extends JRBeanCollectionDataSource implements Serializable {

    private static final long serialVersionUID = 1L;

    public JasperBeanCollectionDataSource(Collection<?> beanCollection) {
        super(beanCollection);
    }
    
    public JasperBeanCollectionDataSource(Collection<?> beanCollection, boolean isUseFieldDescription) {
        super(beanCollection, isUseFieldDescription);
    }

    public JasperBeanCollectionDataSource cloneDataSource()
    {
        return new JasperBeanCollectionDataSource(getData());
    }

}
