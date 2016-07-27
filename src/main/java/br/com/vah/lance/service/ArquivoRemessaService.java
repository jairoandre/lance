package br.com.vah.lance.service;

import br.com.vah.lance.dto.Arquivo;
import br.com.vah.lance.dto.Detalhe;
import br.com.vah.lance.dto.Multa;
import br.com.vah.lance.entity.usrdbvah.Cobranca;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.ejb.Stateless;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jairoportela on 19/07/2016.
 */
@Stateless
public class ArquivoRemessaService implements Serializable {

  public Arquivo gerarArquivoRemessa(List<Cobranca> cobrancas) {
    List<Detalhe> detalhes = new ArrayList<>();

    Integer sequencial = 2;

    for (Cobranca cobranca : cobrancas) {
      Detalhe detalhe = new Detalhe(cobranca, sequencial++);
      detalhe.setMulta(new Multa(cobranca, sequencial++));
      detalhes.add(detalhe);
    }

    Arquivo arquivo = new Arquivo(detalhes, sequencial);

    return arquivo;
  }

  public StreamedContent gerarArquivo(List<Cobranca> cobrancas) {

    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");

    Arquivo arquivo = gerarArquivoRemessa(cobrancas);

    String content = arquivo.print();

    ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes());

    DefaultStreamedContent dsc = new DefaultStreamedContent(bais);

    dsc.setContentType("text/plain");
    dsc.setName(String.format("%s.txt", "arquivo"));

    return dsc;

  }
}
