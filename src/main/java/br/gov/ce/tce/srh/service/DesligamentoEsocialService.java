
package br.gov.ce.tce.srh.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.steadystate.css.parser.ParseException;
import br.gov.ce.tce.srh.dao.DesligamentoEsocialDAO;
import br.gov.ce.tce.srh.domain.Desligamento;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;
import br.gov.ce.tce.srh.util.SRHUtils;

@Service("desligamentoESocialService")
public class DesligamentoEsocialService {

  @Autowired
  private DesligamentoEsocialDAO dao;

  @Autowired
  private EventoService eventoService;

  @Autowired
  private NotificacaoService notificacaoService;

  @Transactional
  public Desligamento salvar(Desligamento entidade) throws SQLIntegrityConstraintViolationException {
    entidade = dao.salvar(entidade);


    // salvando notificação

    Evento evento = this.eventoService.getById(TipoEventoESocial.S2299.getCodigo());
    Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipoAndReferencia(evento.getId(), entidade.getReferencia());
    if (notificacao == null) {
      notificacao = new Notificacao();
      notificacao.setDescricao("Evento S2299 com pendência de envio.");
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

  @Transactional
  public void excluir(Desligamento entidade) {
    dao.excluir(entidade);
  }

  public Desligamento getDesligamentoById(Long idFuncional) {
    return dao.getDesligamentoById(idFuncional);
  }

  public int count(String nome, String cpf) {
    return dao.count(nome, cpf);
  }

  public List<Desligamento> search(String nome, String cpf, Integer first, Integer rows) {
    return dao.search(nome, cpf, first, rows);
  }

  public Desligamento getEventoS2299ByServidor(Funcional servidorFuncional) {
    return dao.getEventoS2299yServidor(servidorFuncional);
  }

  final static String DATE_FORMAT = "dd-MM-yyyy";

  /***
   * valida campos obrigatórios
   */
  public boolean isOk(Desligamento bean) {

    if (bean.getDtDesligamento() == null) {
      throw new NullPointerException("Campo data desligamento é obrigatório");
    } else {
      // adicionar data a referente, caso ainda não tenha
      String[] referenciaParte = bean.getReferencia().split("-");
      final int UM = 1;
      if (referenciaParte.length == UM) {
        String referencia = bean.getFuncional().getId() + "-" + SRHUtils.formataData(SRHUtils.FORMATO_DATA_SEM_BARRAS, bean.getDtDesligamento());
        bean.setReferencia(referencia);
      }
    }

    if (bean.getMtvDesligamento() == null) {
      throw new NullPointerException("Campo motivo desligamento é obrigatório");
    }

    return Boolean.TRUE;
  }
}
