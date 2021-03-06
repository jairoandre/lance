package br.com.vah.lance.constant;

/**
 * Created by jairoportela on 26/08/2016.
 */
public enum EntradasRejeitadasEnum {
  _03("03","AG. COBRADORA CEP SEM ATENDIMENTO DE PROTESTO NO MOMENTO"),
  _04("04","ESTADO SIGLA DO ESTADO INVÁLIDA 05 DATA VENCIMENTO PRAZO DA OPERAÇÃO MENOR QUE PRAZO MÍNIMO OU MAIOR QUE O MÁXIMO"),
  _07("07","VALOR DO TÍTULO VALOR DO TÍTULO MAIOR QUE 10.000.000,00 08 NOME DO PAGADOR NÃO INFORMADO OU DESLOCADO"),
  _09("09","AGENCIA/CONTA AGÊNCIA ENCERRADA"),
  _10("10","LOGRADOURO NÃO INFORMADO OU DESLOCADO"),
  _11("11","CEP CEP NÃO NUMÉRICO OU CEP INVÁLIDO"),
  _12("12","SACADOR / AVALISTA NOME NÃO INFORMADO OU DESLOCADO (BANCOS CORRESPONDENTES)"),
  _13("13","ESTADO/CEP CEP INCOMPATÍVEL COM A SIGLA DO ESTADO"),
  _14("14","NOSSO NÚMERO NOSSO NÚMERO JÁ REGISTRADO NO CADASTRO DO BANCO OU FORA DA FAIXA"),
  _15("15","NOSSO NÚMERO NOSSO NÚMERO EM DUPLICIDADE NO MESMO MOVIMENTO"),
  _18("18","DATA DE ENTRADA DATA DE ENTRADA INVÁLIDA PARA OPERAR COM ESTA CARTEIRA"),
  _19("19","OCORRÊNCIA OCORRÊNCIA INVÁLIDA"),
  _21("21","AG. COBRADORA CARTEIRA NÃO ACEITA DEPOSITÁRIA CORRESPONDENTE ESTADO DA AGÊNCIA DIFERENTE DO ESTADO DO PAGADOR AG. COBRADORA NÃO CONSTA NO CADASTRO OU ENCERRANDO"),
  _22("22","CARTEIRA CARTEIRA NÃO PERMITIDA (NECESSÁRIO CADASTRAR FAIXA LIVRE)"),
  _26("26","AGÊNCIA/CONTA AGÊNCIA/CONTA NÃO LIBERADA PARA OPERAR COM COBRANÇA"),
  _27("27","CNPJ INAPTO CNPJ DO BENEFICIÁRIO INAPTO DEVOLUÇÃO DE TÍTULO EM GARANTIA"),
  _29("29","CÓDIGO EMPRESA CATEGORIA DA CONTA INVÁLIDA"),
  _30("30","ENTRADA BLOQUEADA ENTRADAS BLOQUEADAS, CONTA SUSPENSA EM COBRANÇA"),
  _31("31","AGÊNCIA/CONTA CONTA NÃO TEM PERMISSÃO PARA PROTESTAR (CONTATE SEU GERENTE"),
  _35("35","VALOR DO IOF IOF MAIOR QUE 5% 36 QTDADE DE MOEDA QUANTIDADE DE MOEDA INCOMPATÍVEL COM VALOR DO TÍTULO"),
  _37("37","CNPJ/CPF DO PAGADOR NÃO NUMÉRICO OU IGUAL A ZEROS"),
  _42("42","NOSSO NÚMERO NOSSO NÚMERO FORA DE FAIXA"),
  _52("52","AG. COBRADORA EMPRESA NÃO ACEITA BANCO CORRESPONDENTE"),
  _53("53","AG. COBRADORA EMPRESA NÃO ACEITA BANCO CORRESPONDENTE - COBRANÇA MENSAGEM"),
  _54("54","DATA DE VENCTO BANCO CORRESPONDENTE - TÍTULO COM VENCIMENTO INFERIOR A 15 DIAS"),
  _55("55","DEP/BCO CORRESP CEP NÃO PERTENCE À DEPOSITÁRIA INFORMADA"),
  _56("56","DT VENCTO/BCO CORRESP VENCTO SUPERIOR A 180 DIAS DA DATA DE ENTRADA"),
  _57("57","DATA DE VENCTO CEP SÓ DEPOSITÁRIA BCO DO BRASIL COM VENCTO INFERIOR A 8 DIAS"),
  _60("60","ABATIMENTO VALOR DO ABATIMENTO INVÁLIDO"),
  _61("61","JUROS DE MORA JUROS DE MORA MAIOR QUE O PERMITIDO"),
  _62("62","DESCONTO VALOR DO DESCONTO MAIOR QUE VALOR DO TÍTULO"),
  _63("63","DESCONTO DE ANTECIPAÇÃO VALOR DA IMPORTÂNCIA POR DIA DE DESCONTO (IDD) NÃO PERMITIDO"),
  _64("64","DATA DE EMISSÃO DATA DE EMISSÃO DO TÍTULO INVÁLIDA 65 TAXA FINANCTO TAXA INVÁLIDA (VENDOR) "),
  _66("66","DATA DE VENCTO INVALIDA/FORA DE PRAZO DE OPERAÇÃO (MÍNIMO OU MÁXIMO)"),
  _67("67","VALOR/QTIDADE VALOR DO TÍTULO/QUANTIDADE DE MOEDA INVÁLIDO"),
  _68("68","CARTEIRA CARTEIRA INVÁLIDA OU NÃO CADASTRADA NO INTERCÂMBIO DA COBRANÇA"),
  _69("69","CARTEIRA CARTEIRA INVÁLIDA PARA TÍTULOS COM RATEIO DE CRÉDITO"),
  _70("70","BENEFICIÁRIO NÃO CADASTRADO PARA FAZER RATEIO DE CRÉDITO"),
  _78("78","AGÊNCIA/CONTA DUPLICIDADE DE AGÊNCIA/CONTA BENEFICIÁRIA DO RATEIO DE CRÉDITO"),
  _80("80","QUANTIDADE DE CONTAS BENEFICIÁRIAS DO RATEIO MAIOR DO QUE O PERMITIDO (MÁXIMO DE 30 CONTAS POR TÍTULO)"),
  _81("81","AGÊNCIA/CONTA CONTA PARA RATEIO DE CRÉDITO INVÁLIDA / NÃO PERTENCE AO ITAÚ"),
  _82("82","DESCONTO/ABATIMENTO NÃO PERMITIDO PARA TÍTULOS COM RATEIO DE CRÉDITO 83 VALOR DO TÍTULO VALOR DO TÍTULO MENOR QUE A SOMA DOS VALORES ESTIPULADOS PARA RATEIO"),
  _84("84","AGÊNCIA/CONTA BENEFICIÁRIA DO RATEIO É A CENTRALIZADORA DE CRÉDITO DO BENEFICIÁRIO"),
  _85("85","AGÊNCIA/CONTA AGÊNCIA/CONTA DO BENEFICIÁRIO É CONTRATUAL / RATEIO DE CRÉDITO NÃO PERMITIDO"),
  _86("86","CÓDIGO DO TIPO DE VALOR INVÁLIDO / NÃO PREVISTO PARA TÍTULOS COM RATEIO DE CRÉDITO"),
  _87("87","AGÊNCIA/CONTA REGISTRO TIPO 4 SEM INFORMAÇÃO DE AGÊNCIAS/CONTAS BENEFICIÁRIAS DO RATEIO"),
  _90("90","COBRANÇA MENSAGEM - NÚMERO DA LINHA DA MENSAGEM INVÁLIDO OU QUANTIDADE DE LINHAS EXCEDIDAS"),
  _97("97","SEM MENSAGEM COBRANÇA MENSAGEM SEM MENSAGEM (SÓ DE CAMPOS FIXOS), PORÉM COM REGISTRO DO TIPO 7 OU 8"),
  _98("98","REGISTRO MENSAGEM SEM FLASH CADASTRADO OU FLASH INFORMADO DIFERENTE DO CADASTRADO"),
  _99("99","INVÁLIDO CONTA DE COBRANÇA COM FLASH CADASTRADO E SEM REGISTRO DE MENSAGEM CORRESPONDENTE");

  private String codigo;
  private String label;

  EntradasRejeitadasEnum(String codigo, String label) {
    this.codigo = codigo;
    this.label = label;
  }

  public String getCodigo() {
    return codigo;
  }

  public String getLabel() {
    return label;
  }

  public static EntradasRejeitadasEnum getByCodigo(String codigo) {
    for (EntradasRejeitadasEnum val : EntradasRejeitadasEnum.values()) {
      if(val.codigo.equals(codigo)) {
        return val;
      }
    }
    return null;
  }
}
