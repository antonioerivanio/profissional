
package br.gov.ce.tce.srh.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.steadystate.css.parser.ParseException;
import br.gov.ce.tce.srh.dao.DesligamentoEsocialDAO;
import br.gov.ce.tce.srh.domain.Desligamento;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.enums.CodigoCategoria;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoMotivoDesligamento;
import br.gov.ce.tce.srh.enums.TipoNotificacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
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
    boolean resultado = Boolean.TRUE;
    
    if (bean.getMtvDesligamento() == null || bean.getMtvDesligamento().isEmpty()) {
      throw new NullPointerException("Campo motivo desligamento é obrigatório");
    }
    
    resultado  = isCategoriaValida(bean.getMtvDesligamento(), bean.getCodigoCategoria().toString());        
    
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

    return  resultado;
  }
  
  private Boolean isCategoriaValida(String codMotivoDesligamento, String codigoCategoria) {
    if(Integer.valueOf(codMotivoDesligamento).equals(TipoMotivoDesligamento.APOSENTADORIA_EXCERTO_POR_INVALIDEZ.getCodigo())) { 
      //Aposentadoria, exceto por invalidez                               
      ArrayList<CodigoCategoria> codigosCategoiraValidoParaDesligamento38 = new ArrayList<CodigoCategoria>();
      codigosCategoiraValidoParaDesligamento38.add(CodigoCategoria.CATEG101);
      codigosCategoiraValidoParaDesligamento38.add(CodigoCategoria.CATEG301);
      codigosCategoiraValidoParaDesligamento38.add(CodigoCategoria.CATEG302);
      codigosCategoiraValidoParaDesligamento38.add(CodigoCategoria.CATEG303);
      codigosCategoiraValidoParaDesligamento38.add(CodigoCategoria.CATEG306);
      codigosCategoiraValidoParaDesligamento38.add(CodigoCategoria.CATEG307);
      codigosCategoiraValidoParaDesligamento38.add(CodigoCategoria.CATEG309);
      codigosCategoiraValidoParaDesligamento38.add(CodigoCategoria.CATEG310);
      codigosCategoiraValidoParaDesligamento38.add(CodigoCategoria.CATEG312);
      
      if(!codigosCategoiraValidoParaDesligamento38.contains(CodigoCategoria.getByCodigo(codigoCategoria))) {
        throw new SRHRuntimeException("Ops!, Os codigos da categorias permitido para o motivo " + TipoMotivoDesligamento.APOSENTADORIA_EXCERTO_POR_INVALIDEZ.getDescricao() + " é [101, 301, 302, 303, 306, 307, 309, 310, 312]");
      }
    } else if(Integer.valueOf(codMotivoDesligamento).equals(TipoMotivoDesligamento.APOSENTADORIA_SERVIDOR_ESTATUTARIO_POR_INVALIDEZ.getCodigo())){
      //Aposentadoria de servidor estatutário, por invalidez
      ArrayList<CodigoCategoria> codigosCategoiraValidoParaDesligamento39 = new ArrayList<CodigoCategoria>();    
      codigosCategoiraValidoParaDesligamento39.add(CodigoCategoria.CATEG301);
      codigosCategoiraValidoParaDesligamento39.add(CodigoCategoria.CATEG306);    
      codigosCategoiraValidoParaDesligamento39.add(CodigoCategoria.CATEG309);
      
      if(!codigosCategoiraValidoParaDesligamento39.contains(CodigoCategoria.getByCodigo(codigoCategoria))) {
        throw new SRHRuntimeException("Ops!, Os codigos da categorias permitido para o motivo " + TipoMotivoDesligamento.APOSENTADORIA_SERVIDOR_ESTATUTARIO_POR_INVALIDEZ.getDescricao() + " é [301, 306, 309]");
      }      
    }else if(Integer.valueOf(codMotivoDesligamento).equals(TipoMotivoDesligamento.EXONERACAO.getCodigo())){
      //exoneracao 
      ArrayList<CodigoCategoria> codigosCategoiraValidoParaDesligamento23 = new ArrayList<CodigoCategoria>();
      codigosCategoiraValidoParaDesligamento23.add(CodigoCategoria.CATEG301);
      codigosCategoiraValidoParaDesligamento23.add(CodigoCategoria.CATEG302);
      codigosCategoiraValidoParaDesligamento23.add(CodigoCategoria.CATEG303);
      codigosCategoiraValidoParaDesligamento23.add(CodigoCategoria.CATEG306);
      codigosCategoiraValidoParaDesligamento23.add(CodigoCategoria.CATEG307);
      codigosCategoiraValidoParaDesligamento23.add(CodigoCategoria.CATEG309);
      codigosCategoiraValidoParaDesligamento23.add(CodigoCategoria.CATEG310);
      codigosCategoiraValidoParaDesligamento23.add(CodigoCategoria.CATEG312);
      
      if(!codigosCategoiraValidoParaDesligamento23.contains(CodigoCategoria.getByCodigo(codigoCategoria))) {       
          throw new SRHRuntimeException("Ops!, Os codigos da categorias permitido para o motivo " + TipoMotivoDesligamento.EXONERACAO.getDescricao() + " é [301, 302, 303, 306, 307, 309, 310, 312]" );        
      }     
    }
    
    return Boolean.TRUE;
  }
  

  public Funcional getFuncionalById(Long idFuncional) {
    return dao.getFuncionalById(idFuncional);
  }  

}
