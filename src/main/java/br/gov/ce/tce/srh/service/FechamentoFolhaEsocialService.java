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
    String periodoApuracao = getPeriodoApuracaoStr(entidade.getAnoReferencia(), entidade.getMesReferencia());
    entidade.setPeridoApuracao(periodoApuracao);
    String referenciaMesAno = entidade.getMesReferencia() + entidade.getAnoReferencia();
    
    entidade = dao.salvar(entidade);
    
      if(entidade.getReferencia() == null) {
        String referencia = entidade.getId()  +  "-" + referenciaMesAno;                                  
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

  @Transactional
  public void excluir(FechamentoFolhaEsocial entidade) {
    dao.excluir(entidade);
  }

  public FechamentoFolhaEsocial getById(Long id) {
    return dao.getById(id);
  }

  public int count(String nome, String cpf) {
    return dao.count(nome, cpf);
  }

  public List<FechamentoFolhaEsocial> search(String nome, String cpf, Integer first, Integer rows) {
    return dao.search(nome, cpf, first, rows);
  }

  public FechamentoFolhaEsocial getEventoS1299ByServidor(Funcional servidorFuncional) {
    FechamentoFolhaEsocial fechamentoEventoEsocial = dao.getEventoS1299ByServidor(servidorFuncional);
    return fechamentoEventoEsocial;
  }

  public FechamentoFolhaEsocial getIncializarEventoS1299ByServidor() {
   final Integer evtRemuneracao = 1; //SIM
   final Integer evtComercializacaoProduto=0; //NÃO
   final Integer evtContratoAvulsoNaoPortuario = 0; //NÃO
   final Integer evtInfoComplementarPrevidenciaria = 0; //NÃO
   final Integer evtTransmissaoImediata = 0; //NÃO
   final Integer naoValidacao = 0; //NÃO  
   final Integer tipoInscricaoEmpregador = 1; //CNPJ
   final String periodoApuracao = "11" + "/" + SRHUtils.getAnoCorrente();
    
   return new FechamentoFolhaEsocial(tipoInscricaoEmpregador, DadoTCE.NR_INSC, evtRemuneracao, 
                              evtComercializacaoProduto, evtContratoAvulsoNaoPortuario, evtInfoComplementarPrevidenciaria, evtTransmissaoImediata, naoValidacao, periodoApuracao); 

  }  
  
  private String getPeriodoApuracaoStr(String anoReferencia, String mesReferencia) {
    String periodoApuracaoStr = "";
    
    if(anoReferencia == null) {
      throw new NullPointerException("Ops!. O ano de referencia é obrigatório!");
    }
    
    if(mesReferencia == null) {
      throw new NullPointerException("Ops!. O mês de referencia é obrigatório!");
    }
  
    
    if(mesReferencia.equals("13")) {
        periodoApuracaoStr = anoReferencia;
    }
    else {
        periodoApuracaoStr = mesReferencia + "-" + anoReferencia;
    }
    return periodoApuracaoStr;
}


}
