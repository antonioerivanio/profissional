package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.DependenteDAO;
import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("dependenteService")
public class DependenteServiceImpl implements DependenteService{

	@Autowired
	private DependenteDAO dependenteDAO;	
	
	
	@Override
	@Transactional	
	public void salvar(Dependente entidade, boolean alterar) throws SRHRuntimeException {
		
		validarDados(entidade, alterar);
		
		if (alterar && entidade.getDataFim() != null){
			entidade.setDepIr(false);
			entidade.setDepPrev(false);
			entidade.setDepSf(false);
			entidade.setFlUniversitario(false);
		}
		
		dependenteDAO.salvar(entidade);
	}

	@Override
	@Transactional
	public void excluir(Dependente entidade) {
		dependenteDAO.excluir(entidade);		
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
	
	private void validarDados(Dependente entidade, boolean alterar) throws SRHRuntimeException{
		
		if(entidade == null)
			throw new SRHRuntimeException("O Servidor é obrigatório. Digite o nome e efetue a pesquisa.");
		
		if(entidade.getResponsavel() == null || entidade.getResponsavel().getId() == 0L)
			throw new SRHRuntimeException("O Servidor é obrigatório. Digite o nome e efetue a pesquisa.");
		
		if(entidade.getDependente() == null || entidade.getDependente().getId() == 0L)
			throw new SRHRuntimeException("O Dependente é obrigatório.");
		
		if (!alterar){
			if(dependenteDAO.findByResponsavelAndDependente(entidade.getResponsavel().getId(), entidade.getDependente().getId()) != null)
				throw new SRHRuntimeException("O Resposável já possui este Dependente.");
		}
		
		if(entidade.getTipoDependencia() == null)
			throw new SRHRuntimeException("O Tipo Dependência é obrigatório.");
			
		if(entidade.getMotivoInicio() == null || entidade.getMotivoInicio().getId() == 0L)
			throw new SRHRuntimeException("O Motivo Início é obrigatório.");
		
		if(entidade.getDataInicio() == null)
			throw new SRHRuntimeException("A Data Início é obrigatória.");
		
		if(entidade.getTipoDuracao() == 0L)
			throw new SRHRuntimeException("A Duração é obrigatória.");		
					
		if(entidade.getMotivoFim() != null && entidade.getDataFim() == null)
			throw new SRHRuntimeException("Informe a Data Fim.");
		
		if(entidade.getMotivoFim() == null && entidade.getDataFim() != null)
			throw new SRHRuntimeException("Informe o Motivo Fim.");
		
		
	}

}
