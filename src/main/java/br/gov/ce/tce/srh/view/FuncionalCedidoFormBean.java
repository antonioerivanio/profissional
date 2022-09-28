package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.CodigoCategoria;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.FuncionalCedido;
import br.gov.ce.tce.srh.domain.PessoaJuridica;

import br.gov.ce.tce.srh.enums.TipodeEmpresa;
import br.gov.ce.tce.srh.service.CodigoCategoriaService;
import br.gov.ce.tce.srh.service.FuncionalCedidoService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.PessoaJuridicaService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("funcionalCedidoFormBean")
@Scope("view")
public class FuncionalCedidoFormBean implements Serializable {

  static Logger logger = Logger.getLogger(FuncionalCedidoFormBean.class);
  @Autowired
  private FuncionalCedidoService funcionalCedidoService;

  @Autowired
  private PessoaJuridicaService pessoaJuridicaService;

  @Autowired
  private FuncionalService funcionalService;

  @Autowired
  private CodigoCategoriaService codigoCateogiraService;

  @Autowired
  private AfastamentoFormBean afastamentoFormBean;

  @Autowired
  private NomeacaoServidorFormBean nomeacaoServidorFormBean;

  @Autowired
  private LoginBean loginBean;

  private FuncionalCedido entidade;
  private PessoaJuridica pessoaJuridica;

  private boolean bloquearDatas = false;
  private boolean isEdicao = false;
  private boolean isExibidoTodosOsCampos = false;

  private List<CodigoCategoria> comboCodCateg;
  private List<PessoaJuridica> comboEmpresasCadastradas;
  private List<Funcional> servidorEnvioList;
  private Funcional servidorFuncional;

  @PostConstruct
  public void init() {
    FuncionalCedido flashParameter = null;

    if (FacesUtil.getFlashParameter("entidade") instanceof FuncionalCedido) {
      flashParameter = (FuncionalCedido) FacesUtil.getFlashParameter("entidade");

      entidade = funcionalCedidoService.getById(flashParameter.getId());

      setServidorFuncional(getEntidade().getFuncional());
    } else {
      inicializarEntidade();
    }

    try {
      if (this.entidade.getId() != null) {
        this.servidorEnvioList = funcionalService.findServidoresEventoAuxilioSaude();
        setCnpjPessoaJuridicaChange();

        this.isEdicao = true;
      }
    } catch (Exception e) {
      FacesUtil.addErroMessage("Ocorreu um erro ao carregar os dados. Operação cancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }
  }


  public void salvar() {
    try {
      if (funcionalCedidoService.isOk(entidade)) {
        entidade.setDataAtualizacao(new Date());
        entidade.setIdUsuarioAtualizacao(loginBean.getUsuarioLogado().getId());
        entidade.setFuncional(getFuncionalByServidorFuncional());
        entidade.setPessoaJuridica(pessoaJuridica);
        entidade.setCodigoCategoria(entidade.getCodigoCategoria().getCodigoCategoraByList(getComboCodCateg()));

        funcionalCedidoService.salvar(entidade);

        if (entidade.getId() != null) {
          FacesUtil.addInfoMessage(FacesUtil.MENSAGEM_ALTERACAO_SUCESSO);
        } else {
          FacesUtil.addInfoMessage(FacesUtil.MENSAGEM_SUCESSO);
        }

      }
    } catch (Exception e) {
      FacesUtil.addErroMessage("Ocorreu um erro ao Salvar os dados. Operação cancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }

    inicializarEntidade();
  }

  private void inicializarEntidade() {
    setEntidade(new FuncionalCedido());
    getEntidade().setFuncional(new Funcional());
    getEntidade().setDtAdmCed(null);
    getEntidade().setMatricOrig(null);
    getEntidade().setMatricOrig(null);
    pessoaJuridica = new PessoaJuridica();
    getEntidade().setPessoaJuridica(pessoaJuridica);
    getEntidade().setCodigoCategoria(null);
    setServidorFuncional(null);

    this.servidorEnvioList = funcionalService.findServidoresEventoAuxilioSaude();
  }

  public void setCnpjPessoaJuridicaChange() {
    int index = getComboEmpresasCadastradas().indexOf(entidade.getPessoaJuridica());
    PessoaJuridica pessoaJuridicaEncontrada = getComboEmpresasCadastradas().get(index);
    setPessoaJuridica(pessoaJuridicaEncontrada);
  }


  public String voltar() {
    return "listar";
  }

  /***
   * Buscar na lista dados do servidor selecionado
   * 
   * @return funcional encontrado
   */
  private Funcional getFuncionalByServidorFuncional() {
    int index = getServidorEnvioList().indexOf(getServidorFuncional());
    Funcional funcionalEncontrado = getServidorEnvioList().get(index);
    return funcionalEncontrado;
  }

  public List<PessoaJuridica> getComboEmpresasCadastradas() {

    try {
      if (this.comboEmpresasCadastradas == null) {
        List<TipodeEmpresa> tipodeEmpresas = new ArrayList<TipodeEmpresa>();
        tipodeEmpresas.add(TipodeEmpresa.PLANOS_SAUDE);
        this.comboEmpresasCadastradas = pessoaJuridicaService.findAllNotTipo(tipodeEmpresas);
      }

    } catch (Exception e) {
      FacesUtil.addInfoMessage("Erro ao carregar o campo tipo de publicação. Operação cancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }

    return this.comboEmpresasCadastradas;
  }

  public List<CodigoCategoria> getComboCodCateg() {
    try {
      this.comboCodCateg = codigoCateogiraService.findAll();
    } catch (Exception e) {
      FacesUtil.addErroMessage("Erro ao carregar o campo Codigo Categoria. Operação cancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }

    return this.comboCodCateg;
  }


  public List<Funcional> getServidorEnvioList() {
    return servidorEnvioList;
  }

  public void setServidorEnvioList(List<Funcional> servidorEnvioList) {
    this.servidorEnvioList = servidorEnvioList;
  }

  public FuncionalCedido getEntidade() {
    return entidade;
  }

  public void setEntidade(FuncionalCedido entidade) {
    this.entidade = entidade;
  }

  public PessoaJuridica getPessoaJuridica() {
    return pessoaJuridica;
  }

  public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
    this.pessoaJuridica = pessoaJuridica;
  }

  public boolean isBloquearDatas() {
    return bloquearDatas;
  }

  public boolean getIsEdicao() {
    return isEdicao;
  }

  public void setEdicao(boolean isEdicao) {
    this.isEdicao = isEdicao;
  }


  public boolean getIsExibidoTodosOsCampos() {
    return isExibidoTodosOsCampos;
  }

  public AfastamentoFormBean getAfastamentoFormBean() {
    return afastamentoFormBean;
  }

  public NomeacaoServidorFormBean getNomeacaoServidorFormBean() {
    return nomeacaoServidorFormBean;
  }


  public Funcional getServidorFuncional() {
    return servidorFuncional;
  }


  public void setServidorFuncional(Funcional servidorFuncional) {
    this.servidorFuncional = servidorFuncional;
  }
  
  

}