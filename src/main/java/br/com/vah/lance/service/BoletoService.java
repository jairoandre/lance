package br.com.vah.lance.service;

import br.com.vah.lance.entity.usrdbvah.Lancamento;
import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.febraban.*;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by jairoportela on 14/07/2016.
 */
public class BoletoService extends DataAccessService<Lancamento> {

  public BoletoService() { super(Lancamento.class); }

  public StreamedContent createBoleto(Lancamento lancamento) {

    Cedente cedente = new Cedente("Vitória Apart Hospital", "02.209.094/0001-39");

    Sacado sacado = new Sacado("CLINICA MULHER", "05.940.354/0001-30");

    Endereco endereco = new Endereco();
    endereco.setUF(UnidadeFederativa.ES);
    endereco.setLocalidade("Serra");
    endereco.setCep(new CEP("29180-162"));
    endereco.setBairro("Eurico Salles");
    endereco.setLocalidade("Rua dos Perdizes");
    endereco.setNumero("9");

    sacado.addEndereco(endereco);

    ContaBancaria conta = new ContaBancaria(BancosSuportados.BANCO_ITAU.create());
    conta.setNumeroDaConta(new NumeroDaConta(14019, "6"));
    conta.setCarteira(new Carteira(109));
    conta.setAgencia(new Agencia(9363));

    Titulo titulo = new Titulo(conta, sacado, cedente);
    titulo.setNumeroDoDocumento("123456");
    titulo.setNossoNumero("1");
    titulo.setDigitoDoNossoNumero("5");
    titulo.setValor(BigDecimal.valueOf(0.23));
    titulo.setDataDoDocumento(new Date());
    titulo.setDataDoVencimento(new Date());
    titulo.setTipoDeDocumento(TipoDeTitulo.NF_NOTA_FISCAL);
    titulo.setAceite(Titulo.Aceite.A);
    titulo.setDesconto(new BigDecimal(0.05));
    titulo.setDeducao(BigDecimal.ZERO);
    titulo.setMora(BigDecimal.ZERO);
    titulo.setAcrecimo(BigDecimal.ZERO);
    titulo.setValorCobrado(BigDecimal.ZERO);

    Boleto boleto = new Boleto(titulo);
    boleto.setLocalPagamento("Pagável preferencialmente na Rede X ou em " +
        "qualquer Banco até o Vencimento.");
    boleto.setInstrucaoAoSacado("Senhor sacado, sabemos sim que o valor " +
        "cobrado não é o esperado, aproveite o DESCONTÃO!");
    boleto.setInstrucao1("PARA PAGAMENTO 1 até Hoje não cobrar nada!");
    boleto.setInstrucao2("PARA PAGAMENTO 2 até Amanhã Não cobre!");
    boleto.setInstrucao3("PARA PAGAMENTO 3 até Depois de amanhã, OK, não cobre.");
    boleto.setInstrucao4("PARA PAGAMENTO 4 até 04/xx/xxxx de 4 dias atrás COBRAR O VALOR DE: R$ 01,00");
    boleto.setInstrucao5("PARA PAGAMENTO 5 até 05/xx/xxxx COBRAR O VALOR DE: R$ 02,00");
    boleto.setInstrucao6("PARA PAGAMENTO 6 até 06/xx/xxxx COBRAR O VALOR DE: R$ 03,00");
    boleto.setInstrucao7("PARA PAGAMENTO 7 até xx/xx/xxxx COBRAR O VALOR QUE VOCÊ QUISER!");
    boleto.setInstrucao8("APÓS o Vencimento, Pagável Somente na Rede X.");

    BoletoViewer boletoViewer = new BoletoViewer(boleto);

    ByteArrayOutputStream baos = boletoViewer.getPdfAsStream();

    ByteArrayInputStream arquivoPdf = new ByteArrayInputStream(baos.toByteArray());

    DefaultStreamedContent dsc = new DefaultStreamedContent(arquivoPdf);
    dsc.setContentType("application/pdf");
    dsc.setName(String.format("%s.pdf", "boleto"));

    return dsc;

  }
}
