package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.gov.ce.tce.srh.dao.FechamentoFolhaEsocialDAO;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.FechamentoFolhaEsocial;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.enums.DadoTCE;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;
import br.gov.ce.tce.srh.util.SRHUtils;

@Service("fechamentoFolhaEsocialService")
public class FechamentoFolhaEsocialService {

  @Autowired
  private FechamentoFolhaEsocialDAO dao;

  @Autowired
  private EventoService eventoService;

  @Autowired
  private NotificacaoService notificacaoService;


  @Transactional
  public FechamentoFolhaEsocial salvar(FechamentoFolhaEsocial entidade) {
    validarCampoAntesSalvar(entidade);
    String referencia = entidade.getReferenciaMesAnoTransient();
    entidade = dao.salvar(entidade);

    if (entidade.getReferencia() == null) {
      referencia = entidade.getId() + "-" + referencia;
      entidade.setReferencia(referencia);
    }

    // salvando notificação
    Evento evento = this.eventoService.getById(TipoEventoESocial.S1299.getCodigo());
    Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipoAndReferencia(evento.getId(), entidade.getReferencia());
    if (notificacao == null) {
      notificacao = new Notificacao();
      notificacao.setDescricao("Evento S1299 com pendência de envio.");
      notificacao.setData(new Date());
      notificacao.setTipo(TipoNotificacao.N);
      notificacao.setEvento(evento);
      notificacao.setReferencia(entidade.getReferencia());
    } else {
      notificacao.setData(new Date());
    }

    this.notificacaoService.salvar(notificacao);

    return entidade;
  }

  public void validarCampoAntesSalvar(FechamentoFolhaEsocial bean) {
    String periodoApuracao = getPeriodoApuracaoStr(bean);
    bean.setPeriodoApuracao(periodoApuracao);
    String referenciaMesAno = null;

    if (bean.getIndicativoApuracao() == 1) {
      referenciaMesAno = bean.getMesReferencia() + bean.getAnoReferencia();
    } else {
      referenciaMesAno = bean.getAnoReferencia();
    }

    bean.setReferenciaMesAnoTransient(referenciaMesAno);
  }

  @Transactional
  public void excluir(FechamentoFolhaEsocial entidade) {
    dao.excluir(entidade);
  }

  public FechamentoFolhaEsocial getById(Long id) {
    return dao.getById(id);
  }

  public int count(String periodo) {
    return dao.count(periodo);
  }

  public List<FechamentoFolhaEsocial> search(String periodo, Integer first, Integer rows) {
    return dao.search(periodo, first, rows);
  }

  public FechamentoFolhaEsocial getEventoS1299ByServidor(Funcional servidorFuncional) {
    FechamentoFolhaEsocial fechamentoEventoEsocial = dao.getEventoS1299ByServidor(servidorFuncional);
    return fechamentoEventoEsocial;
  }

  public FechamentoFolhaEsocial getIncializarEventoS1299ByServidor() {
    final char evtRemuneracao = 'S'; // SIM
    final char evtComercializacaoProduto = 'N'; // NÃO
    final char evtContratoAvulsoNaoPortuario = 'N'; // NÃO
    final char evtInfoComplementarPrevidenciaria = 'N'; // NÃO
    final char evtTransmissaoImediata = 'N'; // NÃO
    final char naoValidacao = 'N'; // NÃO
    final Integer tipoInscricaoEmpregador = 1; // CNPJ
    final String periodoApuracao = "11" + "/" + SRHUtils.getAnoCorrente();
    final Integer indicativoApuracao = 1;// 1 - Mensal

    return new FechamentoFolhaEsocial(tipoInscricaoEmpregador, DadoTCE.NR_INSC, evtRemuneracao, evtComercializacaoProduto, evtContratoAvulsoNaoPortuario, evtInfoComplementarPrevidenciaria,
                              evtTransmissaoImediata, naoValidacao, periodoApuracao, indicativoApuracao);

  }

  public String getPeriodoApuracaoStr(FechamentoFolhaEsocial bean) {
    String periodoApuracaoStr = "";
    if (bean.getIndicativoApuracao() == 1) {// MENSAL
      if (bean.getAnoReferencia() == null) {
        throw new NullPointerException("Ops!. O ano de referencia é obrigatório!");
      }

      if (bean.getMesReferencia() == null) {
        throw new NullPointerException("Ops!. O mês de referencia é obrigatório!");
      }

      if (bean.getMesReferencia().equals("13")) {
        periodoApuracaoStr = bean.getAnoReferencia();
      } else {
        periodoApuracaoStr = bean.getMesReferencia() + "-" + bean.getAnoReferencia();
      }
    } else {// ANUAL
      if (bean.getAnoReferencia() == null) {
        throw new NullPointerException("Ops!. O ano de referencia é obrigatório!");
      }

      periodoApuracaoStr = bean.getAnoReferencia();
    }

    return periodoApuracaoStr;
  }


}
