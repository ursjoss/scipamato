package ch.difty.scipamato.core.web.resources.jasper;

import static org.assertj.core.api.Assertions.assertThat;

import net.sf.jasperreports.engine.JRException;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.junit.jupiter.api.Test;

class JasperReportExceptionTest {

    private JasperReportException exception;

    @Test
    void makeJasperReportExceptionWithJRException() {
        final JRException e = new JRException("foo");
        exception = new JasperReportException(e);
        assertThat(exception.getMessage()).isEqualTo("net.sf.jasperreports.engine.JRException: foo");
    }

    @Test
    void makeJasperReportExceptionWithResourceStreamNotFoundException() {
        final ResourceStreamNotFoundException e = new ResourceStreamNotFoundException("foo");
        exception = new JasperReportException(e);
        assertThat(exception.getMessage()).isEqualTo(
            "org.apache.wicket.util.resource.ResourceStreamNotFoundException: foo");
    }

    @Test
    void makeJasperReportException_withStringMessage() {
        exception = new JasperReportException("foo");
        assertThat(exception.getMessage()).isEqualTo("foo");
    }
}
