package br.com.vah.lance.reports;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.fill.JRAbstractLRUVirtualizer;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by jairoportela on 29/04/2016.
 */
@Stateless
public class ReportLoader implements Serializable {

  public StreamedContent imprimeRelatorio(String reportName, Map<String, Object> parameters, List datasource, String downloadFilename) {

    try {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      facesContext.responseComplete();
      ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
      BufferedImage logo = ImageIO.read(scontext.getResource("/resources/img/logo.png"));
      parameters.put("LOGO", logo);
    } catch (Exception e) {
      e.printStackTrace();
    }

    parameters.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    parameters.put("EMISSAO", sdf.format(new Date()));

    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datasource);

    InputStream report = null;

    try {

      FacesContext facesContext = FacesContext.getCurrentInstance();

      facesContext.responseComplete();

      ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();

      //Instancia o virtualizador
      JRAbstractLRUVirtualizer virtualizer = new JRGzipVirtualizer(100);

      parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);

      JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath(String.format("/resources/reports/%s.jasper", reportName)), parameters, ds);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      JRPdfExporter exporter = new JRPdfExporter();

      exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
      exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
      exporter.setConfiguration(new SimplePdfExporterConfiguration());

      exporter.exportReport();

      report = new ByteArrayInputStream(baos.toByteArray());

    } catch (Exception e) {

      e.printStackTrace();

    }

    DefaultStreamedContent dsc = new DefaultStreamedContent(report);
    dsc.setContentType("application/pdf");

    if (downloadFilename == null) {
      dsc.setName(String.format("%s.pdf", reportName));
    } else {
      dsc.setName(String.format("%s.pdf", downloadFilename));
    }

    return dsc;

  }
}
