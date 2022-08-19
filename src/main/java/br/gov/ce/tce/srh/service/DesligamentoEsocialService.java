package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.gov.ce.tce.srh.dao.DesligamentoEsocialDAO;
import br.gov.ce.tce.srh.domain.Desligamento;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;

@Service("desligamentoESocialService")
public class DesligamentoEsocialService {

  @Autowired
  private DesligamentoEsocialDAO dao;

  @Autowired
  private EventoService eventoService;

  @Autowired
  private NotificacaoService notificacaoService;

  @Transactional
  public Desligamento salvar(Desligamento entidade) {


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

  public Desligamento getByIdFuncional(Long idFuncional) {
    return dao.getByIdFuncional(idFuncional);
  }

  public Desligamento getById(Long id) {
    return dao.getById(id);
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

}
