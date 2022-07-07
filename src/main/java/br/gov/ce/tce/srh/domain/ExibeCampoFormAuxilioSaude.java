package br.gov.ce.tce.srh.domain;

public class ExibeCampoFormAuxilioSaude {
  private boolean exibirCamposInputFormBeneficiario;
  private boolean exibirCamposInputFormDependente;


  public boolean isExibirCamposInputFormBeneficiario() {
    return exibirCamposInputFormBeneficiario;
  }

  public void setExibirCamposInputFormBeneficiario(boolean exibirCamposInputFormBeneficiario) {
    this.exibirCamposInputFormBeneficiario = exibirCamposInputFormBeneficiario;
  }

  public boolean isExibirCamposInputFormDependente() {
    return exibirCamposInputFormDependente;
  }

  public void setExibirCamposInputFormDependente(boolean exibirCamposInputFormDependente) {
    this.exibirCamposInputFormDependente = exibirCamposInputFormDependente;
  }
}
