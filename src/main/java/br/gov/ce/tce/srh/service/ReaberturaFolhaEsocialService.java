package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.gov.ce.tce.srh.dao.ReaberturaFolhaEsocialDAO;
import br.gov.ce.tce.srh.dao.ReaberturaFolhaEsocialDAO;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.ReaberturaFolhaEsocial;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.enums.DadoTCE;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;
import br.gov.ce.tce.srh.util.SRHUtils;

@Service("reaberturaFolhaEsocialService")
public class ReaberturaFolhaEsocialService {

  @Autowired
  private ReaberturaFolhaEsocialDAO dao;

  @Autowired
  private EventoService eventoService;

  @Autowired
  private NotificacaoService notificacaoService;


  @Transactional
  public ReaberturaFolhaEsocial salvar(ReaberturaFolhaEsocial entidade) {
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

  public void validarCampoAntesSalvar(ReaberturaFolhaEsocial bean) {
    String periodoApuracao = getPeriodoApuracaoStr(bean);
    bean.setPeriodoApuracao(periodoApuracao);
    String referenciaAnoMes = null;

    if (bean.getIndicativoApuracao() == 1) {
      referenciaAnoMes =  bean.getAnoReferencia() + bean.getMesReferencia();
    } else {
      referenciaAnoMes = bean.getAnoReferencia();
    }

    bean.setReferenciaMesAnoTransient(referenciaAnoMes);
  }

  @Transactional
  public void excluir(ReaberturaFolhaEsocial entidade) {
    dao.excluir(entidade);
  }

  public ReaberturaFolhaEsocial getById(Long id) {
    return dao.getById(id);
  }

  public int count(String periodo) {
    return dao.count(periodo);
  }

  public List<ReaberturaFolhaEsocial> search(String periodo, Integer first, Integer rows) {
    return dao.search(periodo, first, rows);
  }

  public ReaberturaFolhaEsocial getIncializarEventoS1299ByServidor() {
    final String NOVEMBRO = "11";
    final Integer tipoInscricaoEmpregador = 1; // CNPJ
    final String periodoApuracao = NOVEMBRO + "/" + SRHUtils.getAnoCorrente();
    final Integer indicativoApuracao = 1;// 1 - Mensal

    return new ReaberturaFolhaEsocial(tipoInscricaoEmpregador, DadoTCE.NR_INSC, periodoApuracao, indicativoApuracao);
  }

  public String getPeriodoApuracaoStr(ReaberturaFolhaEsocial bean) {
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
        periodoApuracaoStr = bean.getAnoReferencia() + "-" + bean.getMesReferencia()  ;
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
