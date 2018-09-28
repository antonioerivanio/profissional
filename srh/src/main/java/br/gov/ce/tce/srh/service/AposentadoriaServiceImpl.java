package br.gov.ce.tce.srh.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.AposentadoriaDAO;
import br.gov.ce.tce.srh.domain.AbonoPermanencia;
import br.gov.ce.tce.srh.domain.Aposentadoria;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.FuncionalSetor;
import br.gov.ce.tce.srh.enums.EnumStatusFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.util.SRHUtils;

@Service("aposentadoriaService")
public class AposentadoriaServiceImpl implements AposentadoriaService {

	@Autowired
	private AposentadoriaDAO dao;

	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private FolhaService folhaService;
	
	@Autowired
	private FuncionalSetorService funcionalSetorService;
	
	@Autowired
	private SetorService setorService;
	
	@Autowired
	private AbonoPermanenciaService abonoPermanenciaService;
	
	@Autowired
	private RepresentacaoFuncionalService representacaoFuncionalService;
	
		
	@Override
	@Transactional
	public Aposentadoria salvar(Aposentadoria entidade) throws SRHRuntimeException {
		
		validaDados(entidade);
		
		verificaAposentadoriaCadastrada(entidade);		
		
		entidade = dao.salvar(entidade);		
		
		atualizaOutrasEntidades(entidade, false);
		
	    
		return entidade;
	}
	
	
	@Override
	@Transactional
	public void excluir(Aposentadoria entidade) {
		
		atualizaOutrasEntidades(entidade, true);		
		
		dao.excluir(entidade);		
	}
	

	@Override
	public int count() {
		return dao.count();
	}
	

	@Override
	public List<Aposentadoria> search(Long idPessoal, int first, int rows) {
		return dao.search(idPessoal,first, rows);
	}
	

	private void validaDados(Aposentadoria entidade) throws SRHRuntimeException {
		
		if (entidade.getFuncional() == null)
			 throw new SRHRuntimeException("O Funcionário é obrigatório. Digite o nome e efetue a pesquisa.");
		
		verificaTipoOcupacao(entidade.getFuncional());

		if (entidade.getTipoBeneficio() == null)
			throw new SRHRuntimeException("O Tipo Benefício é obrigatório.");
		
		if (entidade.getDataAto() == null)
			throw new SRHRuntimeException("A Data Ato é obrigatória.");
		// Exige que a dataAto seja posterior a de exercício, exceto para Servidores provenientes do TCM
		else if (!entidade.getDataAto().after(entidade.getFuncional().getExercicio())
				&& !entidade.getFuncional().getTipoMovimentoEntrada().getId().equals(43L))
			throw new SRHRuntimeException("A Data Ato deve ser posterior a data de Exercício Funcional.");
		
		if (entidade.getNumeroResolucao() == null || entidade.getNumeroResolucao().intValue() <= 0)
			entidade.setNumeroResolucao(null);
		
		if (entidade.getAnoResolucao() == null || entidade.getAnoResolucao().intValue() <= 0)
			entidade.setAnoResolucao(null);		
		
		if (entidade.getDataUltimaContagem() == null)
			throw new SRHRuntimeException("A Data Última Contagem é obrigatória.");
		else if ( ! ( entidade.getDataUltimaContagem().after(entidade.getDataAto())
					 || entidade.getDataUltimaContagem().equals(entidade.getDataAto()) ) )
			throw new SRHRuntimeException("A Data Última Contagem deve ser maior ou igual a Data do Ato.");
		
		if (entidade.getDataInicioBeneficio() == null)
			throw new SRHRuntimeException("A Data Início Benefício é obrigatória.");
		else if (!entidade.getDataInicioBeneficio().after(entidade.getDataUltimaContagem()))
			throw new SRHRuntimeException("A Data Início Benefício deve ser posterior a Data Última Contagem.");

		if (entidade.getTipoPublicacao() == null)
			throw new SRHRuntimeException("O Tipo Publicação é obrigatório.");
		
		if (entidade.getDataPublicacao() == null)
			throw new SRHRuntimeException("A Data Publicação é obrigatória.");
		else if (entidade.getDataPublicacao().before(entidade.getDataAto()))
			throw new SRHRuntimeException("A Data Publicação não pode ser anterior a Data Ato.");
		
	}	
	
	
	@SuppressWarnings("unused")
	private boolean anoAnterior(Date data, Long ano) {
		
		Calendar dataCalendar = new GregorianCalendar();
		dataCalendar.setTime(data);
		
		
		return ano < dataCalendar.get(Calendar.YEAR);		
	}
	
	
	private void verificaTipoOcupacao(Funcional funcional) {
		
		Long idTipoOcupacao = funcional.getOcupacao().getTipoOcupacao().getId();
		
		if (idTipoOcupacao > 3)
			throw new SRHRuntimeException("Apenas Servidores com ocupação do tipo Membro, Efetivo ou Funcionário Estabilizado podem ser aposentados.");
	}
	
	
	private void atualizaOutrasEntidades(Aposentadoria entidade, boolean excluirAposentadoria) {

		Funcional funcional = entidade.getFuncional();
		
		if (excluirAposentadoria) {
			
			funcional.setStatus(EnumStatusFuncional.ATIVO.getId());
			
			if (funcional.getOcupacao().getTipoOcupacao().getId() != 1L) // Diferente de membros
				funcional.setPonto(true);
			
			reverterAlteracaoAbono(entidade, funcional);					
			
			reverterAlteracaoFolha(entidade, funcional);
			
			reverterAlteracaoLotacao(funcional);
			
			funcional.setAposentadoria(null);
		
		} else {
			
			funcional.setStatus(EnumStatusFuncional.INATIVO.getId());
			
			funcional.setPonto(false);
			
			funcional.setAbonoPrevidenciario(false);			
			
			atualizaFolha(funcional);
									
			atualizaLotacao(entidade, funcional);			
			
			funcional.setAposentadoria(entidade);			
			
		}		
		
		funcionalService.salvar(funcional);
	}


	private void atualizaLotacao(Aposentadoria entidade, Funcional funcional) {
		
		FuncionalSetor lotacao = funcionalSetorService.getAtivoByFuncional(funcional.getId());			
		
		if(lotacao != null) {
			
			Date dataFim = SRHUtils.adiconarSubtrairDiasDeUmaData(entidade.getDataInicioBeneficio(), -1);
			
			if (dataFim.before(lotacao.getDataInicio()))
				throw new SRHRuntimeException("A Data Início Benefício deve ser posterior a Data Início da lotação ativa do servidor.");
			
			lotacao.setDataFim(dataFim);
			funcionalSetorService.salvar(lotacao);
		}		
		
		funcional.setSetor(setorService.getById(113L));
	}

	private void reverterAlteracaoLotacao(Funcional funcional) {
		
		List<FuncionalSetor> lotacoes = funcionalSetorService.findByPessoal(funcional.getPessoal().getId());
		if (lotacoes != null && lotacoes.size() > 0) {
			
			FuncionalSetor ultimaLotacao = lotacoes.get(0);
			ultimaLotacao.setDataFim(null);
			funcionalSetorService.salvar(ultimaLotacao);
			
			funcional.setSetor(ultimaLotacao.getSetor());
			
		}
	}
	
	
	private void atualizaFolha(Funcional funcional) {
		
		if (funcional.getOcupacao().getTipoOcupacao().getId() == 1L) // Membros
			funcional.setFolha(folhaService.getByCodigo("06"));	// Conselheiros Aposentados
		else 
			funcional.setFolha(folhaService.getByCodigo("07")); // Servidores Aposentados
	}
	
	private void reverterAlteracaoFolha(Aposentadoria entidade, Funcional funcional) {
		if (funcional.getOcupacao().getTipoOcupacao().getId() == 1L) { // Membros
			
			funcional.setFolha(folhaService.getByCodigo("01"));	// Conselheiros Ativos
		
		} else { 
			
			if (representacaoFuncionalService.temAtivaByPessoal(entidade.getFuncional().getPessoal().getId()))
				funcional.setFolha(folhaService.getByCodigo("02")); // Chefes(cargo comissionado)
			else
				funcional.setFolha(folhaService.getByCodigo("03")); // Pessoal Ativo
		
		}
	}


	private void reverterAlteracaoAbono(Aposentadoria entidade, Funcional funcional) {
		AbonoPermanencia abono = abonoPermanenciaService.getByPessoalId(entidade.getFuncional().getPessoal().getId());
		if (abono != null && abono.getFuncional().getId().intValue() == entidade.getFuncional().getId().intValue())
			funcional.setAbonoPrevidenciario(true);
	}
	
	
	private void verificaAposentadoriaCadastrada(Aposentadoria entidade) {
		
		if ( entidade.getId() == null || entidade.getId() == 0 ) {
		
			List<Aposentadoria> aposentadoria = dao.search(entidade.getFuncional().getPessoal().getId(), 0, 1);
		
			if (aposentadoria.size() == 1)
				throw new SRHRuntimeException("O Servidor já possui aposentadoria cadastrada.");
		}
	}
	
}
