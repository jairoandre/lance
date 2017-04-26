package br.com.vah.lance.service;

import br.com.vah.lance.dto.*;
import br.com.vah.lance.entity.usrdbvah.Cobranca;
import br.com.vah.lance.entity.usrdbvah.ItemCobranca;
import br.com.vah.lance.exception.LanceBusinessException;
import javassist.runtime.Desc;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jairoportela on 19/07/2016.
 */
@Stateless
public class ArquivoRemessaService implements Serializable {

  private
  @Inject
  CobrancaService cobrancaService;

  public Arquivo gerarArquivoRemessa(List<Cobranca> cobrancas) throws LanceBusinessException {
    List<Detalhe> detalhes = new ArrayList<>();

    Integer sequencial = 2;

    for (Cobranca cobranca : cobrancas) {
      if (cobranca.getId() == null) {
        throw new LanceBusinessException("Somente cobranças gravadas podem ser incluídas em um arquivo de remessa.");
      }
      Detalhe detalhe = new Detalhe(cobranca, sequencial++);
      detalhe.setMulta(new Multa(cobranca, sequencial++));
      detalhes.add(detalhe);
    }

    Arquivo arquivo = new Arquivo(detalhes, sequencial);

    return arquivo;
  }

  public StreamedContent gerarArquivo(List<Cobranca> cobrancas) throws LanceBusinessException {

    Arquivo arquivo = gerarArquivoRemessa(cobrancas);

    String content = arquivo.print();

    ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes());

    DefaultStreamedContent dsc = new DefaultStreamedContent(bais);

    dsc.setContentType("text/plain");
    dsc.setContentEncoding("UTF-8");
    dsc.setName(String.format("%s%s.txt", "AR", ArquivoUtils.formatDate(new Date(), "ddMMyy")));

    return dsc;
  }

  public StreamedContent gerarDescritivo(List<Cobranca> cobrancas) throws LanceBusinessException {
    StringBuilder builder = new StringBuilder();
    for (Cobranca dettached : cobrancas) {
      Cobranca cobranca = cobrancaService.initializeDescritivo(dettached.getId());
      if (cobranca.getId() == null) {
        throw new LanceBusinessException("Somente cobranças gravadas podem gerar o descritivo");
      }
      if (cobranca.getSetor() != null) {
        builder.append((new Descritivo(cobranca)).print());
      }
      for (ItemCobranca item : cobranca.getDescritivo()) {
        builder.append((new Descritivo(item)).print());
      }
    }
    String content = builder.toString();
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(content.toString().getBytes("UTF-8"));
      DefaultStreamedContent dsc = new DefaultStreamedContent(bais);
      dsc.setContentType("text/plain");
      dsc.setName(String.format("%s%s.txt", "DR", ArquivoUtils.formatDate(new Date(), "ddMMyy")));
      return dsc;
    } catch (UnsupportedEncodingException uee) {
      throw new LanceBusinessException("Codificação de caracteres não suportada.");
    }
  }

}
