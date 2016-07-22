package br.com.vah.lance.service;

import br.com.vah.lance.dto.ArquivoRemessa;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.ejb.Stateless;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jairoportela on 19/07/2016.
 */
@Stateless
public class ArquivoRemessaService implements Serializable {

  public StreamedContent gerarArquivo() {

    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");

    ArquivoRemessa arquivoRemessa = new ArquivoRemessa(sdf.format(new Date()));

    String content = arquivoRemessa.print();

    ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes());

    DefaultStreamedContent dsc = new DefaultStreamedContent(bais);

    dsc.setContentType("text/plain");
    dsc.setName(String.format("%s.txt", "arquivo"));

    return dsc;

  }
}
