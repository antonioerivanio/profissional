package br.gov.ce.tce.srh.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.AposentadoriaDAO;
import br.gov.ce.tce.srh.domain.Aposentadoria;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.enums.EnumStatusFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("aposentadoriaService")
public class AposentadoriaServiceImpl implements AposentadoriaService {

	@Autowired
	private AposentadoriaDAO dao;

	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private FolhaService folhaService;
	
		
	@Override
	@Transactional
	public Aposentadoria salvar(Aposentadoria entidade) throws SRHRuntimeException {
		
		
		validaDadosObrigatorios(entidade);
		
		verificaAposentadoriaCadastrada(entidade);
		
		Funcional funcional = funcionalService.getById(entidade.getFuncional().getId());			
		
		verificaTipoOcupacao(funcional);
		
		entidade = dao.salvar(entidade);		
		
		atualizaFuncional(entidade, funcional, false);
		
	    
		return entidade;
	}
	
	
	@Override
	@Transactional
	public void excluir(Aposentadoria entidade) {
		
		Funcional funcional = funcionalService.getById(entidade.getFuncional().getId());
		
		atualizaFuncional(entidade, funcional, true);		
		
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
	

	private void validaDadosObrigatorios(Aposentadoria entidade) throws SRHRuntimeException {
		
		if (entidade.getFuncional() == null)
			 throw new SRHRuntimeException("O Funcionário é obrigatório. Digite o nome e efetue a pesquisa.");

		if (entidade.getTipoBeneficio() == null)
			throw new SRHRuntimeException("O Tipo Benefício é obrigatório.");
		
		if ( entidade.getDataAto() == null )
			throw new SRHRuntimeException("A Data Ato é obrigatória.");
		else if (!dataDepoisDaNomeacao(entidade.getFuncional(), entidade.getDataAto()))
			throw new SRHRuntimeException("A Data Ato deve ser posterior a data de Exercício Funcional.");
		
		if (entidade.getNumeroResolucao() == null || entidade.getNumeroResolucao().intValue() <= 0)
			throw new SRHRuntimeException("O Número Resolução é obrigatório.");
		
		if (entidade.getAnoResolucao() == null || entidade.getAnoResolucao().intValue() <= 0)
			throw new SRHRuntimeException("O Ano Resolução é obrigatório.");
		else if (anoAnteriorANomeacao(entidade.getFuncional(), entidade.getAnoResolucao()))
			throw new SRHRuntimeException("O Ano Resolução não pode ser anterior ao ano do Exercício Funcional.");
		
		if ( entidade.getDataUltimaContagem() == null )
			throw new SRHRuntimeException("A Data Última Contagem é obrigatória.");
		else if (!dataDepoisDaNomeacao(entidade.getFuncional(), entidade.getDataUltimaContagem()))
			throw new SRHRuntimeException("A Data Última Contagem deve ser posterior a data de Exercício Funcional.");

		if ( entidade.getDataInicioBeneficio() != null &&		
				!entidade.getDataInicioBeneficio().after(entidade.getDataUltimaContagem()))
			throw new SRHRuntimeException("A Data Início Benefício deve ser posterior a Data Última Contagem.");
		
	}
	
	
	private boolean dataDepoisDaNomeacao(Funcional funcional, Date data) {
		
		return data.after(funcional.getExercicio());		
	}
	
	
	private boolean anoAnteriorANomeacao(Funcional funcional, Long ano) {
		
		Calendar exercicio = new GregorianCalendar();
		exercicio.setTime(funcional.getExercicio());
		
		
		return ano < exercicio.get(Calendar.YEAR);		
	}
	
	
	private void verificaTipoOcupacao(Funcional funcional) {
		Long idTipoOcupacao = funcional.getOcupacao().getTipoOcupacao().getId();
		
		if (idTipoOcupacao > 3)
			throw new SRHRuntimeException("Apenas Servidores com ocupação do tipo Membro, Efetivo ou Funcionário Estabilizado podem ser aposentados.");
	}
	
	
	private void atualizaFuncional(Aposentadoria entidade, Funcional funcional, boolean excluirAposentadoria) {

		if (excluirAposentadoria) {
			
			funcional.setStatus(EnumStatusFuncional.ATIVO.getId());
			
			if (funcional.getOcupacao().getTipoOcupacao().getId() == 1L) // Membros
				funcional.setFolha(folhaService.getByCodigo("01"));	// Conselheiros Ativos
			else 
				funcional.setFolha(folhaService.getByCodigo("03")); // Pessoal Ativo
			
			funcional.setAposentadoria(null);
		
		} else {
			
			funcional.setStatus(EnumStatusFuncional.INATIVO.getId());
			
			if (funcional.getOcupacao().getTipoOcupacao().getId() == 1L) // Membros
				funcional.setFolha(folhaService.getByCodigo("06"));	// Conselheiros Aposentados
			else 
				funcional.setFolha(folhaService.getByCodigo("07")); // Servidores Aposentados
			
			funcional.setAposentadoria(entidade);
			
		}		
		
		funcionalService.salvar(funcional);
	}
	
	
	private void verificaAposentadoriaCadastrada(Aposentadoria entidade) {
		
		if ( entidade.getId() == null || entidade.getId() == 0 ) {
		
			List<Aposentadoria> aposentadoria = dao.search(entidade.getFuncional().getPessoal().getId(), 0, 1);
		
			if (aposentadoria.size() == 1)
				throw new SRHRuntimeException("O Servidor já possui aposentadoria cadastrada.");
		}
	}
	
}
