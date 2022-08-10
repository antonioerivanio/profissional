package br.gov.ce.tce.srh.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.DependenteDAO;
import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("dependenteService")
public class DependenteServiceImpl implements DependenteService{

	@Autowired
	private DependenteDAO dependenteDAO;
	
	@Autowired
	private PessoalService pessoalService;
	
	
	@Override
	@Transactional	
	public void salvar(Dependente entidade, boolean alterar) throws SRHRuntimeException {
		
		validarDadosObrigatorios(entidade);
			
		verificarSeDependenteJaFoiCadastrado(entidade, alterar);
		
		validarDeAcordoComAIdade(entidade);		
			
		validarDataInicio(entidade);
		
		atualizarFlagsDeDependencia(entidade, alterar);		
		
		dependenteDAO.salvar(entidade);
		
		// Atualiza QTDDEPSF, QTDDEPPREV e QTDDEPIR no registro do Responsável na tabela TB_PESSOAL
		atualizaQtdDependentes(entidade.getResponsavel().getId());		
		
	}

	private void validarDataInicio(Dependente entidade) {
		if (entidade.getDependente().getNascimento() != null && entidade.getDependente().getNascimento().after(entidade.getDataInicio())) {
			throw new SRHRuntimeException("A data início não pode ser anterior a data de nascimento do dependente.");
		}
	}

	private void validarDeAcordoComAIdade(Dependente entidade) {
		
		Long [] idsQuePermitemApenasMenoresDe21Anos = {3L, 6L, 8L, 10L};		
		List<Long> listaIdsQuePermitemApenasMenoresDe21Anos = Arrays.asList(idsQuePermitemApenasMenoresDe21Anos);
			
		if (entidade.getDependente().getIdade() >= 21 && listaIdsQuePermitemApenasMenoresDe21Anos.contains(entidade.getTipoDependencia().getId())) {
			throw new SRHRuntimeException("O Tipo Dependência selecionado permite apenas dependentes menores de 21 anos.");
		}
		
		//Filho(a) ou enteado(a) cursando estab. de ensino superior ou escola técnica de 2º grau, até 24 anos
		if (entidade.getDependente().getIdade() >= 25 && entidade.getTipoDependencia().getId().intValue() == 12) {
			throw new SRHRuntimeException("O Tipo Dependência selecionado permite apenas dependentes menores de 25 anos.");
		}		
	}

	private void verificarSeDependenteJaFoiCadastrado(Dependente entidade, boolean alterar) {
		if (!alterar){
			if(dependenteDAO.findByResponsavelAndDependente(entidade.getResponsavel().getId(), entidade.getDependente().getId()) != null)
				throw new SRHRuntimeException("O Resposável já possui este Dependente.");
		}
	}

	private void atualizarFlagsDeDependencia(Dependente entidade, boolean alterar) {
		if (alterar && entidade.getDataFim() != null){
			entidade.setDepIr(false);
			entidade.setDepPrev(false);
			entidade.setDepSf(false);
			entidade.setFlUniversitario(false);
		}
	}

	@Override
	@Transactional
	public void excluir(Dependente entidade) {
		
		dependenteDAO.excluir(entidade);
		
		// Atualiza QTDDEPSF, QTDDEPPREV e QTDDEPIR no registro do Responsável na tabela TB_PESSOAL
		
		atualizaQtdDependentes(entidade.getResponsavel().getId());		
		
	}

	@Override	
	public List<Dependente> findAll() {
		return dependenteDAO.findAll();
	}

	@Override
	public Dependente getById(Long id) {
		return dependenteDAO.getById(id);
	}

	@Override
	public int count(Long idPessoal) {
		return dependenteDAO.count(idPessoal);
	}

	@Override
	public List<Dependente> search(Long idPessoal, int first, int rows) {
		return dependenteDAO.search(idPessoal, first, rows);
	}
	
	private void validarDadosObrigatorios(Dependente entidade) throws SRHRuntimeException{
		
		if(entidade == null)
			throw new SRHRuntimeException("O Servidor é obrigatório. Digite o nome e efetue a pesquisa.");
		
		if(entidade.getResponsavel() == null || entidade.getResponsavel().getId() == 0L)
			throw new SRHRuntimeException("O Servidor é obrigatório. Digite o nome e efetue a pesquisa.");
		
		if(entidade.getDependente() == null || entidade.getDependente().getId() == 0L)
			throw new SRHRuntimeException("O Dependente é obrigatório.");		
		
		if(entidade.getTipoDependencia() == null)
			throw new SRHRuntimeException("O Tipo Dependência é obrigatório.");
			
		if(entidade.getMotivoInicio() == null || entidade.getMotivoInicio().getId() == 0L)
			throw new SRHRuntimeException("O Motivo Início é obrigatório.");
		
		// A Data Início é obrigatória para todos os tipo de dependência exceto Conjuge
		if(entidade.getDataInicio() == null && entidade.getTipoDependencia().getId() != 1L)
			throw new SRHRuntimeException("A Data Início é obrigatória.");
		
		if(entidade.getTipoDuracao() == 0L)
			throw new SRHRuntimeException("A Duração é obrigatória.");		
					
		if(entidade.getMotivoFim() != null && entidade.getDataFim() == null)
			throw new SRHRuntimeException("Informe a Data Fim.");
		
		if(entidade.getMotivoFim() == null && entidade.getDataFim() != null)
			throw new SRHRuntimeException("Informe o Motivo Fim.");
		
		
	}
	
	@Transactional
	public void atualizaQtdDependentes(Long idResponsavel){
		
		Pessoal responsavel = pessoalService.getById(idResponsavel);
		
		List<Dependente> dependentes = dependenteDAO.findByResponsavel(responsavel.getId());
		
		long qtdDepIr = 0;
		long qtdDepPrev = 0;
		long qtdDepSf = 0;
		
		for (Dependente d: dependentes){
			if(d.isDepIr())
				qtdDepIr++;
			if(d.isDepPrev())
				qtdDepPrev++;
			if(d.isDepSf())
				qtdDepSf++;			
		}		
		
		responsavel.setQtdDepir(qtdDepIr);
		responsavel.setQtdDepprev(qtdDepPrev);
		responsavel.setQtdDepsf(qtdDepSf);
		responsavel.setPermitidoValidarCampoTipoDeficiencia(Boolean.FALSE);
		pessoalService.salvar(responsavel);
		
	}

	@Override
	public List<Dependente> find(Dependente dependente) {
		return dependenteDAO.find(dependente);
	}

	@Override
	public List<Dependente> listaParaDarBaixa(List<Dependente> dependentes) {
		
		List<Dependente> listaParaDarBaixa = new ArrayList<>();
				
		for (Dependente dependente : dependentes) {
			
			if (dependente.isDepIr() || dependente.isDepPrev() || dependente.isDepSf() || dependente.isFlUniversitario()) {
			
				// Filho(a) não emancipado menor de 21 anos
				if (dependente.getTipoDependencia().getId() == 3){				
					if (dependente.getDependente().getIdade() >= 21)
						listaParaDarBaixa.add(dependente);
					
				// Irmão não emancipado menor de 21 anos com dependência econômica
				} else if (dependente.getTipoDependencia().getId() == 6){
					if (dependente.getDependente().getIdade() >= 21)
						listaParaDarBaixa.add(dependente);
				
				// Enteado não emancipado menor de 21 anos com dependência econômica
				} else if (dependente.getTipoDependencia().getId() == 8){
					if (dependente.getDependente().getIdade() >= 21)
						listaParaDarBaixa.add(dependente);
				
				// Menor tutelado não emancipado menor de 21 anos com dependência econômica	
				} else if (dependente.getTipoDependencia().getId() == 10){
					if (dependente.getDependente().getIdade() >= 21)
						listaParaDarBaixa.add(dependente);
				
				// Filho(a) ou enteado(a) cursando estab. de ensino superior ou escola técnica de 2º grau, até 24 anos	
				} else if (dependente.getTipoDependencia().getId() == 12){
					if (dependente.getDependente().getIdade() > 24)
						listaParaDarBaixa.add(dependente);
				
				} 
			
			}
			
			
		}
		
		
		return listaParaDarBaixa;
	}

	@Override
	public List<Dependente> findByResponsavel(Long idResponsavel) {
		return dependenteDAO.findByResponsavel(idResponsavel);
	}	

}
